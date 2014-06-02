package neoTest;

import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.NullPointerException;
import java.util.*;
class CompTypeComparator implements Comparator<String[]> { 
  public int compare(String[] o1, String[] o2) {
    return (o1[1].compareTo(o2[1]));
  }
}
public class App 
{
    public static void writeNodePropVal(String[] values,String[] props,PrintWriter out,String template,int startIndex,boolean append)
    {
      if (values.length>props.length)
      {
        System.out.println("more vals than prop names! "+values.length+'>'+props.length+" for "+values[1]+','+values[2]);
        System.exit(0);
      }
      for (int i = startIndex; i < values.length; i++) {
        values[i]=values[i].trim();
        if(!values[i].equals(""))
        {
          if(append)
            out.print(",");
          if(values[i].length()==3 && values[i].charAt(0)=='\"' && values[i].charAt(1)=='-')
            values[i] = values[i].replace("\"","\'");
          String p=props[i];
          String v=values[i];
          out.println(template.replace("%P%",props[i]).replace("%V%",values[i]));
          append=true;
        }
      }
    }
    public static ArrayList<String[]> readCodesCVS(InputStreamReader dis) throws IOException
    {
      ArrayList<String[]> al = new ArrayList();
      String props[] = null;
      String iString = "";
      int iCh = -1;
      boolean nl = false;
      while (!nl && (iCh=dis.read()) != -1) {
          if( (char) iCh != '\n')
            iString += (char) iCh;
           else
            nl = true;
          }
      iString = iString.replace(' ','_');
      iString = iString.replace("\"","");//property names must not quoted
      props = iString.trim().split(";");
      al.add(props);
      do
      {
        iString = "";
        String values[] = null;
        boolean multipleLine = false;
        boolean stringStart=false;
        do
        {
          nl = false;
          multipleLine = false;
          while (!nl && (iCh=dis.read()) != -1) {
              //remove ';' in string
              if (iCh=='"' && !stringStart)
                stringStart=true;
              else
                if (iCh=='"' && stringStart)
                  stringStart=false;
              if (stringStart && iCh==';')
                iCh=',';

              if( (char) iCh != '\n')
                iString += (char) iCh;
              else
                nl = true;
              }
          if(iCh!=-1)
          {
            values = iString.split(";");
            if(values[values.length-1].charAt(0)=='\"')
            {
              if((!values[values.length-1].trim().endsWith("\"")) || values[values.length-1].trim().length()==1 )
              {
                System.out.println("multipleLine! " + values[0]);
                multipleLine = true;
                iString += '\n';
                values = null;
              }
            }
          }
        }while(multipleLine);
        if(values!=null)
          al.add(values);
      }while(iCh != -1);
      return al;
    }
    public static String[] findEqCode(String code,ArrayList<String[]> al)
    {
      int c=0;
      while(c<al.size() && !al.get(c)[0].equals(code))
        c++;
      if(c<al.size() && al.get(c)[0].equals(code))
        return al.get(c);
      else
        return null;
    }
    public static void main( String[] args ) throws IOException
    {
        File DataEQUfile = new File("Setad_Equals.txt");
        FileInputStream fisEQU = new FileInputStream(DataEQUfile);
        InputStreamReader dis = new InputStreamReader(fisEQU,"UTF-8");
        //read equal codes
        ArrayList<String[]> alEquCodes=readCodesCVS(dis);
        fisEQU.close();
        dis.close();
        //convert codes to string
        for (int ii=1;ii<alEquCodes.size();ii++)
        {
          String[] strAr=alEquCodes.get(ii);
          for (int ij=0;ij<strAr.length;ij++)
          {
            if(!strAr[ij].trim().equals(""))
              strAr[ij]='"'+strAr[ij].trim()+'"';
          }
          alEquCodes.set(ii,strAr);
        }
        int cNonConsistant=0;
        PrintWriter out = new PrintWriter("outPut.txt","UTF-8");
        PrintWriter outerror = new PrintWriter("error.txt","UTF-8");
        PrintWriter outCNT = new PrintWriter("outPutCNT.txt","UTF-8");
        PrintWriter tmpout=outerror;
        String templateForAddNodeWithPropertyAndLable1 = "CREATE (:ÏæÑå { ";
        String templateForAddCNTWithPropertyAndLable1 = "MERGE (n:ãÍÊæÇ { ßÏ_ÏæÑå_ÏÑ_äÝÊ: %C%}) SET";
        String templateForAddNodeWithPropertyAndLable2 = "%P% : %V%";
        String templateForAddCNTWithPropertyAndLable2 = " n.%P%_%F% = %V%";
        String templateForAddNodeWithPropertyAndLable3 = "});";
        String templateForAddCNTWithPropertyAndLable3 = ";";
        File Datafile = new File("Setad_CODE.txt");
        File DataCNTfile = new File("Setad_CNT.txt");
        int counter=1;
        FileInputStream fis = null;
        FileInputStream fisCNT = null;
        dis = null;
        try {
          fis = new FileInputStream(Datafile);
          fisCNT = new FileInputStream(DataCNTfile);
          dis = new InputStreamReader(fis,"UTF-8");
          ArrayList<String[]> alCodes=readCodesCVS(dis);
          fis.close();
          dis.close();

          String props[] = alCodes.get(0);
          while (counter<alCodes.size())
          {
            String values[] =alCodes.get(counter);
            if(values[1].length()==4 || values[1].length()==1)
              values[1]='0'+values[1];
            if(values[2].equals("")) //check if code_dore is null
            {
              if(values[1].length()==5) //build code_dore from tartib
                values[2]='"'+values[1]+'"';
              else
              {
                if(values[3].length()==5 && values[1].length()<=2) //build code_dore from tartib & gorooh
                {
                  if (values[1].length()==2)
                    values[2]='"'+values[3].replace("\"","")+values[1]+'"';
                  else
                    System.out.println("can not create code! naft_code=" + values[0]);
                }
                else
                  System.out.println("can not create code! naft_code=" + values[0]);
              }
              if(!values[2].equals(""))
                System.out.println("auto create code=" + values[2]);
            }
            if(values[2].length()==7) //so here we must have code_dore not is null
            {
              if(values[3].equals("")) //check if gorooh is null
              {
                values[3]=values[2].substring(0,4)+'"'; //build gorooh from code_dore
                System.out.println("auto create gorooh=" + values[3]);
              }
              if(values[1].equals("")) //check if tartib is null
              {
                values[1]=values[2].substring(4,6); //buid tartib from code_dore
                System.out.println("auto create tartib=" + values[1] + ' ' + values[2]);
              }
            }
            boolean conststant=false;
            //chck that tartib & gorooh consistant
            if(values[1].length()>2)
              if(values[3].equals('"'+values[1].substring(0,3)+'"'))
              {
                values[1]=values[1].substring(values[1].length()-2);
                conststant=true;
              }
              else
                System.out.println("not consistant gorooh="+values[3]+" tartib="+values[1]);
            else
              conststant=true;
            //chck that code_dore & tartib & gorooh consistant
            if (!(conststant && values[3].length()==5 && values[2].equals(values[3].substring(0,4)+values[1]+'"')))
            {
              System.out.println("not consistant gorooh="+values[3]+" code="+values[2]+" tartib="+values[1]);
              conststant=false;
            }
            values[1]='"'+values[1]+'"'; //create string tartib
            if (!conststant && out!=outerror)
            {
              //temperary save out file
              tmpout=out;
              //switch out to error
              out=outerror;
            }
            else
              if (out==outerror && conststant)
                out=tmpout; //reset file to pre
            if(!conststant)
              cNonConsistant++;
            //TODO check pishniyaz code
            out.println("begin");
            out.println(templateForAddNodeWithPropertyAndLable1);
            writeNodePropVal(values,props,out,templateForAddNodeWithPropertyAndLable2,0,false);
            if(conststant)
            {
              String[] eqCodes=findEqCode(values[2],alEquCodes);              
              if(eqCodes!=null)
              {
                //create string codes
                writeNodePropVal(eqCodes,alEquCodes.get(0),out,templateForAddNodeWithPropertyAndLable2,2,true);
              }
            }
            out.println(templateForAddNodeWithPropertyAndLable3);
            out.println("commit");   
            counter++;
          }
          // dispose all the resources after using them.
          System.out.println("END! error found="+cNonConsistant);
          alEquCodes.clear();
          alCodes.clear();
          out.close();
          //read contents in CNT
          dis = new InputStreamReader(fisCNT,"UTF-8");
          ArrayList<String[]> alCNT=readCodesCVS(dis);
          fisCNT.close();
          dis.close();
          props = alCNT.get(0);
          alCNT.remove(0);
          Collections.sort(alCNT, new CompTypeComparator());
          counter=0;
          int mohtavaCounter=0;
          String lastCode="";
          while(counter<alCNT.size())
          {
            String values[]=alCNT.get(counter);
            if(values!=null && values.length>3)
            {
              int i=3;
              boolean allEmpty=true;
              while(allEmpty && i<values.length)
              {
                if(!values[i].trim().equals(""))
                  allEmpty=false;
                i++;
              }
              if (!allEmpty)
              {
                outCNT.println("begin");
                outCNT.println(templateForAddCNTWithPropertyAndLable1.replace("%C%",values[1]));
                if (!values[1].equals(lastCode))
                {
                  lastCode=values[1];
                  mohtavaCounter=1;
                }
                else
                  mohtavaCounter++;
                writeNodePropVal(values,props,outCNT,templateForAddCNTWithPropertyAndLable2.replace("%F%",Integer.toString(mohtavaCounter)),3,false);
                outCNT.println(templateForAddCNTWithPropertyAndLable3);
                outCNT.println("commit");   
              }
            }
            counter++;
          }
          // dispose all the resources after using them.
          alCNT.clear();
          outCNT.close();
          outerror.close();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
    }
}

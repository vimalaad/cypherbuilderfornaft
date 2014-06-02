cd C:\01Converter\DataImport
set PATH="c:\apache-maven-3.0.5\bin";%PATH%
mvn compile exec:java -Dexec.mainClass=neoTest.App -DskipTests -o

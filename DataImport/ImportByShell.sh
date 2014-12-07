 #!/bin/bash
./neo4j-shell -path ~/neo4j/packaging/standalone/target/neo4j-community-2.2-SNAPSHOT/data/graph.db -file './01Converter/DataImport/CreateBaseNodes.txt'
./neo4j-shell -path ~/neo4j/packaging/standalone/target/neo4j-community-2.2-SNAPSHOT/data/graph.db -file './01Converter/DataImport/outPut.txt'
./neo4j-shell -path ~/neo4j/packaging/standalone/target/neo4j-community-2.2-SNAPSHOT/data/graph.db -file './01Converter/DataImport/CreateGorooh.txt'
./neo4j-shell -path ~/neo4j/packaging/standalone/target/neo4j-community-2.2-SNAPSHOT/data/graph.db -file './01Converter/DataImport/CreateRelations.txt'
./neo4j-shell -path ~/neo4j/packaging/standalone/target/neo4j-community-2.2-SNAPSHOT/data/graph.db -file './01Converter/DataImport/deleteCNT.txt'
./neo4j-shell -path ~/neo4j/packaging/standalone/target/neo4j-community-2.2-SNAPSHOT/data/graph.db -file './01Converter/DataImport/outPutCNT.txt'
./neo4j-shell -path ~/neo4j/packaging/standalone/target/neo4j-community-2.2-SNAPSHOT/data/graph.db -file './01Converter/DataImport/CreateRelationsMohtava.txt'
./neo4j-shell -path ~/neo4j/packaging/standalone/target/neo4j-community-2.2-SNAPSHOT/data/graph.db -file './01Converter/DataImport/CreatePackages.txt'

## Local Neo4J db start using docker 
docker run --publish=7474:7474 --publish=7687:7687 neo4j:4.3.6

## Connect to neo4j aura db
neo4j+s://787d0c42.databases.neo4j.io

## Building & Running
Local build and run without IDE:

    $ ./mvnw clean install && ./mvnw spring-boot:run
    $ ./mvnw clean install 
    $ ./mvnw spring-boot:run
    $ java -jar target/popara.jar

## Create indexes

    create index for (n:Carrier) on n.name;
    create index for (n:Country) on n.name;
    create index for (n:Journey) on n.name;
    create index for (n:Line) on n.name;
    create index for (n:Location) on n.name;
    create index for (n:Period) on n.name;
    create index for (n:Schedule) on n.name;
    create index for (n:Station) on n.name;
    create index for (n:Stop) on n.name;
    create index for (n:Trip) on n.name;

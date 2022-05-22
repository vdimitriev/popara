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


## Create indexes in Neo4J

    CREATE INDEX FOR (n:Carrier) ON (n.name);
    CREATE INDEX FOR (n:Country) ON (n.name);
    CREATE INDEX FOR (n:Journey) ON (n.name);
    CREATE INDEX FOR (n:Location) ON (n.name);
    CREATE INDEX FOR (n:Line) ON (n.name);
    CREATE INDEX FOR (n:Period) ON (n.name);
    CREATE INDEX FOR (n:Schedule) ON (n.name);
    CREATE INDEX FOR (n:Station) ON (n.name);
    CREATE INDEX FOR (n:Stop) ON (n.name);
    CREATE INDEX FOR (n:Trip) ON (n.name);

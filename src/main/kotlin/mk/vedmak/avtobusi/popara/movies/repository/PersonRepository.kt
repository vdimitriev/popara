package mk.vedmak.avtobusi.popara.movies.repository

import mk.vedmak.avtobusi.popara.movies.entity.Person
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository:Neo4jRepository<Person, Long>
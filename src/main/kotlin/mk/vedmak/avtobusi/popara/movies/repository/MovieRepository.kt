package mk.vedmak.avtobusi.popara.movies.repository

import mk.vedmak.avtobusi.popara.movies.entity.Movie
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface MovieRepository: Neo4jRepository<Movie, String>
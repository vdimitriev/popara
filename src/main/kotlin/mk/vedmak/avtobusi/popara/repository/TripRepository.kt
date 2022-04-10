package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Trip
import mk.vedmak.avtobusi.popara.movies.entity.Movie
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface TripRepository: Neo4jRepository<Trip, String> {

    @Query("MATCH (trp:Trip)-[ts]->(stp:Stop)-[sl]->(loc:Location) where loc.name in ['VE','SK'] RETURN distinct trp,ts,stp,sl,loc")
    fun findByDepartureAndArrival(): List<Trip>

//    @Query("MATCH (trp:Trip {name: 'TRA0101'}) -[*]-> (stp:Stop) RETURN trp")
//    fun findByDepartureAndArrival(): List<Trip>
}
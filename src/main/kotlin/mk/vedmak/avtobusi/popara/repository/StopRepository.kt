package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Stop
import mk.vedmak.avtobusi.popara.model.Trip
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface StopRepository: Neo4jRepository<Stop, String> {
//    @Query("MATCH (trp:Trip {name: 'TRA0101'}) -[*]-> (stp:Stop) RETURN stp")
//    fun findByDepartureAndArrival(): List<Stop>

}
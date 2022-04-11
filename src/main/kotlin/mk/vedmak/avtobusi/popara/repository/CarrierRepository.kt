package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Carrier
import mk.vedmak.avtobusi.popara.model.Journey
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface CarrierRepository: Neo4jRepository<Carrier, String> {

    @Query("MATCH (j:Journey)-[c:CONTAINS]->(t:Trip)-[sa:STOPS_AT]->(s:Stop) MATCH (j:Journey)<-[p:PERFORMS]-(l:Line)<-[o:OPERATES]-(car:Carrier) where j.departure = 'BT' and j.arrival = 'OH' RETURN car,collect(o),collect(l),collect(p),collect(j),collect(c),collect(t),collect(sa),collect(s)")
    fun findJourneys(): Collection<Carrier>

}
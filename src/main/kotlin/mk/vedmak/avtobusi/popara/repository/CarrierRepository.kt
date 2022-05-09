package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Carrier
import mk.vedmak.avtobusi.popara.model.Journey
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(propagation = Propagation.REQUIRES_NEW)
interface CarrierRepository: Neo4jRepository<Carrier, String>
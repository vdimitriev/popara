package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Location
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface LocationRepository: Neo4jRepository<Location, String>
package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Journey
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface JourneyRepository: Neo4jRepository<Journey, String> {
}
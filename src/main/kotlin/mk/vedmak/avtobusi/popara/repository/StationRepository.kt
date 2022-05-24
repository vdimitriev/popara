package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Carrier
import mk.vedmak.avtobusi.popara.model.Station
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface StationRepository: ReactiveNeo4jRepository<Station, String> {
}
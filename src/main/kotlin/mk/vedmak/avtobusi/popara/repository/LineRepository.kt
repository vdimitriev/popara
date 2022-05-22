package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Line
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface LineRepository: Neo4jRepository<Line, String> {

}
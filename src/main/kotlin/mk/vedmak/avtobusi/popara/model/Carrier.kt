//package mk.vedmak.avtobusi.popara.model
//
//import mk.vedmak.avtobusi.popara.movies.entity.Person
//import org.springframework.data.neo4j.core.schema.Id
//import org.springframework.data.neo4j.core.schema.Node
//import org.springframework.data.neo4j.core.schema.Relationship
//
//@Node
//data class Carrier(
//
//    @Id
//    val id: Long,
//
//    val name: String,
//
//    @Relationship(type="OPERATED_BY", direction = Relationship.Direction.INCOMING)
//    val lines: Set<Line> = HashSet()
//)
//package mk.vedmak.avtobusi.popara.movies.entity
//
//import org.springframework.data.neo4j.core.schema.GeneratedValue
//import org.springframework.data.neo4j.core.schema.Id
//import org.springframework.data.neo4j.core.schema.RelationshipProperties
//import org.springframework.data.neo4j.core.schema.TargetNode
//
//@RelationshipProperties
//data class Actor(
//
//    @Id
//    @GeneratedValue
//    val id: Long,
//
//    @TargetNode
//    val person: Person,
//
//    val roles: List<String> = ArrayList()
//)
//package mk.vedmak.avtobusi.popara.movies.entity
//
//import org.neo4j.ogm.annotation.*
//
//@RelationshipEntity(type = "ACTED_IN")
//data class RoleEntity(
//
//    @Id
//    @GeneratedValue
//    val id: Long,
//
//    val roles: Collection<String>,
//
//    @StartNode
//    val person: PersonEntity,
//
//    @EndNode
//    val movie: MovieEntity
//)
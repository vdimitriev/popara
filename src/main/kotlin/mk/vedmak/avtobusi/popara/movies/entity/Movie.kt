package mk.vedmak.avtobusi.popara.movies.entity

import org.springframework.data.neo4j.core.schema.*

@Node
data class Movie(

    @Id
    val title: String,

    @Property("tagline")
    val description: String?,

//    @Relationship(type="ACTED_IN", direction = Relationship.Direction.INCOMING)
//    val actors: Set<Actor> = HashSet(),
//
//    @Relationship(type="DIRECTED", direction = Relationship.Direction.INCOMING)
//    val directors: Set<Person> = HashSet()

)
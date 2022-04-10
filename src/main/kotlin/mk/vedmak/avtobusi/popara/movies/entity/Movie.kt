package mk.vedmak.avtobusi.popara.movies.entity

import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING

@Node
data class Movie(

    @Id
    @GeneratedValue
    val id: Long,

    val title: String,

    @Property("tagline")
    val description: String?,

    @Relationship(type="ACTED_IN", direction = INCOMING)
    val actors: Set<Actor> = HashSet(),

    @Relationship(type="DIRECTED", direction = INCOMING)
    val directors: Set<Director> = HashSet()

)
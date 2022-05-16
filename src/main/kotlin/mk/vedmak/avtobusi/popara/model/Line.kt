package mk.vedmak.avtobusi.popara.model

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING

@Node
data class Line(

    @Id
    val name: String?,

    val fullNameLatin: String?,

    val fullNameCyrillic: String?,

    val lineNumber: Int,

    @Relationship("PERFORMS", direction = OUTGOING)
    var journeys: MutableSet<Journey> = HashSet(),

    val description: String? = null,

    val descriptionLatin: String? = null,

    val descriptionCyrillic: String? = null,

//    @Version
//    val version: Long = 0,
//
    ) {
    override fun toString(): String {
        return "$name - $journeys"
    }
}
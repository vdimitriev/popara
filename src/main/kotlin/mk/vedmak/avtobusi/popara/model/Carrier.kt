package mk.vedmak.avtobusi.popara.model

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING

@Node
data class Carrier(

    @Id
    val name: String,

    val carrierNumber: Int? = 0,

    val location: String? = null,

    @Relationship(type="OPERATES", direction = OUTGOING)
    val lines: MutableList<Line> = mutableListOf(),

    val lineCount: Int? = 0,

    val latinName: String? = null,

    val cyrillicName: String? = null,

    val description: String? = null,

    val descriptionLatin: String? = null,

    val descriptionCyrillic: String? = null,

    @Version
    val version: Long = 0,

    ) {
    override fun equals(other: Any?): Boolean = other is Carrier && other.name == name

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String {
        return "$name - $lines"
    }
}
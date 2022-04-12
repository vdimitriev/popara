package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING

@Node
data class Carrier(

    @Id
    val name: String?,

    val location: String?,

    @Relationship(type="OPERATES", direction = OUTGOING)
    val lines: MutableList<Line>? = null,

    val latinName: String? = null,

    val cyrillicName: String? = null,

    val description: String? = null,

    val descriptionCyrillic: String? = null,
)
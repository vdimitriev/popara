package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING

@Node
data class Journey(

    @Id
    val name: String?,

    val lineNumber: Int?,

    val journeyNumber: Int?,

    val departure: String?,

    val arrival: String?,

    @Relationship("CONTAINS", direction = OUTGOING)
    val trips: List<Trip>? = null,

    val description: String? = null
)
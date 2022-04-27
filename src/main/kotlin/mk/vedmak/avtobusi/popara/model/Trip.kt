package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING

@Node
data class Trip(

    @Id
    val name: String?,

    val lineNumber: Int?,

    val journeyNumber: Int?,

    val tripNumber: Int?,

    @Relationship("STOPS", direction = OUTGOING)
    val stops: List<Stop>? = null,

    @Relationship("MAINTAINS", direction = OUTGOING)
    val schedule: Schedule? = null,

    val description: String? = null

)
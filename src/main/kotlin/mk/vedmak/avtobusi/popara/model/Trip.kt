package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
data class Trip(
    @Id
    val name: String?,
    val tripNumber: Int?,
    val lineNumber: Int?,
    @Relationship("STOPS_AT")
    val stops: List<Stop>,
    val description: String? = null
)
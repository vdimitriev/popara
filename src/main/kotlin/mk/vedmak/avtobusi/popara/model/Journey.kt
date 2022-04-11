package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
data class Journey(
    @Id
    val name: String?,
    val lineNumber: Int?,
    val journeyNumber: Int?,
    val departure: String?,
    val arrival: String?,
    @Relationship("PART_OF")
    val line: Line?,
    @Relationship("CONTAINS")
    val trips: List<Trip> = ArrayList(),
    val description: String? = null
)
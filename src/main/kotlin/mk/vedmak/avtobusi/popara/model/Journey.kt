package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
data class Journey(
    @Id
    val name: String?,
    @Relationship("DEPARTS_AT")
    val departure: Location,
    @Relationship("ARRIVES_AT")
    val arrival: Location,
    @Relationship("CONTAINS")
    val trips: List<Trip> = ArrayList(),
    val description: String? = null
)
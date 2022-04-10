package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class Station(
    @Id
    val name: String,
    val latinName: String? = null,
    val cyrillicName: String? = null,
    val address: String? = null,
    val telephoneNumber: String? = null,
    val location: Location? = null
)
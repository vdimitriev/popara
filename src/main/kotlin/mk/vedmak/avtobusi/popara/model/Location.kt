package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class Location(

    @Id
    val name: String?,

    val latinName: String?,

    val cyrillicName: String?,

    val country: Country? = null,

    val category: Int? = 0,

    val description: String? = null,
)
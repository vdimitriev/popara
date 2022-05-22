package mk.vedmak.avtobusi.popara.model

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING

@Node
data class Location(

    @Id
    val name: String?,

    val latinName: String?,

    val cyrillicName: String?,

    @Relationship("SITUATED", direction = OUTGOING)
    val country: Country? = null,

    val category: Int? = 0,

    val description: String? = null,

    )
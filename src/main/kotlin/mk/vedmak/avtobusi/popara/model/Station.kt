package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING

@Node
data class Station(

    @Id
    val name: String,

    val latinName: String? = null,

    val cyrillicName: String? = null,

    val address: String? = null,

    val telephoneNumber: String? = null,

    @Relationship(type="LOCATED", direction = OUTGOING)
    val location: Location? = null,

)
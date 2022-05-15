package mk.vedmak.avtobusi.popara.model

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING
import java.time.LocalTime

@Node
data class Stop(

    @Id
    val name: String,

    val time: LocalTime?,

    val location: String,

    val stopNumber: Int? = 0,

    val description: String? = null,

    @Version
    val version: Long = 0,

    ) {
    override fun toString(): String {
        return "$name"
    }
}
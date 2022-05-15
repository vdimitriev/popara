package mk.vedmak.avtobusi.popara.model

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING
import java.time.LocalTime

@Node
data class Trip(

    @Id
    val name: String,

    val lineNumber: Int = 0,

    val journeyNumber: Int = 0,

    val tripNumber: Int = 0,

    @Relationship("STOPS", direction = OUTGOING)
    var stops: MutableList<Stop> = ArrayList(),

    @Relationship("MAINTAINS", direction = OUTGOING)
    val schedules: List<Schedule> = ArrayList(),

    val description: String? = null,

    @Version
    val version: Long = 0,

    var travelTime: String = "",

    var scheduledDepartureTime: LocalTime? = null,

    var scheduledArrivalTime: LocalTime? = null,

    ) {
    override fun toString(): String {
        return "$name - $travelTime - $scheduledDepartureTime - $scheduledArrivalTime"
    }
}
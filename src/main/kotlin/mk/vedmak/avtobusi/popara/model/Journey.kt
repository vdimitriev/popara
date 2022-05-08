package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING

@Node
data class Journey(

    @Id
    val name: String,

    val carrierName: String,

    val lineNumber: Int,

    var journeyNumber: Int?,

    val departure: String,

    val arrival: String,

    @Relationship("CONTAINS", direction = OUTGOING)
    var trips: MutableSet<Trip> = HashSet(),

    val description: String? = null,

    @Relationship("MAINTAINS", direction = OUTGOING)
    var schedules: List<Schedule> = ArrayList(),

    ) {

    override fun equals(other: Any?): Boolean = other is Journey && other.carrierName == carrierName && other.lineNumber == lineNumber && other.departure == departure && other.arrival == arrival

    override fun hashCode(): Int = carrierName.hashCode() + lineNumber.hashCode() + departure.hashCode() + arrival.hashCode()

    override fun toString(): String {
        return "$name - $trips"
    }
}
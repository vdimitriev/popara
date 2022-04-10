package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Trip
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TripRepositoryIT {

    @Autowired
    private lateinit var tripRepository: TripRepository

//    @Test
//    fun insertTrip() {
//        val trip = Trip(
//            id = null,
//            tripNumber = 1,
//            hashMapOf(Pair("Bitola", "13:15"), Pair("Resen", "14:05"), Pair("Skopje", "15:00"))
//        )
//        tripRepository.save(trip)
//
//        val allTrips = tripRepository.findAll()
//        assertTrue(allTrips.size > 0)
//
//        allTrips.forEach { println(it) }
//    }

    @Test
    fun deleteAllTrips() {
        tripRepository.deleteAll()
        assertTrue(tripRepository.findAll().size == 0)
    }
}
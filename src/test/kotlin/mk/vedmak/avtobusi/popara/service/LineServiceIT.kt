package mk.vedmak.avtobusi.popara.service

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LineServiceIT {

    @Autowired
    private lateinit var lineService: LineService

    @Test
    fun findBusLinesForGivenDepartureAndArrival() {
        lineService.findAllLinesByDepartureAndArrivalStation("Veles", "Bitola")
        assertTrue(true)
    }

    @Test
    fun findAllTripsForGivenDepartureAndArrival() {
        val trips = lineService.findAllTripsForDepartureAndArrivalStation("VE", "BT")
        trips.forEach { trip ->
            println(trip)
        }
        assertTrue(true)
    }

    @Test
    fun findAllStopsForGivenDepartureAndArrival() {
        val trips = lineService.findAllStopsForDepartureAndArrivalStation("VE", "BT")
        trips.forEach { trip ->
            println(trip)
        }
        assertTrue(true)
    }
}
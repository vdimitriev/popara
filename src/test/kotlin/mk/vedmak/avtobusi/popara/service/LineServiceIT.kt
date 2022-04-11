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
    fun findJourneys() {
        lineService.findJourneys().forEach {
            println(it)
        }
        assertTrue(true)
    }

    @Test
    fun findJourneysForCarrier() {
        lineService.findJourneysForCarrier().forEach {
            println(it)
        }
        assertTrue(true)
    }

    @Test
    fun findAllJourneys() {
        val j = lineService.findAllJourneys()
        j.forEach {
            println(it)
        }
        assertTrue(true)
    }

    @Test
    fun findAllJourneysForGivenDepartureAndArrival() {
        lineService
            .findAllJourneysForDepartureAndArrivalStation("BT", "OH")
            .forEach {
                println(it)
            }
        assertTrue(true)
    }

    @Test
    fun findAllJourneysForGivenDeparture() {
        lineService
            .findAllJourneysForDepartureStation("BT")
            .forEach {
                println(it)
            }
        assertTrue(true)
    }

    @Test
    fun findAllTripsForGivenDepartureAndArrival() {
        val trips = lineService.findAllTripsForDepartureAndArrivalStation("BT", "OH")
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
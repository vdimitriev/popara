package mk.vedmak.avtobusi.popara.service

import mk.vedmak.avtobusi.popara.model.Carrier
import mk.vedmak.avtobusi.popara.model.Journey
import mk.vedmak.avtobusi.popara.model.Stop
import mk.vedmak.avtobusi.popara.model.Trip
import mk.vedmak.avtobusi.popara.repository.*
import org.springframework.stereotype.Service

@Service
class LineService(
    val lineRepository: LineRepository,
    val tripRepository: TripRepository,
    val journeyRepository: JourneyRepository,
    val stopRepository: StopRepository,
    val carrierRepository: CarrierRepository
) {


    fun findAllLinesByDepartureAndArrivalStation(departure: String, arrival: String) {

    }

    fun findJourneysForCarrier():Collection<Carrier> {
        return carrierRepository.findJourneys()
    }

    fun findJourneys():Collection<Journey> {
        return journeyRepository.findJourneys()
    }

    fun findAllJourneys():List<Journey> {
        return journeyRepository.findAll()
    }

    fun findAllJourneysForDepartureAndArrivalStation(departure: String, arrival: String):Collection<Journey> {
        return journeyRepository.findByDepartureAndArrival(departure, arrival)
    }

    fun findAllJourneysForDepartureStation(departure: String):Collection<Journey> {
        return journeyRepository.findByDeparture(departure)
    }

    fun findAllTripsForDepartureAndArrivalStation(departure: String, arrival: String):List<Trip> {
        return tripRepository.findByDepartureAndArrival()
    }

    fun findAllStopsForDepartureAndArrivalStation(departure: String, arrival: String):List<Stop> {
        return stopRepository.findByDepartureAndArrival()
    }

}
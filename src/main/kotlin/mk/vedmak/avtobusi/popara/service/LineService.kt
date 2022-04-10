package mk.vedmak.avtobusi.popara.service

import mk.vedmak.avtobusi.popara.model.Stop
import mk.vedmak.avtobusi.popara.model.Trip
import mk.vedmak.avtobusi.popara.repository.CarrierRepository
import mk.vedmak.avtobusi.popara.repository.LineRepository
import mk.vedmak.avtobusi.popara.repository.StopRepository
import mk.vedmak.avtobusi.popara.repository.TripRepository
import org.springframework.stereotype.Service

@Service
class LineService(
    val lineRepository: LineRepository,
    val tripRepository: TripRepository,
    val stopRepository: StopRepository,
    val carrierRepository: CarrierRepository
) {


    fun findAllLinesByDepartureAndArrivalStation(departure: String, arrival: String) {

    }

    fun findAllTripsForDepartureAndArrivalStation(departure: String, arrival: String):List<Trip> {
        return tripRepository.findByDepartureAndArrival()
    }

    fun findAllStopsForDepartureAndArrivalStation(departure: String, arrival: String):List<Stop> {
        return stopRepository.findByDepartureAndArrival()
    }

}
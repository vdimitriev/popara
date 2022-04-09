package mk.vedmak.avtobusi.popara.movies.service

import mk.vedmak.avtobusi.popara.movies.entity.Movie
import mk.vedmak.avtobusi.popara.movies.repository.MovieRepository
import org.springframework.stereotype.Service


@Service
class MovieService(val movieRepository: MovieRepository) {

    fun getDirectedBy(name: String): List<Movie> {
        return movieRepository.findAll().toCollection(ArrayList())
    }

    fun getActedInBy(name: String): List<Movie> {
        return movieRepository.findAll().toCollection(ArrayList())
    }

}
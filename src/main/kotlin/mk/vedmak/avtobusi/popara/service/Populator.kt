package mk.vedmak.avtobusi.popara.service

import mk.vedmak.avtobusi.popara.movies.repository.MovieRepository
import mk.vedmak.avtobusi.popara.movies.repository.PersonRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class Populator(
    val movieRepository: MovieRepository,
    val personRepository: PersonRepository
) {

    private val logger = KotlinLogging.logger {}

    fun populate() {
        logger.info("call populator in popara")
        val size = movieRepository.findAll().size
        logger.info("movie size = $size")
        logger.info("finish with populator in popara")
    }
}
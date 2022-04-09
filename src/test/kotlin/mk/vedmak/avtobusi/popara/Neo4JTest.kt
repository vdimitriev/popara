package mk.vedmak.avtobusi.popara

import mk.vedmak.avtobusi.popara.movies.repository.MovieRepository
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Neo4JTest {

    @Autowired
    private lateinit var movieRepository: MovieRepository

    private val logger = KotlinLogging.logger {}

    @Test
    fun findAllMovies() {
        logger.info("find all movies")
        val movies = movieRepository.findAll()
        println("movies size = ${movies.size}")
        logger.info("found all movies")
        assertTrue(true)
    }
}
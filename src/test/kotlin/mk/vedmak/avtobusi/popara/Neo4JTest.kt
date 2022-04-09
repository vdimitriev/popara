package mk.vedmak.avtobusi.popara

import mk.vedmak.avtobusi.popara.movies.entity.Movie
import mk.vedmak.avtobusi.popara.movies.repository.MovieRepository
import mk.vedmak.avtobusi.popara.movies.repository.PersonRepository
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class Neo4JTest {

    @Autowired
    private lateinit var movieRepository: MovieRepository

    @Autowired
    private lateinit var personRepository: PersonRepository

    private val logger = KotlinLogging.logger {}

    @Test
    fun countAllMovies() {
        assertEquals(38, movieRepository.findAll().size)
    }

//    @Test
//    fun countAllPersons() {
//        assertEquals(38, personRepository.findAll().size)
//    }

}
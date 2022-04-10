package mk.vedmak.avtobusi.popara.movies

import mk.vedmak.avtobusi.popara.movies.repository.MovieRepository
import mk.vedmak.avtobusi.popara.movies.repository.PersonRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MoviesIT {

    @Autowired
    private lateinit var movieRepository: MovieRepository

    @Autowired
    private lateinit var personRepository: PersonRepository

    @Test
    fun deleteAllMoviesAndPersons() {
        deleteAll()
        assertTrue(movieRepository.findAll().size == 0)
        assertTrue(personRepository.findAll().size == 0)
    }

    private fun deleteAll() {
        movieRepository.deleteAll()
        personRepository.deleteAll()
    }

}
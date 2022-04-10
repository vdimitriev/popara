package mk.vedmak.avtobusi.popara.repository

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StopRepositoryIT {

    @Autowired
    private lateinit var stopRepository: StopRepository

    @Test
    fun deleteAllStops() {
        stopRepository.deleteAll()
        assertTrue(stopRepository.findAll().size == 0)
    }

    @Test
    fun findAllStops() {
        stopRepository.findAll().forEach { println(it) }
    }
}
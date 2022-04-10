package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Carrier
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CarrierRepositoryIT {

    @Autowired
    private lateinit var carrierRepository: CarrierRepository

    @Test
    fun carrierTest() {
    }

    @Test
    fun deleteAllCarriers() {
        carrierRepository.deleteAll()
        assertTrue(carrierRepository.findAll().size == 0)
    }
}
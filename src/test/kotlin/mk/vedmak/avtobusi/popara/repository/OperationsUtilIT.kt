package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.util.MacedonianToLatin
import mk.vedmak.avtobusi.popara.util.OperationsUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.random.Random

@SpringBootTest
class OperationsUtilIT {

    @Autowired
    private lateinit var operationsUtil: OperationsUtil

    @Test
    fun randomTest() {
        val random = UUID.randomUUID().toString()
        println(random)

        val random2 = operationsUtil.getRandom()
        println(random2)


    }

    @Test
    fun latinToMacedonianDotTest() {
        val macedonianName = "Св. пат"
        val latinName = operationsUtil.createLatinName(macedonianName)
        val subname = latinName.substring(0,4)
        println(subname.uppercase())
    }
    @Test
    fun latinToMacedonianDashTest() {
        val macedonianName = "Битола - Скопје"
        val latinName = operationsUtil.createLatinName(macedonianName)
        println(latinName)
    }

    fun splitWithDashTest() {
        val value = "Битола - Скопје"
        val values = value.split(" - ")
        values.forEach {
            println(it)
        }
    }
}
package mk.vedmak.avtobusi.popara.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component;
import java.util.*
import kotlin.random.Random

@Component
class OperationsUtil {

    @Autowired
    private lateinit var macedonianToLatin: MacedonianToLatin

    @Autowired
    private lateinit var latinToMacedonian: LatinToMacedonian

    fun createLatinName(name: String): String {
        return try {
            macedonianToLatin.translate(name)
        } catch (e: Exception) {
            ""
        }
    }
    fun createMacedonianName(name: String?): String? {
        return try {
            latinToMacedonian.translate(name)
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    fun getRandom(): String = UUID.randomUUID().toString()
}

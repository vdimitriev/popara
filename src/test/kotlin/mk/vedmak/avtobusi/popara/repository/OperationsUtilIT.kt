//package mk.vedmak.avtobusi.popara.repository
//
//import mk.vedmak.avtobusi.popara.util.MacedonianToLatin
//import mk.vedmak.avtobusi.popara.util.OperationsUtil
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//
//@SpringBootTest
//class OperationsUtilIT {
//
//    @Autowired
//    private lateinit var operationsUtil: OperationsUtil
//
//    @Test
//    fun latinToMacedonianTest() {
//        val macedonianName = "Св. пат"
//        val latinName = operationsUtil.createLatinName(macedonianName)
//        val subname = latinName.substring(0,4)
//        println(subname.uppercase())
//    }
//}
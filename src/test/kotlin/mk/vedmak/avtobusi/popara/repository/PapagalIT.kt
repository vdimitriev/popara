//package mk.vedmak.avtobusi.popara.repository
//
//import mk.vedmak.avtobusi.popara.model.Carrier
//import mk.vedmak.avtobusi.popara.model.Country
//import mk.vedmak.avtobusi.popara.model.Location
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//
//@SpringBootTest
//class PapagalIT {
//
//    @Autowired
//    private lateinit var carrierRepository: CarrierRepository
//
//    @Test
//    fun simpleTest() {
//        val mkd = Country("MKD", "Macedonia", "Македонија", "MK", "MKD")
//
//        val bt = Location("BT", "Bitola", "Битола", mkd, 1)
//        val oh = Location("OH", "Ohrid", "Охрид", mkd, 1)
//        val sk = Location("SK", "Skopje", "Скопје", mkd, 1)
//        val pp = Location("PP", "Prilep", "Prilep", mkd, 2)
//        val ve = Location("VE", "Veles", "Veles", mkd, 2)
//        val ka = Location("KA", "Kavadarci", "Kavadarci", mkd, 3)
//        val sr = Location("SR", "Strumica", "Strumica", mkd, 3)
//        val ne = Location("NE", "Negotino", "Negotino", mkd, 4)
//        val gr = Location("GR", "Gradsko", "Gradsko", mkd, 5)
//
//        val ctr = Carrier("TR", 1, bt,  mutableListOf(), 40, "Transkop")
//        val cpt = Carrier("PE", 2, pp,  mutableListOf(), 6, "Pelagonija Trans")
//        val cse = Carrier("ST", 4, sr,  mutableListOf(), 14, "Strumica Ekspres")
//        val cga = Carrier("GA", 22, oh,  mutableListOf(), 15, "Galeb")
//        val cek = Carrier("EK", 27, ka,  mutableListOf(), 16, "Ekstra 03")
//        val crt = Carrier("RU", 34, sk,  mutableListOf(), 48, "Rule Turs")
//        val cda = Carrier("DA", 36, ne,  mutableListOf(), 2, "Dajo Turs")
//
////        val re = Location("RE", "Resen", "Ресен",  mkd, 5)
////        val ks = Location("KS", "Krusevo", "Kratovo", mkd, 5)
////        val dj = Location("DJ", "Dojran", "Dojran",  mkd, 6)
////        val kk = Location("KO", "Kokino", "Kokino",  mkd, 6)
////        val sb = Location("SB", "Stobi", "Stobi",  mkd, 6)
////        val ma = Location("MA", "Mavrovo", "Mavrovo",  mkd, 6)
//    }
//}
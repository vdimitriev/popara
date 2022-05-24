package mk.vedmak.avtobusi.popara.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import mk.vedmak.avtobusi.popara.model.*
import org.bouncycastle.util.test.SimpleTest.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalTime

@SpringBootTest
class PoparaIT {

    @Autowired
    private lateinit var carrierRepository: CarrierRepository

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var stopRepository: StopRepository

    @Autowired
    private lateinit var journeyRepository: JourneyRepository

    @Autowired
    private lateinit var lineRepository: LineRepository

    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    @Autowired
    private lateinit var periodRepository: PeriodRepository

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    private lateinit var countryRepository: CountryRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteAllDataFromRepositories() = runBlocking(Dispatchers.IO) {
        deleteAll()
    }

//    @Test
//    fun insertSmallAmountOfData() {
//        val mkd = Country("MKD", "Macedonia", "Македонија", "MK", "MKD")
//        val schedule = Schedule("P1", listOf(1,2,3,4,5,6,7), listOf(), listOf(1,2,3,4,5,6,7,8,9,10,11,12),true, "Sekojdnevno", "Sekojdnevno", "Секојдневно")
//        val bitola = Location("BT", "Bitola", "Битола", mkd, 1)
//        val ohrid = Location("OH", "Ohrid", "Охрид", mkd, 1)
//        val resen = Location("RE", "Resen", "Ресен",  mkd, 5)
//        val transkop = Carrier("TR", 1, "BT",  mutableListOf(), 40, "Transkop", "Транскоп", "Transkop - Bitola", "Transkop - Bitola")
//
//        val bt1315 = Stop("SBT1315", LocalTime.of(13, 15), "BT", 1)
//        val re1405 = Stop("SRE1405", LocalTime.of(14, 5), "RE", 2)
//        val oh1458 = Stop("SOH1458", LocalTime.of(14, 58), "OH", 3)
//
//        val tra010101 = Trip("TRA01J01T01", 1, 1, 1, mutableListOf(bt1315, re1405), listOf(schedule), "TRA01J01T01")
//        val tra010201 = Trip("TRA01J02T01", 1, 2, 1, mutableListOf(bt1315, re1405, oh1458), listOf(schedule), "TRA01J02T01")
//        val tra010202 = Trip("TRA01J02T02", 1, 2, 2, mutableListOf(
//            Stop("SBT1415", LocalTime.of(14, 15), "BT", 1),
//            Stop("SRE1505", LocalTime.of(15, 5), "RE", 2),
//            Stop("SOH1558", LocalTime.of(15, 58), "OH", 3)
//        ), listOf(schedule), "TRA01J02T02")
//        val tra010301 = Trip("TRA01J03T01", 1, 3, 1, mutableListOf(re1405, oh1458), listOf(schedule), "TRA01J03T01")
//
//        val tra01 = Line("TRA01", "Bitola - Ohrid", "Битола - Охрид",1, mutableSetOf())
//
//        val tra01j01 = Journey("TRA01J01", "TRA",1, 1,"BT", "RE", mutableSetOf(tra010101))
//        val tra01j02 = Journey("TRA01J02", "TRA",1, 2,"BT", "OH", mutableSetOf(tra010201, tra010202))
//        val tra01j03 = Journey("TRA01J03", "TRA",1, 3,"RE", "OH", mutableSetOf(tra010301))
//
//        tra01.journeys?.add(tra01j01)
//        tra01.journeys?.add(tra01j02)
//        tra01.journeys?.add(tra01j03)
//
//        transkop.lines?.add(tra01)
//
//        carrierRepository.save(transkop)
//
//        println("================================================================================================")
//        carrierRepository.findAll().forEach {
//            println("bus line: $it")
//            println("================================================================================================")
//        }
//    }

//    fun insertLocationsAndCarriers() {
//        val mkd = Country("MKD", "Macedonia", "Македонија", "MK", "MKD")
//
//        val bt = Location("BT", "Bitola", "Битола", mkd, 1)
//        val oh = Location("OH", "Ohrid", "Охрид", mkd, 1)
//        val sk = Location("SK", "Skopje", "Скопје", mkd, 1)
//
//        val pp = Location("PP", "Prilep", "Prilep", mkd, 2)
//        val te = Location("TE", "Tetovo", "Tetovo", mkd, 2)
//        val ku = Location("KU", "Kumanovo", "Kumanovo", mkd, 2)
//        val ve = Location("VE", "Veles", "Veles", mkd, 2)
//        val st = Location("ST", "Stip", "Stip", mkd, 2)
//
//        val go = Location("GO", "Gostivar", "Gostivar", mkd, 3)
//        val sr = Location("SR", "Strumica", "Strumica", mkd, 3)
//        val ka = Location("KA", "Kavadarci", "Kavadarci", mkd, 3)
//
//        val ge = Location("GE", "Gevgelija", "Gevgelija", mkd, 4)
//        val ne = Location("NE", "Negotino", "Negotino", mkd, 4)
//        val ko = Location("KO", "Kocani", "Kocani", mkd, 4)
//        val ki = Location("KI", "Kicevo", "Kicevo", mkd, 4)
//        val ra = Location("RA", "Radovis", "Radovis", mkd, 4)
//        val su = Location("ST", "Struga", "Struga", mkd, 4)
//
//        val be = Location("BE", "Berovo", "Berovo", mkd, 5)
//        val de = Location("DE", "Delcevo", "Delcevo", mkd, 5)
//        val db = Location("DB", "Debar", "Debar", mkd, 5)
//        val ps = Location("PS", "Probistip", "Probistip", mkd, 5)
//        val sn = Location("SN", "Sveti Nikole", "Sveti Nikole", mkd, 5)
//        val kr = Location("KR", "Kratovo", "Krusevo", mkd, 5)
//        val ks = Location("KS", "Krusevo", "Kratovo", mkd, 5)
//        val va = Location("VA", "Valandovo", "Valandovo",  mkd, 5)
//        val vi = Location("VI", "Vinica", "Vinica",  mkd, 5)
//        val re = Location("RE", "Resen", "Ресен",  mkd, 5)
//
//        val bo = Location("BO", "Bogdanci", "Bogdanci",  mkd, 6)
//        val dh = Location("DH", "Demir Hisar", "Demir Hisar",  mkd, 6)
//        val dk = Location("DK", "Demir Kapija", "Demir Kapija",  mkd, 6)
//        val dj = Location("DJ", "Dojran", "Dojran",  mkd, 6)
//        val sd = Location("SD", "Star Dojran", "Star Dojran",  mkd, 6)
//        val nd = Location("ND", "Nov Dojran", "Nov Dojran",  mkd, 6)
//        val gr = Location("GR", "Gradsko", "Gradsko",  mkd, 6)
//        val pe = Location("PE", "Pehcevo", "Pehcevo",  mkd, 6)
//        val kp = Location("KP", "Kriva Palanka", "Kriva Palanka",  mkd, 6)
//        val ma = Location("MA", "Mavrovo", "Mavrovo",  mkd, 6)
//        val mb = Location("MB", "Makedonski Brod", "Makedonski Brod",  mkd, 6)
//        val mk = Location("MK", "Makedonska Kamenica", "Makedonska Kamenica",  mkd, 6)
//        val vv = Location("VV", "Vevcani", "Vevcani",  mkd, 6)
//        val zl = Location("ZL", "Zletovo", "Zletovo",  mkd, 6)
//        val kk = Location("KO", "Kokino", "Kokino",  mkd, 6)
//        val sb = Location("SB", "Stobi", "Stobi",  mkd, 6)
//
//        val ctr = Carrier("TR", 1, bt,  mutableListOf(), 40, "Transkop")
//        val cpt = Carrier("PT", 2, pp,  mutableListOf(), 6, "Pelagonija Trans")
//        val caa = Carrier("AA", 3, ko,  mutableListOf(), 27, "Avto Atom")
//        val cse = Carrier("SE", 4, sr,  mutableListOf(), 14, "Strumica Ekspres")
//        val ctb = Carrier("TB", 5, ge,  mutableListOf(), 2, "Trans Balkan")
//        val cpr = Carrier("PR", 6, kp,  mutableListOf(), 7, "ATP Proleter")
//        val cgs = Carrier("GS", 7, be,  mutableListOf(), 1, "Gjoko Gligor Sontevski")
//        val cut = Carrier("UT", 8, pp,  mutableListOf(), 3, "Uni Tours")
//        val cet = Carrier("ET", 9, te,  mutableListOf(), 1, "Eko Tours")
//        val cit = Carrier("IT", 10, te,  mutableListOf(), 1, "Ido Tours")
//        val crk = Carrier("RK", 11, sn,  mutableListOf(), 3, "Reis-K")
//        val cmt = Carrier("MT", 12, ps,  mutableListOf(), 1, "Marjan Turs")
//        val cbp = Carrier("BP", 13, sk,  mutableListOf(), 4, "Biser Prom")
//        val cak = Carrier("AK", 14, pe,  mutableListOf(), 5, "Andon Kompani")
//        val chi = Carrier("HI", 15, de,  mutableListOf(), 16, "Hisari - GD")
//        val cea = Carrier("EA", 16, su,  mutableListOf(), 8, "Euro Linia")
//        val csp = Carrier("SP", 17, vv,  mutableListOf(), 2, "Sprija")
//        val cbe = Carrier("BE", 18, be,  mutableListOf(), 3, "Berovo Trans")
//        val cin = Carrier("IN", 19, sk,  mutableListOf(), 10, "Inter Tours")
//        val cbt = Carrier("BT", 20, ma,  mutableListOf(), 4, "Balkan Tours")
//        val cva = Carrier("VA", 21, su,  mutableListOf(), 5, "Vakcare")
//        val cga = Carrier("GA", 22, oh,  mutableListOf(), 15, "Galeb")
//        val ckk = Carrier("KK", 23, oh,  mutableListOf(), 7, "Klasik Kompani")
//        val clt = Carrier("LT", 24, pp,  mutableListOf(), 2, "Lasta Turs s.Debreste")
//        val cst = Carrier("ST", 25, ku,  mutableListOf(), 1, "Stuz")
//        val cra = Carrier("RA", 26, pp,  mutableListOf(), 4, "Rama Turs s.Debreste")
//        val cek = Carrier("EK", 27, ka,  mutableListOf(), 16, "Ekstra 03")
//        val cts = Carrier("TS", 28, va,  mutableListOf(), 4, "T-Stefani")
//        val cdt = Carrier("DT", 29, go,  mutableListOf(), 2, "Durmo Turs")
//        val cnt = Carrier("NT", 30, go,  mutableListOf(), 1, "Nasir Turizam")
//        val cdl = Carrier("DL", 31, ku,  mutableListOf(), 3, "Deluks Turs")
//        val com = Carrier("OM", 32, oh,  mutableListOf(), 2, "Omega 2006")
//        val cma = Carrier("MA", 33, su,  mutableListOf(), 2, "Martinoski")
//        val crt = Carrier("RT", 34, sk,  mutableListOf(), 48, "Rule Turs")
//        val cft = Carrier("FT", 35, ku,  mutableListOf(), 2, "Fikret Turizam")
//        val cda = Carrier("DA", 36, ne,  mutableListOf(), 2, "Dajo Turs")
//    }

//    @Test
//    fun insertCompleteLineWithCarrier() {
//        val mkd = Country("MKD", "Macedonia", "Македонија", "MKD", "MK", "MKD")
//        val schedule = Schedule("P1", listOf(1,2,3,4,5,6,7), "01/01-31/12", listOf(1,2,3,4,5,6,7,8,9,10,11,12),"Sekojdnevno", "Sekojdnevno", "Секојдневно")
//
//        val bitola = Location("BT", "Bitola", "Битола", mkd, 1)
//        val ohrid = Location("OH", "Ohrid", "Охрид", mkd, 1)
//        val skopje = Location("SK", "Skopje", "Скопје", mkd, 1)
//
//        val veles = Location("VE", "Veles", "Veles", mkd, 3)
//        val kavadarci = Location("KA", "Kavadarci", "Kavadarci", mkd, 3)
//        val prilep = Location("PP", "Prilep", "Prilep", mkd, 2)
//        val strumica = Location("SR", "Strumica", "Strumica", mkd, 3)
//        val stip = Location("ST", "Stip", "Stip", mkd, 3)
//
//        val negotino = Location("NE", "Negotino", "Negotino", mkd, 4)
//        val kocani = Location("KO", "Kocani", "Kocani", mkd, 4)
//        val struga = Location("ST", "Struga", "Struga", mkd, 4)
//
//        val valandovo = Location("VA", "Valandovo", "Valandovo",  mkd, 5)
//        val vinica = Location("VI", "Vinica", "Vinica",  mkd, 5)
//        val resen = Location("RE", "Resen", "Ресен",  mkd, 5)
//
//        val dk = Location("DK", "Demir Kapija", "Demir Kapija",  mkd, 6)
//        val gradsko = Location("GR", "Gradsko", "Gradsko",  mkd, 6)
//
//        val transkop = Carrier("TRA", bitola,  mutableListOf(), "Transkop", "Транскоп", "Transkop - Bitola", "Transkop - Bitola")
//        val pelagonija = Carrier("PEL", prilep,  mutableListOf(), "Pelagonija Trans", "Pelagonija Trans", "Pelagonija Trans A.D. - Prilep", "Pelagonija Trans A.D. - Prilep")
//        val avtoAtom = Carrier("AVT", kocani,  mutableListOf(), "Avto Atom", "Avto Atom", "Avto Atom Kocani", "Avto Atom Kocani")
//        val dajoTours = Carrier("DAJ", negotino,  mutableListOf(), "Dajo Turs", "Dajo Turs", "Dajo Turs DOOEL - Negotino", "Dajo Turs DOOEL - Negotino")
//
//        val bt1315 = Stop("TRA010101", LocalTime.of(13, 15), bitola, 1)
//        val re1405 = Stop("TRA010102", LocalTime.of(14, 5), resen, 2)
//        val oh1458 = Stop("TRA010103", LocalTime.of(14, 58), ohrid, 3)
//
//        val bt0630 = Stop("TRA020101", LocalTime.of(6, 30), bitola, 1)
//        val re0720 = Stop("TRA020102", LocalTime.of(7, 20), resen, 2)
//        val oh0858 = Stop("TRA020103", LocalTime.of(8, 58), ohrid, 3)
//        val st0918 = Stop("TRA020104", LocalTime.of(9, 18), struga, 4)
//
//        val pp0600 = Stop("PEL020101", LocalTime.of(6, 0), prilep, 1)
//        val ka0705 = Stop("PEL020102", LocalTime.of(7, 5), kavadarci, 2)
//        val ne0720 = Stop("PEL020103", LocalTime.of(7, 20), negotino, 3)
//        val dk0743 = Stop("PEL020104", LocalTime.of(7, 43), dk, 4)
//        val va0838 = Stop("PEL020105", LocalTime.of(8, 38), valandovo, 5)
//        val sr0908 = Stop("PEL020106", LocalTime.of(9, 8), strumica, 6)
//
//        val ko0630 = Stop("AVT050101", LocalTime.of(6, 30), kocani, 1)
//        val st0717 = Stop("AVT050102", LocalTime.of(7, 17), stip, 2)
//        val ve0817 = Stop("AVT050103", LocalTime.of(8, 17), veles, 3)
//        val gr0830 = Stop("AVT050104", LocalTime.of(8, 30), gradsko, 4)
//        val pp0945 = Stop("AVT050105", LocalTime.of(9, 45), prilep, 5)
//        val bt1035 = Stop("AVT050106", LocalTime.of(10, 35), bitola, 6)
//        val re1125 = Stop("AVT050107", LocalTime.of(11, 25), resen, 7)
//        val oh1215 = Stop("AVT050108", LocalTime.of(12, 15), ohrid, 8)
//
//        val tra0101 = Trip("TRA01T01", 1, 1, listOf(bt1315, re1405, oh1458), "TRA0101")
//        val tra0201 = Trip("TRA02T01", 1, 2, listOf(bt0630, re0720, oh0858, st0918), "TRA0201")
//        val tra3901 = Trip("TRA39T01", 1, 39, listOf(
//            Stop("TRA390101", LocalTime.of(13, 0), bitola, 1),
//            Stop("TRA390102", LocalTime.of(14, 0), prilep, 2),
//            Stop("TRA390103", LocalTime.of(15, 15), gradsko, 3),
//            Stop("TRA390104", LocalTime.of(15, 40), veles, 4),
//            Stop("TRA390105", LocalTime.of(16, 40), skopje, 5),
//        ), "TRA3901")
//        val pel0201 = Trip("PEL0201", 1, 2, listOf(pp0600, ka0705, ne0720, dk0743, va0838, sr0908), "PEL0201")
//        val avt0101 = Trip("AVT0101", 1, 1, listOf(
//            Stop("AVT010101", LocalTime.of(6, 25), kocani, 1),
//            Stop("AVT010102", LocalTime.of(6, 55), stip, 2)
//        ), "AVT0101")
//        val avt0102 = Trip("AVT0102", 2, 1, listOf(
//            Stop("AVT010201", LocalTime.of(14, 40), kocani, 1),
//            Stop("AVT010202", LocalTime.of(15, 14), stip, 2)
//        ), "AVT0102")
//        val avt0501 = Trip("AVT0501", 1, 5, listOf(ko0630, st0717, ve0817, gr0830, pp0945, bt1035, re1125, oh1215), "AVT0501")
//        val daj0101 = Trip("DAJ0101", 1, 1, listOf(
//            Stop("DAJ010101", LocalTime.of(6, 35), negotino, 1),
//            Stop("DAJ010102", LocalTime.of(7, 52), skopje, 2)
//        ), "DAJ0101")
//        val daj0102 = Trip("DAJ0102", 2, 1, listOf(
//            Stop("DAJ010201", LocalTime.of(18, 15), negotino, 1),
//            Stop("DAJ010202", LocalTime.of(19, 32), skopje, 2)
//        ), "DAJ0102")
//
//        val tra01j01 = Journey("TRA01J01", bitola, resen, listOf(tra0101))
//        val tra01j02 = Journey("TRA01J02", resen, ohrid, listOf(tra0101))
//        val tra01j03 = Journey("TRA01J03", bitola, ohrid, listOf(tra0101))
//
//        val jTra02 = Journey("TRA02", bitola, struga, listOf(tra0201))
//        val jTra39 = Journey("TRA39", bitola, skopje, listOf(tra3901))
//        val jPel02 = Journey("PEL02", prilep, strumica, listOf(pel0201))
//        val jAvt01 = Journey("AVT01", kocani, stip, listOf(avt0101, avt0102))
//        val jAvt05 = Journey("AVT05", kocani, ohrid, listOf(avt0501))
//        val jDaj01 = Journey("DAJ01", negotino, skopje, listOf(daj0101, daj0102))
//
//        val tra01 = Line("TRA01", "Bitola - Ohrid", "Битола - Охрид",1, tra01j01, schedule)
//        val tra02 = Line("TRA02", "Bitola - Struga", "Битола - Struga",2, jTra02, schedule)
//        val tra39 = Line("TRA39", "Bitola - Skopje", "Битола - Skopje",39, jTra39, schedule)
//
//        val pel02 = Line("PEL02", "Prilep - Strumica", "Prilep - Strumica", 2, jPel02, schedule)
//        val avt01 = Line("AVT01", "Kocani - Stip", "Kocani - Stip", 1, jAvt01, schedule)
//        val avt05 = Line("AVT05", "Kocani - Ohrid", "Kocani - Ohrid", 5, jAvt05, schedule)
//
//        val daj01 = Line("DAJ01", "Negotino - Skopje", "Negotino - Skopje", 1, jDaj01, schedule)
//
//        transkop.lines.add(tra01)
//        transkop.lines.add(tra02)
//        transkop.lines.add(tra39)
//
//        pelagonija.lines.add(pel02)
//
//        avtoAtom.lines.add(avt01)
//        avtoAtom.lines.add(avt05)
//
//        dajoTours.lines.add(daj01)
//
//        carrierRepository.save(transkop)
//        carrierRepository.save(pelagonija)
//        carrierRepository.save(avtoAtom)
//        carrierRepository.save(dajoTours)
//
////        carrierRepository.findAll().forEach {
////            println("================================================================================================")
////            println("carrier: $it")
////            println("================================================================================================")
////        }
//
//        println("================================================================================================")
//        lineRepository.findAll().forEach {
//            println("bus line: $it")
//            println("================================================================================================")
//        }
//        //deleteAll()
//    }

    private suspend fun deleteAll() {
        periodRepository.deleteAll().block()
        scheduleRepository.deleteAll().block()
        countryRepository.deleteAll().block()
        stopRepository.deleteAll().block()
        locationRepository.deleteAll().block()
        tripRepository.deleteAll().block()
        journeyRepository.deleteAll().block()
        lineRepository.deleteAll().block()
        carrierRepository.deleteAll().block()
    }

}
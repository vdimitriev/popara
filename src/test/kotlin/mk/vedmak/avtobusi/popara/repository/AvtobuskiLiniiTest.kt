package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.*
import mk.vedmak.avtobusi.popara.util.OperationsUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.time.LocalTime
import java.time.temporal.TemporalAmount

@SpringBootTest
class AvtobuskiLiniiTest() {

    @Autowired
    private lateinit var operationsUtil: OperationsUtil

    @Autowired
    private lateinit var carrierRepository: CarrierRepository

    @Test
    fun readXlsFileAndPersistBusLines() {
        val pathname = "files/avtobuski-linii-nice.xls"
        val startTime = LocalTime.now()
        println("$startTime: start reading file $pathname")
        var rows = ArrayList<Row>()
        val workBook = WorkbookFactory.create(File(pathname))
        workBook.use { wb ->
            val sheet = wb.getSheetAt(0)
            val firstRowNum = sheet.firstRowNum
            val lastRowNum = sheet.lastRowNum
            var firstCell:Cell? = null
            for(rowCount in firstRowNum until lastRowNum) {
                val row = sheet.getRow(rowCount)
                val cell = row.getCell(0)
                if(firstCell == null && cell != null && cell.toString().isNotBlank()) {
                    firstCell = cell
                    rows.add(row)
                } else if((cell == null || cell.toString().isBlank()) && firstCell != null) {
                    rows.add(row)
                } else if(cell != null && cell.toString().isNotBlank() && firstCell != null) {
                    rows.add(row)
                    val firstCellValue = firstCell.toString().trim()
                    val carrierName = createCarrierName(firstCellValue)
                    val firstCellValueLatin = operationsUtil.createLatinName(firstCellValue)
                    val (location, nameOfCarrier) = createLocation(firstCellValue)
                    val (locationLatin, nameOfCarrierLatin) = createLocation(firstCellValueLatin)
                    var carrier = Carrier(carrierName, 0, location, mutableListOf(), 0, nameOfCarrierLatin, nameOfCarrier, locationLatin, firstCellValueLatin, firstCellValueLatin, firstCellValue)
                    firstCell = null
                    extractAllLinesForCarrier(rows, carrier)
                    rows = ArrayList()
                    //printCarrier(carrier)
                    saveCarrier(carrier)
                }
            }
        }
        val finishTime = LocalTime.now()
        println("$finishTime: finish reading file $pathname")
        println("Time spent is ${finishTime.minusNanos(startTime.toNanoOfDay())}")
        assertTrue(true)
    }

    private fun createLocation(firstCellValue: String): Pair<String?,String?> {
        val values = firstCellValue.split(" - ")
        var location = firstCellValue.trim()
        var nameOfCarrier = firstCellValue.trim()
        if(values.size < 2) return Pair(location, nameOfCarrier)
        nameOfCarrier = values[0].trimStart().trimEnd().trim()
        location = values[1].trimStart().trimEnd().trim()
        return Pair(location, nameOfCarrier)
    }

    private fun saveCarrier(carrier: Carrier) {
        println("========================================")
        val startTime = LocalTime.now()
        println("$startTime: saving carrier = ${carrier.name}")
        carrierRepository.save(carrier)
        val finishTime = LocalTime.now()
        println("$finishTime: saved carrier = ${carrier.name}")
        println("Time spent is ${finishTime.minusNanos(startTime.toNanoOfDay())}")
        println("----------------------------------------")
    }

    private fun printCarriersInfo(carriers: ArrayList<Carrier>) {
        println("carries count = " + carriers.size)
    }

    private fun printCarrier(carrier: Carrier) {
        println("========================================")
        println(carrier.name)
        println("----------------------------------------")
        println("${carrier.name} lines count = ${carrier.lines.size}")
//        carrier.lines.forEach {
//            println("----------------------------------------")
//            println("${carrier.name} - ${it.name} journeys count = ${it.journeys.size}")
////            if(it.name == "TRA006") {
////                it.journeys.forEach { j -> println("${j.name} trip size = ${j.trips.size}") }
////            }
//            println("----------------------------------------")
//        }
//
//        println("----------------------------------------")
//        carrier.lines.forEach {line ->
//            line.journeys.forEach {
//                println(it)
//            }
//        }
//        println("----------------------------------------")
        carrier.lines.forEach {line ->
            line.journeys.forEach {journey ->
                println(journey)
            }
        }
        println("----------------------------------------")
        carrier.lines.forEach {line ->
            line.journeys.forEach {journey ->
                journey.trips.forEach {trip ->
                    println(trip)
                }
            }
        }
        println("========================================")
//        carrier.lines.forEach {line ->
//            line.journeys.forEach {journey ->
//                journey.trips.forEach {trip ->
//                    trip.stops.forEach { stop ->
//                        println(stop)
//                    }
//                }
//            }
//        }
//        println("========================================")
    }



    private fun extractAllLinesForCarrier(rows: ArrayList<Row>, carrier: Carrier) {
        var journeys = ArrayList<Row>()
        var firstCell:Cell? = null
        rows.forEach { row ->
            val cell = row.getCell(1)
            if (firstCell == null && cell != null && cell.toString().isNotBlank()) {
                firstCell = cell
                journeys.add(row)
            } else if ((cell == null || cell.toString().isBlank()) && firstCell != null) {
                journeys.add(row)
            } else if (cell != null && cell.toString().isNotBlank() && firstCell != null) {
                journeys.add(row)
                firstCell = null
                extractAllJourneysForLine(journeys, carrier)
                journeys = ArrayList()
            }
        }
    }

    private fun extractAllJourneysForLine(rows: ArrayList<Row>, carrier: Carrier) {
        val lineNumber = rows[0].getCell(2).numericCellValue.toInt()
        val lineNameCell = rows[0].getCell(3).stringCellValue
        val line = Line(createLineName(carrier.name, lineNumber),operationsUtil.createLatinName(lineNameCell), lineNameCell, lineNumber, mutableSetOf())
        println("extract line ${line.name}")

        extractOneWay(rows, carrier.name, line)
        extractReturn(rows, carrier.name, line)
        println("extracted line ${line.name}")
        carrier.lines.add(line)
    }


    private fun extractOneWay(rows: ArrayList<Row>, carrierName: String, line: Line) {
        val rowsSize = rows.size
        val lineNumber = line.lineNumber
        val journeyCountOneWay = getStringFromCell(rows[0].getCell(4))
        var journeyCountCellOneWay = 0
        val journeyCountCellOneWayList = journeyCountOneWay.split("+")
        var gotoOneWay = 0
        if(journeyCountCellOneWayList.size == 1) {
            journeyCountCellOneWay = Integer.valueOf(journeyCountCellOneWayList[0])
            gotoOneWay = journeyCountCellOneWay
        } else {
            val one = journeyCountCellOneWayList[0].toInt()
            val two = journeyCountCellOneWayList[1].toInt()
            gotoOneWay = one - (10 * two)
            if(gotoOneWay > 10) {
                gotoOneWay = 10
            }
        }
        val journeyCountCellOneWayMax = gotoOneWay
        val journeys = mutableSetOf<Journey>()
        var journeyNumber = 0
        val journeyPrefix = "JD"
        for(jco in 0 until journeyCountCellOneWayMax) {
            var tripNumber = 1 + jco
            for (i in 0 until rowsSize - 1) {
                val trip = makeATrip(carrierName, lineNumber, journeyNumber, tripNumber, journeyPrefix)
                val departureLocationName = rows[i].getCell(17).stringCellValue
                val departureTime = rows[i].getCell(6 + jco).toString()
                val scheduleCell = rows[i].getCell(28).toString()
                val scheduleDescriptionCell = rows[i].getCell(30).toString()
                val firstStop = createBusStop(departureLocationName, departureTime)
                val distanceDeparture = getIntFromCell(rows[i].getCell(16))
                val stops = mutableListOf<Stop>()
                if(firstStop != null) stops.add(firstStop)
                var lastStop = firstStop
                for (j in (i + 1) until rowsSize) {
                    val arrivalLocationName = rows[j].getCell(17).stringCellValue
                    val arrivalTime = rows[j].getCell(6 + jco).toString()
                    val distanceArrival = getIntFromCell(rows[j].getCell(16))
                    val stop = createBusStop(arrivalLocationName, arrivalTime)
                    if(stop != null) stops.add(stop)
                    trip.stops = stops
                    lastStop = stop

                    if(firstStop != null && lastStop != null) {
                        val journey = createJourney(
                            carrierName,
                            lineNumber,
                            firstStop,
                            lastStop,
                            journeys,
                            journeyNumber,
                            journeyPrefix,
                            distanceArrival - distanceDeparture,
                        )
                        journeyNumber = journey.journeyNumber ?: 0

                        val schedules =
                            createSchedulesForTrip(scheduleCell, scheduleDescriptionCell, tripNumber, journey.name)
                        journey.trips.add(
                            makenewtrip(
                                trip,
                                carrierName,
                                lineNumber,
                                journeyNumber,
                                tripNumber,
                                journeyPrefix,
                                schedules,
                                firstStop.time,
                                lastStop.time
                            )
                        )
                        journey.schedules =
                            createSchedulesForJourney(scheduleCell, scheduleDescriptionCell, journey.name)
                        journeys.add(journey)
                        line.journeys?.add(journey)
                    }
                }
            }
        }
    }

    private fun extractReturn(rows: ArrayList<Row>, carrierName: String, line: Line) {
        val rowsSize = rows.size
        val lineNumber = line.lineNumber
        val journeyCountReturn = getStringFromCell(rows[0].getCell(5))
        var journeyCountCellReturn = 0
        val journeyCountCellReturnList = journeyCountReturn.split("+")
        var gotoReturn = 0
        if(journeyCountCellReturnList.size == 1) {
            journeyCountCellReturn = Integer.valueOf(journeyCountCellReturnList[0])
            gotoReturn = journeyCountCellReturn
        } else {
            val one = journeyCountCellReturnList[0].toInt()
            val two = journeyCountCellReturnList[1].toInt()
            gotoReturn = one - (10 * two)
            if(gotoReturn > 10) {
                gotoReturn = 10
            }
        }
        val journeyCountCellReturnMax = gotoReturn
        val journeys = mutableSetOf<Journey>()
        val rowsSizeMinusOne = rowsSize - 1
        var journeyNumber = 0
        val journeyPrefix = "JR"
        for(jcr in 0 until journeyCountCellReturnMax) {
            var tripNumber = 1 + jcr
            for (k in rowsSizeMinusOne downTo 1) {
                val trip = makeATrip(carrierName, lineNumber, journeyNumber, tripNumber, journeyPrefix)
                val departureLocationName = rows[k].getCell(17).stringCellValue
                val departureTime = rows[k].getCell(18 + jcr).toString()
                val scheduleCell = rows[k].getCell(29).toString()
                val scheduleDescriptionCell = rows[k].getCell(30).toString()
                val distanceDeparture = getIntFromCell(rows[k].getCell(16))
                val firstStop = createBusStop(departureLocationName, departureTime)
                val stops = mutableListOf<Stop>()
                if(firstStop != null) stops.add(firstStop)
                var lastStop = firstStop
                for (l in (k - 1) downTo 0) {
                    val arrivalLocationName = rows[l].getCell(17).stringCellValue
                    val arrivalTime = rows[l].getCell(18 + jcr).toString()
                    val distanceArrival = getIntFromCell(rows[l].getCell(16))
                    val stop = createBusStop(arrivalLocationName, arrivalTime)
                    if(stop != null) stops.add(stop)
                    trip.stops = stops
                    lastStop = stop
                    if(firstStop != null && lastStop != null) {
                        val journey = createJourney(
                            carrierName,
                            lineNumber,
                            firstStop,
                            lastStop,
                            journeys,
                            journeyNumber,
                            journeyPrefix,
                            distanceDeparture - distanceArrival,
                        )
                        val schedules =
                            createSchedulesForTrip(scheduleCell, scheduleDescriptionCell, tripNumber, journey.name)
                        journeyNumber = journey.journeyNumber ?: 0
                        journey.trips.add(
                            makenewtrip(
                                trip,
                                carrierName,
                                lineNumber,
                                journeyNumber,
                                tripNumber,
                                journeyPrefix,
                                schedules,
                                firstStop.time,
                                lastStop.time
                            )
                        )
                        journey.schedules =
                            createSchedulesForJourney(scheduleCell, scheduleDescriptionCell, journey.name)
                        journeys.add(journey)
                        line.journeys?.add(journey)
                    }
                }
            }
        }
    }
    private fun createJourney(
        carrier: String,
        lineNumber: Int,
        firstStop: Stop,
        lastStop: Stop,
        journeys: MutableSet<Journey>,
        journeyNumber: Int,
        journeyPrefix: String,
        distance: Int,
    ):Journey {
        val jn = journeyNumber + 1
        val journey = Journey(createJourneyName(carrier, lineNumber, jn, journeyPrefix), carrier, lineNumber, jn, firstStop.location, lastStop.location)
        journey.distance = distance
        journey.fullNameCyrillic = firstStop.location.plus(" - ").plus(lastStop)
        journey.fullNameLatin = operationsUtil.createLatinName(journey.fullNameCyrillic)
        return journeys.singleOrNull { it == journey } ?: journey
    }

    private fun calculateTravelTime(departureTime: LocalTime?, arrivalTime: LocalTime?): String {
        if(arrivalTime == null) return ""
        if(departureTime == null) return ""
        val rlt = arrivalTime.minusHours(departureTime.hour.toLong()).minusMinutes(departureTime.minute.toLong())
        return rlt.toString()
    }

    private fun makenewtrip(
        trip: Trip,
        carrier: String,
        lineNumber: Int,
        journeyNumber: Int,
        tripNumber: Int,
        journeyPrefix: String,
        schedules:List<Schedule>,
        departureTime: LocalTime?,
        arrivalTime: LocalTime?,
        ): Trip {

        val newstops = ArrayList<Stop>()
        trip.stops.forEach { newstops.add(it) }
        val travelTime = calculateTravelTime(departureTime, arrivalTime)
        val tripName = makeTripName(carrier, lineNumber, journeyNumber, tripNumber, journeyPrefix)
        return Trip(tripName, lineNumber, journeyNumber, tripNumber, newstops, schedules, null, travelTime, departureTime, arrivalTime)

    }

    private fun createJourneyName(carrier: String, lineNumber: Int, journeyNumber: Int, journeyPrefix: String): String {
        var lns = lineNumber.toString()
        if(lineNumber / 100 == 0) {
            lns = "0$lns"
        }

        if(lineNumber / 10 == 0) {
            lns = "0$lns"
        }

        var jn = "$journeyPrefix"
        if(journeyNumber / 10000 == 0) {
            jn += "0"
        }

        if(journeyNumber / 1000 == 0) {
            jn += "0"
        }

        if(journeyNumber / 100 == 0) {
            jn += "0"
        }

        if(journeyNumber / 10 == 0) {
            jn += "0"
        }
        return carrier + lns + jn + journeyNumber
    }

    private fun makeATrip(carrier: String, lineNumber: Int, journeyNumber:Int, tripNumber: Int, journeyPrefix: String): Trip {
        val trip = Trip(makeTripName(carrier, lineNumber, journeyNumber, tripNumber, journeyPrefix), lineNumber, journeyNumber, tripNumber)
        return trip
    }

    private fun makeTripName(carrier: String, lineNumber: Int, journeyNumber:Int, tripNumber: Int, journeyPrefix: String): String {
        var lns = lineNumber.toString()
        if(lineNumber / 100 == 0) {
            lns = "0$lns"
        }

        if(lineNumber / 10 == 0) {
            lns = "0$lns"
        }

        var jn = "$journeyPrefix"
        if(journeyNumber / 10000 == 0) {
            jn += "0"
        }

        if(journeyNumber / 1000 == 0) {
            jn += "0"
        }

        if(journeyNumber / 100 == 0) {
            jn += "0"
        }

        if(journeyNumber / 10 == 0) {
            jn += "0"
        }

        var tn = tripNumber.toString()
        if(tripNumber / 10 == 0) {
            tn = "0$tn"
        }
        return carrier + lns + jn + journeyNumber + "T"+tn
    }

    private fun createBusStop(locationName: String, stopTime: String): Stop? {
        val busStopTime = createBusStopTime(stopTime) ?: return null
        //println("Bus stop time = $busStopTime")
        val busStopName = createBusStopName(locationName.trim(), busStopTime)
        val stop = Stop(busStopName, busStopTime, locationName)

        return stop
    }

    private fun createBusStopName(locationName: String, busStopTime: LocalTime): String {
        var hour = busStopTime.hour.toString()
        if(hour.length == 1) hour = "0"+hour

        var minute = busStopTime.minute.toString()
        if(minute.length == 1) minute = "0"+minute

        val name = makeLocationName(locationName.trim()) + hour + minute
        return name
    }

    private fun makeLocationName(locationName: String): String {
        return when(locationName) {
            "Берово" -> "BER"
            "Битола" -> "BIT"
            "Богданци" -> "BOG"
            "Брод" -> "MBR"
            "Валандово" -> "VAL"
            "Вевчани" -> "VEV"
            "Велес" -> "VEL"
            "Виница" -> "VIN"
            "Гевгелија" -> "GEV"
            "Гостивар" -> "GOS"
            "Градско" -> "GRA"
            "Дебар" -> "DEB"
            "Делчево" -> "DEL"
            "Дојран" -> "DОЈ"
            "Демир Капија" -> "DKA"
            "Д Капија" -> "DKA"
            "Демир Хисар" -> "DHI"
            "Д Хисар" -> "DHI"
            "Злетово" -> "ZLE"
            "Кавадарци" -> "KAV"
            "Каменица" -> "MKA"
            "Кичево" -> "KIC"
            "Кочани" -> "KOC"
            "Кратово" -> "KRA"
            "Крива Паланка" -> "KPA"
            "Кр Паланка" -> "KPA"
            "К Паланка" -> "KPA"
            "Крушево" -> "KRU"
            "Куманово" -> "KUM"
            "Куманово (Болница)" -> "KUB"
            "Куманово (Центар)" -> "KUC"
            "Македонски Брод" -> "MBR"
            "Мак Брод" -> "MBR"
            "М Брод" -> "MBR"
            "Македонска Каменица" -> "MKA"
            "Мак Каменица" -> "MKA"
            "М Каменица" -> "MKA"
            "Неготино" -> "NEG"
            "Нов Дојран" -> "DOJ"
            "Н Дојран" -> "DOJ"
            "Маврово" -> "MAV"
            "Маврови анови" -> "MAV"
            "Маврово и Ростуша" -> "MAV"
            "Охрид" -> "OHR"
            "Паланка" -> "KPA"
            "Пехчево" -> "PEH"
            "Пештани" -> "PES"
            "Претор" -> "PRE"
            "Прилеп" -> "PRI"
            "Пробиштип" -> "PRO"
            "Радовиш" -> "RAD"
            "Раскрсница Штип - Кочани" -> "RSK"
            "Раскрсница Радовиш - Штип" -> "RRS"
            "Ресен" -> "RES"
            "Росоман" -> "ROS"
            "Свети Николе" -> "SVN"
            "Св Николе" -> "SVN"
            "С Николе" -> "SVN"
            "Скопје" -> "SKO"
            "Скопје (Алкалоид)" -> "SKA"
            "Скопје (Ѓорче Петров - СП Маркет)" -> "SGP"
            "Скопје (Соборен Храм)" -> "SSH"
            "Скопје (Континентал)" -> "SKT"
            "Стар Дојран" -> "DOJ"
            "Ст Дојран" -> "DOJ"
            "С Дојран" -> "DOJ"
            "Струга" -> "STU"
            "Струмица" -> "STR"
            "Тетово" -> "TET"
            "Штип" -> "STI"
            else -> makeShortLocationName(locationName)
        }
    }

    private fun makeShortLocationName(locationName: String): String {
        try {
            val latinName = operationsUtil.createLatinName(locationName)
            if(latinName.length == 5) {
                return latinName.substring(0, 5).uppercase()
            }
            return latinName.substring(0, 4).uppercase()
        }catch(e: Exception) {
            println(e.message)
        }
        return locationName.uppercase()
    }

    private fun createBusStopTime(stopTime: String): LocalTime? {
        try {
            val st = stopTime.split(".")
            //println("stop time split = ${st[0]} - ${st[1]}")
            var minute = 0
            if (st.size > 1) {
                minute = createMinute(st[1])
            }
            val lt = LocalTime.of(st[0].toInt(), minute)
            return lt
        }catch (nfe: NumberFormatException) {
            return null
        }catch (e: Exception) {
            return null
        }
    }

    private fun createMinute(minute: String): Int {
        if(minute.length == 1) return minute.toInt() * 10
        else return minute.toInt()
    }

    private fun getStringFromCell(cell: Cell?): String {
        return if(cell == null) ""
        else {
            try {
                cell.numericCellValue.toInt().toString()
            } catch(e: NumberFormatException) {
                cell.stringCellValue
            } catch(e: IllegalStateException) {
                cell.stringCellValue
            }
        }
    }

    private fun getIntFromCell(cell: Cell?): Int {
        return if(cell == null) 0
        else {
            try {
                cell.numericCellValue.toInt()
            } catch(e: IllegalStateException) {
                0
            }
        }
    }

    private fun createSchedulesForTrip(scheduleCell: String, scheduleDescriptionCell: String, tripNumber: Int, journeyName: String): List<Schedule> {
        return when(scheduleCell) {
            "S+1,2,3,4,5+01/01-31/12;" -> listOf(Schedule("SCH01", listOf(1,2,3,4,5), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), false, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5+01/09-31/05;" -> listOf(Schedule("SCH02", listOf(1,2,3,4,5), listOf(Period("PERIOD-0109-3105", 1, 9, 31, 5)), listOf(1,2,3,4,5,9,10,11,12), false, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6+01/01-31/12;" -> listOf(Schedule("SCH03", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), false, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/01-31/12;" -> listOf(Schedule("SCH04", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/04-30/09;" -> listOf(Schedule("SCH05", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0104-3009", 1, 4, 30, 9)), listOf(4,5,6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/06-30/09;" -> listOf(Schedule("SCH06", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0106-3009", 1, 6, 30, 9)), listOf(6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/06-31/08;" -> listOf(Schedule("SCH07", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0106-3108", 1, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/07-01/09;" -> listOf(Schedule("SCH08", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0107-0109", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/07-30/08;" -> listOf(Schedule("SCH09", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0107-3008", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/07-31/08;" -> listOf(Schedule("SCH10", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0107-3108", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/09-30/06;" -> listOf(Schedule("SCH11", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0109-3006", 1, 9, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/09-31/05;" -> listOf(Schedule("SCH12", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0109-3105", 1, 9, 31, 5)), listOf(1,2,3,4,5,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+15/06-15/09;" -> listOf(Schedule("SCH13", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-1506-1509", 15, 6, 15, 9)), listOf(6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+15/06-31/08;" -> listOf(Schedule("SCH14", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-1506-3108", 15, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+20/06-31/08;" -> listOf(Schedule("SCH15", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-2006-3108", 20, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+26/06-31/08;" -> listOf(Schedule("SCH16", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-2606-3108", 26, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+30/06-31/08;" -> listOf(Schedule("SCH17", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-3006-3108", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell))
            "S+1,2,3,4,5,6,p+01/01-31/12;" -> listOf(Schedule("SCH18", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+01/07-31/08;" -> listOf(Schedule("SCH19", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0107-3108", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+01/09-14/06;" -> listOf(Schedule("SCH20", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0109-1406", 1, 9, 14, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+01/09-30/06;" -> listOf(Schedule("SCH21", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0109-3006", 1, 9, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+01/10-30/06;" -> listOf(Schedule("SCH22", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0110-3006", 1, 10, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+15/06-31/08;" -> listOf(Schedule("SCH23", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-1506-3108", 15, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,7,p+01/01-31/12;" -> listOf(Schedule("SCH24", listOf(1,2,3,4,5,7), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,7,p+01/06-31/08;" -> listOf(Schedule("SCH25", listOf(1,2,3,4,5,7), listOf(Period("PERIOD-0106-3108", 1, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,7,p+01/09-31/05;" -> listOf(Schedule("SCH26", listOf(1,2,3,4,5,7), listOf(Period("PERIOD-0109-3105", 1, 9, 31, 5)), listOf(1,2,3,4,5,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/01-31/12;" -> listOf(Schedule("SCH27", listOf(1,2,3,4,5), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/02-15/05;" -> listOf(Schedule("SCH28", listOf(1,2,3,4,5), listOf(Period("PERIOD-0102-1505", 1, 2, 15, 5)), listOf(2,3,4,5), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/06-30/09;" -> listOf(Schedule("SCH29", listOf(1,2,3,4,5), listOf(Period("PERIOD-0106-3009", 1, 6, 30, 9)), listOf(6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/09-10/06;" -> listOf(Schedule("SCH30", listOf(1,2,3,4,5), listOf(Period("PERIOD-0109-1006", 1, 9, 10, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/09-30/06;" -> listOf(Schedule("SCH31", listOf(1,2,3,4,5), listOf(Period("PERIOD-0109-3006", 1, 9, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+15/09-31/12;" -> listOf(Schedule("SCH32", listOf(1,2,3,4,5), listOf(Period("PERIOD-1509-3112", 15, 9, 31, 12)), listOf(9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,5,6,p+01/01-31/12;" -> listOf(Schedule("SCH33", listOf(1,2,3,5,6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,3,4,6,p+15/06-31/08;" -> listOf(Schedule("SCH34", listOf(1,3,4,6), listOf(Period("PERIOD-1506-3108", 15, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,3,5,6,p+01/01-31/12;" -> listOf(Schedule("SCH35", listOf(1,3,5,6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,5,p+01/01-31/12;" -> listOf(Schedule("SCH36", listOf(1,5), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,5,p+01/07-31/08;" -> listOf(Schedule("SCH37", listOf(1,5), listOf(Period("PERIOD-0107-3108", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+6,7,p+01/01-31/12;" -> listOf(Schedule("SCH38", listOf(6,7), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+6,p+01/01-31/12;" -> listOf(Schedule("SCH39", listOf(6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+7,p+01/01-31/12;" -> listOf(Schedule("SCH40", listOf(7), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+7,p+01/09-30/06;" -> listOf(Schedule("SCH41", listOf(7), listOf(Period("PERIOD-0109-3006", 1, 9, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/05-30/09;S+1,2,3,4,5,6,p+01/10-30/04;" -> listOf(
                Schedule("SCH42", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0105-3009", 1, 5, 30, 9)), listOf(5,6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"),
                Schedule("SCH43", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0110-3004", 1, 10, 30, 4)), listOf(1,2,3,4,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"),
            )
            "S+1,2,3,4,5,p+15/09-31/12;S+1,2,3,4,5,p+01/02-15/05;" -> listOf(
                Schedule("SCH32", listOf(1,2,3,4,5), listOf(Period("PERIOD-1509-3112", 15, 9, 31, 12)), listOf(9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"),
                Schedule("SCH28", listOf(1,2,3,4,5), listOf(Period("PERIOD-0102-1505", 1, 2, 15, 5)), listOf(2,3,4,5), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S")
            )
            else -> createSchedulesBasedOnTripNumber(scheduleCell, scheduleDescriptionCell, tripNumber, journeyName)
        }
    }

    private fun createSchedulesForJourney(scheduleCell: String, scheduleDescriptionCell: String, journeyName: String): List<Schedule> {
        return when(scheduleCell) {
            "S+1,2,3,4,5+01/01-31/12;" -> listOf(Schedule("SCH01", listOf(1,2,3,4,5), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), false, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5+01/09-31/05;" -> listOf(Schedule("SCH02", listOf(1,2,3,4,5), listOf(Period("PERIOD-0109-3105", 1, 9, 31, 5)), listOf(1,2,3,4,5,9,10,11,12), false, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6+01/01-31/12;" -> listOf(Schedule("SCH03", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), false, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/01-31/12;" -> listOf(Schedule("SCH04", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/04-30/09;" -> listOf(Schedule("SCH05", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0104-3009", 1, 4, 30, 9)), listOf(4,5,6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/06-30/09;" -> listOf(Schedule("SCH06", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0106-3009", 1, 6, 30, 9)), listOf(6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/06-31/08;" -> listOf(Schedule("SCH07", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0106-3108", 1, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/07-01/09;" -> listOf(Schedule("SCH08", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0107-0109", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/07-30/08;" -> listOf(Schedule("SCH09", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0107-3008", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/07-31/08;" -> listOf(Schedule("SCH10", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0107-3108", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/09-30/06;" -> listOf(Schedule("SCH11", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0109-3006", 1, 9, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/09-31/05;" -> listOf(Schedule("SCH12", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0109-3105", 1, 9, 31, 5)), listOf(1,2,3,4,5,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+15/06-15/09;" -> listOf(Schedule("SCH13", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-1506-1509", 15, 6, 15, 9)), listOf(6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+15/06-31/08;" -> listOf(Schedule("SCH14", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-1506-3108", 15, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+20/06-31/08;" -> listOf(Schedule("SCH15", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-2006-3108", 20, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+26/06-31/08;" -> listOf(Schedule("SCH16", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-2606-3108", 26, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+30/06-31/08;" -> listOf(Schedule("SCH17", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-3006-3108", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+01/01-31/12;" -> listOf(Schedule("SCH18", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+01/07-31/08;" -> listOf(Schedule("SCH19", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0107-3108", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+01/09-14/06;" -> listOf(Schedule("SCH20", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0109-1406", 1, 9, 14, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+01/09-30/06;" -> listOf(Schedule("SCH21", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0109-3006", 1, 9, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+01/10-30/06;" -> listOf(Schedule("SCH22", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0110-3006", 1, 10, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,p+15/06-31/08;" -> listOf(Schedule("SCH23", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-1506-3108", 15, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,7,p+01/01-31/12;" -> listOf(Schedule("SCH24", listOf(1,2,3,4,5,7), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,7,p+01/06-31/08;" -> listOf(Schedule("SCH25", listOf(1,2,3,4,5,7), listOf(Period("PERIOD-0106-3108", 1, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,7,p+01/09-31/05;" -> listOf(Schedule("SCH26", listOf(1,2,3,4,5,7), listOf(Period("PERIOD-0109-3105", 1, 9, 31, 5)), listOf(1,2,3,4,5,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/01-31/12;" -> listOf(Schedule("SCH27", listOf(1,2,3,4,5), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/02-15/05;" -> listOf(Schedule("SCH28", listOf(1,2,3,4,5), listOf(Period("PERIOD-0102-1505", 1, 2, 15, 5)), listOf(2,3,4,5), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/06-30/09;" -> listOf(Schedule("SCH29", listOf(1,2,3,4,5), listOf(Period("PERIOD-0106-3009", 1, 6, 30, 9)), listOf(6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/09-10/06;" -> listOf(Schedule("SCH30", listOf(1,2,3,4,5), listOf(Period("PERIOD-0109-1006", 1, 9, 10, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+01/09-30/06;" -> listOf(Schedule("SCH31", listOf(1,2,3,4,5), listOf(Period("PERIOD-0109-3006", 1, 9, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,p+15/09-31/12;" -> listOf(Schedule("SCH32", listOf(1,2,3,4,5), listOf(Period("PERIOD-1509-3112", 15, 9, 31, 12)), listOf(9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,5,6,p+01/01-31/12;" -> listOf(Schedule("SCH33", listOf(1,2,3,5,6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,3,4,6,p+15/06-31/08;" -> listOf(Schedule("SCH34", listOf(1,3,4,6), listOf(Period("PERIOD-1506-3108", 15, 6, 31, 8)), listOf(6,7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,3,5,6,p+01/01-31/12;" -> listOf(Schedule("SCH35", listOf(1,3,5,6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,5,p+01/01-31/12;" -> listOf(Schedule("SCH36", listOf(1,5), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,5,p+01/07-31/08;" -> listOf(Schedule("SCH37", listOf(1,5), listOf(Period("PERIOD-0107-3108", 1, 7, 31, 8)), listOf(7,8), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+6,7,p+01/01-31/12;" -> listOf(Schedule("SCH38", listOf(6,7), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+6,p+01/01-31/12;" -> listOf(Schedule("SCH39", listOf(6), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+7,p+01/01-31/12;" -> listOf(Schedule("SCH40", listOf(7), listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+7,p+01/09-30/06;" -> listOf(Schedule("SCH41", listOf(7), listOf(Period("PERIOD-0109-3006", 1, 9, 30, 6)), listOf(1,2,3,4,5,6,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"))
            "S+1,2,3,4,5,6,7,p+01/05-30/09;S+1,2,3,4,5,6,p+01/10-30/04;" -> listOf(
                Schedule("SCH42", listOf(1,2,3,4,5,6,7), listOf(Period("PERIOD-0105-3009", 1, 5, 30, 9)), listOf(5,6,7,8,9), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"),
                Schedule("SCH43", listOf(1,2,3,4,5,6), listOf(Period("PERIOD-0110-3004", 1, 10, 30, 4)), listOf(1,2,3,4,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"),
            )
            "S+1,2,3,4,5,p+15/09-31/12;S+1,2,3,4,5,p+01/02-15/05;" -> listOf(
                Schedule("SCH32", listOf(1,2,3,4,5), listOf(Period("PERIOD-1509-3112", 15, 9, 31, 12)), listOf(9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S"),
                Schedule("SCH28", listOf(1,2,3,4,5), listOf(Period("PERIOD-0102-1505", 1, 2, 15, 5)), listOf(2,3,4,5), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell, "S")
            )
            //else -> Schedule("SCH99ED", listOf(1,2,3,4,5,6,7), listOf(Period("NON-STOP", 1, 1, 31, 12)), listOf(1,2,3,4,5,6,7,8,9,10,11,12), true, scheduleCell.trim(), scheduleDescriptionCell, scheduleDescriptionCell)
            else -> createSchedulesBasedOnTripNumberForJourney(scheduleCell, scheduleDescriptionCell, journeyName)
        }
    }

    private fun createSchedulesBasedOnTripNumber(scheduleCell: String, scheduleDescriptionCell: String, tripNumber: Int, journeyName: String): List<Schedule> {
        val schedules = scheduleCell.split(";")
        val size = schedules.size - 1
        val resultSchedules = mutableListOf<Schedule>()
        for(i in 0 until size) {
            val sch = schedules[i]
            val schList = sch.split("+")
            val tripNumbers = schList[0].split(",")
            if(tripNumbers.contains(tripNumber.toString())) {
                val days = schList[1].split(",")
                val workDays: MutableList<Int> = ArrayList<Int>()
                var holiday = false
                days.forEach {
                    if(it == "p") {
                        holiday = true
                    } else {
                        if(it.isNotBlank()) workDays.add(it.toInt())
                    }
                }
                val periods = schList[2].split("-")
                val froms = periods[0].split("/")
                val tos = periods[1].split("/")

                val fromDay = froms[0].toInt()
                val fromMonth = froms[1].toInt()

                val toDay = tos[0].toInt()
                val toMonth = tos[1].toInt()

                val workMonths: MutableList<Int> = ArrayList()
                if(fromMonth <= toMonth) {
                    for(fm in fromMonth..toMonth) {
                        workMonths.add(fm)
                    }
                } else {
                    for(afm in fromMonth..12) {
                        workMonths.add(afm)
                    }
                    for (tm in 1..toMonth) {
                        workMonths.add(tm)
                    }
                }
                val schedule = Schedule(journeyName + tripNumber, workDays, listOf(Period("PERIOD-$fromDay$fromMonth-$toDay$toMonth", fromDay, fromMonth, toDay, toMonth)), workMonths, holiday, sch, scheduleDescriptionCell, scheduleDescriptionCell, tripNumber.toString())
                resultSchedules.add(schedule)
            }
        }

        if(resultSchedules.isEmpty()) {
            return listOf(
                Schedule(
                    "SCH04",
                    listOf(1, 2, 3, 4, 5, 6, 7),
                    listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)),
                    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
                    true,
                    scheduleCell.trim(),
                    scheduleDescriptionCell,
                    scheduleDescriptionCell
                ),
            )
        } else {
            return resultSchedules
        }
    }
    private fun createSchedulesBasedOnTripNumberForJourney(scheduleCell: String, scheduleDescriptionCell: String, journeyName: String): List<Schedule> {
        val schedules = scheduleCell.split(";")
        val size = schedules.size - 1
        val resultSchedules = mutableListOf<Schedule>()
        for(i in 0 until size) {
            val sch = schedules[i]
            val schList = sch.split("+")
            val tripNumbers = schList[0].split(",")

            val days = schList[1].split(",")
            val workDays: MutableList<Int> = ArrayList<Int>()
            var holiday = false
            days.forEach {
                if(it == "p") {
                    holiday = true
                } else {
                    if(it.isNotBlank()) workDays.add(it.toInt())
                }
            }
            val periods = schList[2].split("-")
            val froms = periods[0].split("/")
            val tos = periods[1].split("/")

            val fromDay = froms[0].toInt()
            val fromMonth = froms[1].toInt()

            val toDay = tos[0].toInt()
            val toMonth = tos[1].toInt()

            val workMonths: MutableList<Int> = ArrayList()
            if(fromMonth <= toMonth) {
                for(ftm in fromMonth..toMonth) {
                    workMonths.add(ftm)
                }
            } else {
                for(fm in fromMonth..12) {
                    workMonths.add(fm)
                }
                for (tm in 1..toMonth) {
                    workMonths.add(tm)
                }
            }
            val schedule = Schedule(journeyName + tripNumbers.joinToString("-"), workDays, listOf(Period("PERIOD-$fromDay$fromMonth-$toDay$toMonth", fromDay, fromMonth, toDay, toMonth)), workMonths, holiday, sch, scheduleDescriptionCell, scheduleDescriptionCell, tripNumbers.joinToString(","))
            resultSchedules.add(schedule)
        }

        if(resultSchedules.isEmpty()) {
            return listOf(
                Schedule(
                    "SCH04",
                    listOf(1, 2, 3, 4, 5, 6, 7),
                    listOf(Period("PERIOD-0101-3112", 1, 1, 31, 12)),
                    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12),
                    true,
                    scheduleCell.trim(),
                    scheduleDescriptionCell,
                    scheduleDescriptionCell
                ),
            )
        } else {
            return resultSchedules
        }
    }

    private fun createLineName(carrierName: String, lineNumber: Int): String? {
        var lns = lineNumber.toString()

        if(lineNumber / 100 == 0) {
            lns = "0$lns"
        }

        if(lineNumber / 10 == 0) {
            lns = "0$lns"
        }

        return carrierName + lns
    }

    private fun createCarrierName(firstCellValue: String): String {
        return when(firstCellValue) {
            "ЃОКО ГЛИГОР ШОНТЕВСКИ - БЕРОВО" -> "GGS"
            "ЃОКО ТРАНС - КРУШЕВО" -> "GOT"
            "ЈОЦО ТУРС МС - КУМАНОВО" -> "JTM"
            "ЏЕМ ТУРС - БИТОЛА" -> "GMT"
            "АВТО АТОМ - КОЧАНИ" -> "AAK"
            "АМИГО КОМПАНИ - ГЕВГЕЛИЈА" -> "AMK"
            "АНДОН КОМПАНИ - ПЕХЧЕВО" -> "ANP"
            "АРИ ДИЕ 2014 - СКОПЈЕ" -> "ARD"
            "АТП ПРОЛЕТЕР - КРИВА ПАЛАНКА" -> "PRO"
            "БАЛКАН ТОУРС - Ростуше" -> "BLT"
            "БЕРОВО ТРАНС - БЕРОВО" -> "BER"
            "БИСЕР ПРОМ - Скопје" -> "BIS"
            "ВАКЧАРЕ - РАДОЛИШТА" -> "VAK"
            "ГАЛЕБ - ОХРИД" -> "GAL"
            "ДАЈО ТУРС - Неготино" -> "DAJ"
            "ДЕ КА - ДЕМИР КАПИЈА" -> "DEK"
            "ДЕЛУКС ТУРС - КУМАНОВО" -> "DLK"
            "ДЕЛФИНА ТУРС - СКОПЈЕ" -> "DLF"
            "ДОРА ТУРИСТ - Куманово" -> "DRA"
            "ДОРИ - ГЕВГЕЛИЈА" -> "DRI"
            "ДУРМО ТУРС - ГОСТИВАР" -> "DUR"
            "ЕДИ ТУРС - Липково" -> "EDI"
            "ЕКО ТОУРС - ТЕТОВО" -> "EKO"
            "ЕКСТРА 03 - КАВАДАРЦИ" -> "EKS"
            "ЕУРО ЛИНИА - СТРУГА" -> "EUL"
            "ЕУРО ТУРИСТ - ГОСТИВАР" -> "EUT"
            "ЕУРОПА БУС - СТРУГА" -> "EUB"
            "ЕУРОПА БУС СВ 2018 - Велешта" -> "EBV"
            "ЗОРЕЛ ТРАНС - Гостивар" -> "ZOR"
            "ИДО ТОУРС - ТЕТОВО" -> "IDO"
            "КАМ СМАРК - КАВАДАРЦИ" -> "KAM"
            "КЛАСИК КОМПАНИ - СТРУГА" -> "KLA"
            "ЛАСТА ТУРС - ДЕБРЕШТЕ" -> "LAS"
            "МАК ТРАВЕЛ - Прилеп" -> "MAK"
            "МАМЛИ ТРАВЕЛ - КУМАНОВО" -> "MAM"
            "МАРЈАН ТУРС - ПРОБИШТИП" -> "MRJ"
            "МАРИЈА ТРАНС - БИТОЛА" -> "MAR"
            "МАРТИНОСКИ - СТРУГА" -> "MRT"
            "МЕРЏО КОМПАНИ - НИКУШТАК" -> "MER"
            "МЕТРО ТРАНС - Дебреште" -> "MET"
            "НАСИР ТУРИЗАМ - ГОСТИВАР" -> "NAS"
            "ОМЕГА 2006 - ОХРИД" -> "OMG"
            "ПАН ПРОМЕТ - ВЕЛЕС" -> "PAN"
            "ПЕЛАГОНИЈА ТРАНС - ПРИЛЕП" -> "PEL"
            "РАДИКА ПРЕВОЗ НСР - Центар Жупа" -> "RAD"
            "РАМА ТУРС - ДЕБРЕШТЕ" -> "RAM"
            "РЕИС К - СВЕТИ НИКОЛЕ" -> "REI"
            "РОЗ РУБ ТРАНС - Кривогаштани" -> "ROZ"
            "РУЛЕ ТУРС - СКОПЈЕ" -> "RUL"
            "СЕНАД ТОУРС - ВЕЛЕШТА" -> "SEN"
            "СКОРПИОН МК - КРУШЕВО" -> "SKO"
            "СОТКА БУС - Свети Николе" -> "SOT"
            "СПРИЈА - ВЕВЧАНИ" -> "SPR"
            "СТЕЛА БУС - Кривогаштани" -> "STE"
            "СТРУМИЦА ЕКСПРЕС - СТРУМИЦА" -> "STR"
            "Т СТЕФАНИ - Пирава" -> "TST"
            "ТРАНС БАЛКАН - ГЕВГЕЛИЈА" -> "TRB"
            "ТРАНСКОП - БИТОЛА" -> "TRA"
            "УНИ ТОУРС - ПРИЛЕП" -> "UNI"
            "УНИК АР ТУРС - ДЕБАР" -> "UNK"
            "ФИКРЕТ ТУРИЗАМ - КУМАНОВО" -> "FKR"
            "ФИЛЕ ТУРС - КРАТОВО" -> "FIL"
            "ХИСАРИ ГД - ДЕБАР" -> "HIS"
            "ЦОБИ СМ - БЕРОВО" -> "COB"
            "ЧИКРИ ТРЕЈД - ДЕБАР" -> "CIK"
            "ШТУЗ ПАТНИЧКИ - КУМАНОВО" -> "TU"
            else -> "N/A"
        }
    }
}
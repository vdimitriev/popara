package mk.vedmak.avtobusi.popara.repository

import mk.vedmak.avtobusi.popara.model.Trip
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.io.File
import java.lang.IllegalStateException

@SpringBootTest
class AvtobuskiLinii {

    @Test
    fun readXlsFile2() {
        var rows = ArrayList<Row>()
        val workBook = WorkbookFactory.create(File("files/avtobuski-linii-test.xlsx"))
        workBook.use { wb ->
            val sheet = wb.getSheetAt(0)
            val firstRowNum = sheet.firstRowNum
            val lastRowNum = sheet.lastRowNum
            var firstCell:Cell? = null

            var firstRowCount = 0
            var lastRowCount = 0
            //var lastCell:Cell? = null
            for(rowCount in firstRowNum until lastRowNum) {
                val row = sheet.getRow(rowCount)
                val cell = row.getCell(0)
                if(firstCell == null && cell != null && cell.toString().isNotBlank()) {
                    firstCell = cell
                    firstRowCount = rowCount
                    rows.add(row)
                } else if((cell == null || cell.toString().isBlank()) && firstCell != null) {
                    rows.add(row)
                } else if(cell != null && cell.toString().isNotBlank() && firstCell != null) {
                    rows.add(row)
                    val carrier = firstCell.toString()
                    firstCell = null
                    lastRowCount = rowCount
                    extractAllLinesForCarrier(rows, carrier, firstRowCount, lastRowCount)
                    rows = ArrayList()
                }
            }
        }
    }

    private fun extractAllLinesForCarrier(rows: ArrayList<Row>, carrier: String?, firstRowCount: Int, lastRowCount:Int) {
        //rows.forEach { r -> println("$carrier, firstRowCount = $firstRowCount, lastRowCount = $lastRowCount, row num = ${r.rowNum}, cell = ${r.getCell(0)}") }
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
                extractAllJourneysForLine(journeys, carrier, firstRowCount, lastRowCount)
                journeys = ArrayList()
            }
        }
    }

    private fun extractAllJourneysForLine(rows: ArrayList<Row>, carrier: String?, firstRowCount: Int, lastRowCount: Int) {

        var trips = ArrayList<Row>()
        var firstCell:Cell? = null


        extractOneWay(rows, carrier)
        extractReturn(rows, carrier)

        println()
        println()
    }


    private fun extractOneWay(rows: ArrayList<Row>, carrier: String?) {
        val rowsSize = rows.size

        val lineNumber = rows[0].getCell(2).numericCellValue.toInt()
        val lineName = rows[0].getCell(3).stringCellValue

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
        println("========================================")
        for(jco in 0 until journeyCountCellOneWayMax) {
            for (i in 0 until rowsSize - 1) {
                for (j in (i + 1) until rowsSize) {
                    println("$carrier: $lineNumber: $lineName: ${rows[i].getCell(17)} - ${rows[j].getCell(17)} -> ${rows[i].getCell(6 + jco)} - ${rows[j].getCell(6 + jco)}")
                }
            }
        }
    }

    private fun extractReturn(rows: ArrayList<Row>, carrier: String?) {
        val rowsSize = rows.size

        val lineNumber = rows[0].getCell(2).numericCellValue.toInt()
        val lineName = rows[0].getCell(3).stringCellValue

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
        println("----------------------------------------")
        val rowsSizeMinusOne = rowsSize - 1
        for(jcr in 0 until journeyCountCellReturnMax) {
            for (k in rowsSizeMinusOne downTo 1) {
                for (l in (k - 1) downTo 0) {
                    println("$carrier: $lineNumber: $lineName: ${rows[k].getCell(17)} - ${rows[l].getCell(17)} -> ${rows[k].getCell(18 + jcr)} - ${rows[l].getCell(18 + jcr)}")
                }
            }
        }
        println("========================================")
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

    @Test
    fun readXlsFile() {
        val carriers = HashSet<String>()
        var rows = ArrayList<Row>()
        var rowCount = 0
        val workBook = WorkbookFactory.create(File("files/avtobuski-linii.xls"))
        workBook.use { wb ->
            val sheet = wb.getSheetAt(0)
            var carrierStarted = false
            var startCell:Cell? = null
            var endCell:Cell? = null
            sheet.rowIterator().forEach { row ->
                try {
                    val cell = row.getCell(0)
                    when {
                        cell != null && cell != startCell -> startCell = cell
                        cell != null && cell == startCell -> endCell = cell
                    }
                    if(startCell != null) {
                        if (startCell == endCell) {
                            startCell = null
                            endCell = null
                        } else {
                            rows.add(row)
                            rowCount += 1
                        }
                    }
                } catch(e: IllegalStateException) {
                    println("Illegal carrier found.")
                }
            }
        }

        rows.forEach {
            println("row = ${it.rowNum}")
        }
        assertTrue(true)
    }

//    @Test
//    fun readAvtobuskiLiniiXlsFile() {
//        val carriers = HashSet<String>()
//        var carrierCell = ""
//        val workBook = WorkbookFactory.create(File("files/avtobuski-linii.xls"))
//        workBook.use { wb ->
//            val sheet = wb.getSheetAt(0)
//            sheet.rowIterator().forEach { row ->
//                try {
//                    carrierCell = row.cellIterator().next().toString()
//                    if(carrierCell.isNotBlank()) {
//                        carriers.add(carrierCell)
//                    }
//
//                } catch(e: IllegalStateException) {
//                    println("Illegal carrier found.")
//                }
//            }
//        }
//
////        println("-------------------------------------------------")
////        carriers.forEach {
////            println(it)
////        }
//
//        println("-------------------------------------------------")
//
//        val cs = carriers.size
//        println("-------------------------------------------------")
//        println("-------------------------------------------------")
//        println("Carrier count = $cs")
//        println("-------------------------------------------------")
//        carriers.sortedBy { car -> car }.forEach {
//            println(it)
//        }
//
//        println("-------------------------------------------------")
//        println("Carrier count = $cs")
//        assertTrue(true)
//    }
}
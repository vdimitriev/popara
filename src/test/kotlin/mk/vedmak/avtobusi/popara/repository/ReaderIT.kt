//package mk.vedmak.avtobusi.popara.repository
//
//import org.apache.poi.ss.usermodel.WorkbookFactory
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.core.io.ResourceLoader
//import java.io.BufferedReader
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileReader
//import java.lang.IllegalStateException
//
//@SpringBootTest
//class ReaderIT {
//
////    @Autowired
////    private lateinit var resourceLoader: ResourceLoader
//
////    @Test
////    fun readCsvFile() {
////        File("buslines.csv").bufferedReader().useLines { lines ->
////            lines.forEach { line ->
////                println(line)
////            }
////        }
////        assertTrue(true)
////    }
//
//    @Test
//    fun readXlsFile() {
////        val resource = resourceLoader.getResource("classpath:buslines.xls")
////        val inputStream = FileInputStream(File("buslines.xls"))
//        val carriers = HashSet<String>()
//        var carrierCell = ""
//        val workBook = WorkbookFactory.create(File("buslines-full.xls"))
//        workBook.use { wb ->
//            val sheet = wb.getSheetAt(0)
//            sheet.rowIterator().forEach { row ->
//                try {
//                    carrierCell = row.cellIterator().next().toString()
//                    if(carrierCell.isNotBlank()) {
//                        //println("-------------------------------------------------")
//                        //print(carrierCell)
//                        carriers.add(carrierCell)
//                        //println()
//                    }
//
//                } catch(e: IllegalStateException) {
//                    println("Illegal carrier found.")
//                }
//            }
////            for(row in sheet) {
////                println("-------------------------------------------------")
////                for(cell in row) {
////                    //print(cell)
////                }
////                println()
////            }
////            val row0 = sheet.getRow(0)
////            val cell00 = row0.getCell(0)
////            println(cell00.stringCellValue)
//        }
//
//        val cs = carriers.size
//        println("-------------------------------------------------")
////        println("Carrier count = $cs")
////        carriers.forEach { carrier ->
////            println("-------------------------------------------------")
////            println(carrier)
////        }
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
//
//    }
//}
package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class Schedule(

    @Id
    var name: String = "",

    var workDays: List<Int> = ArrayList(),

    var workPeriods: List<Period> = ArrayList(),

    var workMonths: List<Int> = ArrayList(),

    var holidays: Boolean = true,

    var description: String = "",

    var descriptionLatin: String = "",

    var descriptionCyrillic: String = "",

    var tripNumbers: String = ""

)
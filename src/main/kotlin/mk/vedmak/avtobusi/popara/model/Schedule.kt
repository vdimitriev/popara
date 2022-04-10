package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class Schedule(
    @Id
    val name: String? = null,
    val workDays: List<Int> = ArrayList(),
    val workPeriod: String? = "",
    val workMonths: List<Int> = ArrayList(),
    val description: String? = "",
    val descriptionLatin: String? = "",
    val descriptionCyrillic: String? = ""
)
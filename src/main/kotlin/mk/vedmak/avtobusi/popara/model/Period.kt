package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import java.time.LocalDate

@Node
data class Period(

    @Id
    val name: String = "",

    val fromDay: Int,

    val fromMonth: Int,

    val toDay: Int,

    val toMonth: Int
)
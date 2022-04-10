package mk.vedmak.avtobusi.popara.model

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node
data class Line(
    @Id
    val name: String?,
    val fullNameLatin: String?,
    val fullNameCyrillic: String?,
    val lineNumber: Int?,
    @Relationship("PERFORMS")
    val journey: Journey?,
    @Relationship("FINDS_PERIOD_OF_WORK_AT")
    val schedule: Schedule?,
    val description: String? = null
)
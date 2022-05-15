package mk.vedmak.avtobusi.popara.model

import org.springframework.data.annotation.Version
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node

@Node
data class Country(

    @Id
    val name: String,

    val latinName: String? = null,

    val cyrillicName: String? = null,

    val twoLetterCountryCode: String? = null,

    val threeLetterCountryCode: String? = null,

    @Version
    val version: Long = 0,

    )
package mk.vedmak.avtobusi.popara.util

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currency: String
)
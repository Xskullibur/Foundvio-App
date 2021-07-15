package com.foundvio.tracking

import java.math.BigDecimal

data class GeoCoord(
    val lat: BigDecimal,
    val lng: BigDecimal
){
    /**
     * Converts [GeoCoord] into a [String] representation of Degrees Minutes Seconds format
     */
    fun toDMSFormat(): String{
        val degree = lat.toBigInteger()
        val decimal = lat.subtract(degree.toBigDecimal())
        val mins = decimal.multiply(BASE60_MULTIPLICAND)
        val seconds = mins.subtract(mins.toBigInteger().toBigDecimal())
            .multiply(BASE60_MULTIPLICAND)

        //Format geo coordinate into DMS format
        return "$degree ° ${mins.toBigInteger()}' $seconds\""
    }

    companion object {
        val BASE60_MULTIPLICAND = BigDecimal(60)
    }

}

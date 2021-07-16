package com.foundvio.tracking

import com.huawei.hms.maps.model.LatLng
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

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
        private val MATH_CONTEXT = MathContext(4, RoundingMode.HALF_UP)

        fun create(lat: Double, lng: Double) = GeoCoord(
            lat.toBigDecimal(MATH_CONTEXT), lng.toBigDecimal(MATH_CONTEXT)
        )

        infix fun Double.at(lng: Double) = create(this, lng)

    }

}

fun GeoCoord.toLatLng(): LatLng{
    return LatLng(this.lat.toDouble(), this.lng.toDouble())
}
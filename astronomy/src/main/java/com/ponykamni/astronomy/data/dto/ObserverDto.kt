package com.ponykamni.astronomy.data.dto

import com.google.gson.annotations.SerializedName
import java.util.*

internal class ObserverDto(
    val latitude: Double = SAINT_PETERSBURG_LATITUDE,
    val longitude: Double = SAINT_PETERSBURG_LONGITUDE,
    val elevation: Int = SAINT_PETERSBURG_ELEVATION,

    @SerializedName("from_date")
    val fromDate: String,

    @SerializedName("to_date")
    val toDate: String,

    val time: String,
) {


    companion object {

        fun getObserverForTime(timeMs: Long): ObserverDto {
            val calendar = Calendar.getInstance().apply { timeInMillis = timeMs }

            val hour = calendar.get(Calendar.HOUR_OF_DAY).toString().addZeroIfOneDigit()
            val minute = calendar.get(Calendar.MINUTE).toString().addZeroIfOneDigit()

            val time = "$hour:$minute:00"

            val day = calendar.get(Calendar.DAY_OF_MONTH).toString().addZeroIfOneDigit()
            val month = (calendar.get(Calendar.MONTH) + 1).toString().addZeroIfOneDigit()
            val year = calendar.get(Calendar.YEAR).toString()

            val date = "$year-$month-$day"

            return ObserverDto(fromDate = date, toDate = date, time = time)
        }

        private fun String.addZeroIfOneDigit(): String =
            if (this.length == 1 && this[0].isDigit()) {
                "0$this"
            } else {
                this
            }
    }
}

private const val SAINT_PETERSBURG_LATITUDE = 59.561900
private const val SAINT_PETERSBURG_LONGITUDE = 30.200906
private const val SAINT_PETERSBURG_ELEVATION = 5
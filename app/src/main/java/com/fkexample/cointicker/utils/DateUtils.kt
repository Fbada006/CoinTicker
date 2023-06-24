package com.fkexample.cointicker.utils

import java.util.Date
import android.text.format.DateUtils as AndroidDateUtils

object DateUtils {

    fun getTimeAgo(date: Long): String {
        val currentTimeMillis = System.currentTimeMillis()
        val timeMillis = Date(date).time
        val flags = AndroidDateUtils.FORMAT_ABBREV_RELATIVE or AndroidDateUtils.FORMAT_SHOW_YEAR or AndroidDateUtils.FORMAT_SHOW_DATE
        return AndroidDateUtils.getRelativeTimeSpanString(timeMillis, currentTimeMillis, AndroidDateUtils.MINUTE_IN_MILLIS, flags).toString()
    }
}
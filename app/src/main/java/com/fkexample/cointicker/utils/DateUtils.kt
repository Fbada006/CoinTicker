package com.fkexample.cointicker.utils

import java.util.Date
import android.text.format.DateUtils as AndroidDateUtils

/**
 * Utility class for handling date-related operations.
 */
object DateUtils {

    /**
     * Retrieves a string representation of the time elapsed since the given date.
     * @param date The date to calculate the elapsed time from.
     * @return A string representation of the elapsed time.
     */
    fun getTimeAgo(date: Long): String {
        val currentTimeMillis = System.currentTimeMillis()
        val timeMillis = Date(date).time
        val flags = AndroidDateUtils.FORMAT_ABBREV_RELATIVE or AndroidDateUtils.FORMAT_SHOW_YEAR or AndroidDateUtils.FORMAT_SHOW_DATE
        return AndroidDateUtils.getRelativeTimeSpanString(timeMillis, currentTimeMillis, AndroidDateUtils.MINUTE_IN_MILLIS, flags).toString()
    }
}

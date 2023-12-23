package cz.mendelu.pef.flashyflashcards.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    fun getDateString(unixTime: Long): String {
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = unixTime

        return format.format(calendar.time)
    }

    fun getUnixTime(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        return calendar.timeInMillis
    }
}
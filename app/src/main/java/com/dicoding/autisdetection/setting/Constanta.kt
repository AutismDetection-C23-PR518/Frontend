package com.dicoding.autisdetection.setting

import java.text.SimpleDateFormat
import java.util.*

object Constanta {

    private const val DB_DATE_FORMAT = "yyyy-MM-dd"
    private const val DISPLAY_DATE_FORMAT = "dd MMMM yyyy"

    fun convertDbDateToDisplayDate(dbDate: String): String {
        val dbDateFormat = SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault())
        val displayDateFormat = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())

        val date = dbDateFormat.parse(dbDate)
        return displayDateFormat.format(date)
    }

}
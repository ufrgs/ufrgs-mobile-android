package com.cpd.utils

import java.util.*


object DateUtils {

    private val TAG = "DateUtils"

    fun todayIsWeekend(): Boolean {

        val now = Calendar.getInstance(TimeZone.getTimeZone("GMT-3:00"))
        val day = now.get(Calendar.DAY_OF_WEEK)

//        System.out.println("NOW: " + now)

        return (day == Calendar.SATURDAY || day == Calendar.SUNDAY)

    }

}

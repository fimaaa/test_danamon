package com.general.common.util

import android.text.format.DateFormat
import com.general.model.common.enum.Language
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object DateUtils {

    const val DEFAULT_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DEFAULT_API_SENDDATE_FORMAT = "dd-MM-yyyy"
    const val CALENDAR_FULLSHOWN_FORMAT = "dd/MMMM/yyyy"
    const val CALENDAR_RECYCLERVIEW_FORMAT = "yyyy-MM"
    const val CALENDAR_BOTTOMSHEET_FORMAT = "MMMM yyyy"
    private const val SIMPLE_FORMAT = "yyyy-MM-dd"
    const val DMY_FORMAT = "dd/MM/yyyy"

    fun getCalendarFromString(
        formatString: String,
        dateString: String,
        locale: Locale? = null
    ): Calendar? {
        try {

            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat(formatString, locale ?: Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            calendar.time = sdf.parse(dateString)!!

            return calendar
        } catch (_: ParseException) {
        }
        return null
    }

    fun getCalendarToString(
        formatString: String,
        date: Calendar
    ): String {
        return DateFormat.format(formatString, date.time).toString()
    }

    /**
     * @param dateInput input date string eg: 2020-12-31
     * @param dateInputFormat input date format eg: yyyy-MM-dd
     * @param dateOutputFormat output date format eg: yyyy/MM/dd
     * @param locale default
     * @param ordinalSuffix eg: 21 to 21st
     * @return eg: 2020/12/31
     * */
    fun getDateStringWithFormat(
        dateInput: String,
        dateInputFormat: String,
        dateOutputFormat: String,
        locale: Locale? = null,
        ordinalSuffix: Boolean? = false
    ): String? {
        return try {
            val defaultLocale = Locale(Language.INDONESIA.language, Language.INDONESIA.country)
            val sdfOrigin = SimpleDateFormat(dateInputFormat, locale ?: defaultLocale)
            sdfOrigin.timeZone = TimeZone.getTimeZone("UTC")
            val dateOrigin = sdfOrigin.parse(dateInput)
            val dateResult = dateOrigin?.let {
                SimpleDateFormat(dateOutputFormat, locale ?: defaultLocale).format(it)
            }
            if (ordinalSuffix == true && dateOrigin != null) {
                dateResult?.let {
                    return dateOrdinalSuffix(dateOrigin, it, locale ?: defaultLocale)
                }
            }
            return dateResult
        } catch (e: ParseException) {
            null
        }
    }

    private fun dateOrdinalSuffix(dateInput: Date, dateOutput: String, locale: Locale): String {
        return when (locale) {
            Locale.UK, Locale.ENGLISH, Locale.US -> {
                val cal = Calendar.getInstance()
                cal.time = dateInput
                val day = cal.get(Calendar.DAY_OF_MONTH)
                "${dateOutput.substringBefore("$day")}${ordinalSuffix(day)}${
                    dateOutput.substringAfter("$day")
                }"
            }
            else -> dateOutput
        }
    }

    private fun ordinalSuffix(day: Int): String {
        return if (day in 11..13) "${day}th"
        else when (day % 10) {
            1 -> "${day}st"
            2 -> "${day}nd"
            3 -> "${day}rd"
            else -> "${day}th"
        }
    }

    fun getDateMonthly(date: String): MutableList<Date> {
        val maxCalenderDays = 42
        val calendar = Calendar.getInstance(Locale.getDefault())
        try {
            val sdf = SimpleDateFormat(CALENDAR_RECYCLERVIEW_FORMAT, Locale.ENGLISH)
            calendar.time = sdf.parse(date)!!
        } catch (_: ParseException) {
        }
        val monthCalendar: Calendar = calendar.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayofMonth: Int = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayofMonth)
        val dates = mutableListOf<Date>()
        for (i in 0 until maxCalenderDays) {
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dates
    }

    fun isMoreDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return if (cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR)) true else
            cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    }

    private fun isSameHour(hour1: Int, hour2: Int): Boolean = hour1 == hour2

    fun getIntervalDate(date: String): String {
        val calData = getCalendarFromString(
            DEFAULT_TIME_FORMAT,
            date
        )
        val calNow = Calendar.getInstance()

        calData?.let {
            if (isSameDay(it.time, calNow.time)) {
                val hourNow = calNow.get(Calendar.HOUR_OF_DAY)
                val hourData = it.get(Calendar.HOUR_OF_DAY)
                if (isSameHour(hourNow, hourData)) {
                    val minuteNow = calNow.get(Calendar.MINUTE)
                    val minuteData = it.get(Calendar.MINUTE)
                    val intervalMinute = abs(minuteNow - minuteData)
                    if (intervalMinute >= 1) {
                        return "$intervalMinute Minute ago"
                    }
                    return "Just Now"
                }
                val intervalHour = abs(hourNow - hourData)
                if (intervalHour < 23) {
                    return "$intervalHour Hour ago"
                }
                return "Today"
            }
            val dateNow = calNow.get(Calendar.DATE)
            val dateData = calData.get(Calendar.DATE)
            val intervalDate = abs(dateNow - dateData)
            if (intervalDate > Calendar.DAY_OF_WEEK) {
                return getDateStringWithFormat(
                    date,
                    DEFAULT_TIME_FORMAT,
                    CALENDAR_FULLSHOWN_FORMAT,
                    ordinalSuffix = true
                ) ?: "null"
            }
            if (intervalDate > 1) {
                return "$intervalDate Days ago"
            }
            return "Yesterday"
        }
        return "null"
    }

    fun toDMY(date: Date): String? {
        val format = SimpleDateFormat(
            DMY_FORMAT,
            Locale.getDefault()
        )
        return format.format(date)
    }

    fun dateToString(date: Date = Date(), into: String = SIMPLE_FORMAT): String? {
        val format = SimpleDateFormat(into,
            Locale.getDefault()
        )
        return format.format(date)
    }
}
package ru.maxpek.friendslinkup.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
object GoDataTime {
    fun convertDataTime(dateTime: String): String {
        return if (dateTime == ""){
            ""
        } else {
            val parsedDate = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
            return parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDataTimeJob(dateTime: String): String {
        return if (dateTime == ""){
            ""
        } else {
            val parsedDate = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
            return parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDataInput(date: List<Int>): String {
        val x = date[0]
        val y = date[1] + 1
        val z = date[2]
        val day = when (x) {
            in 1..9 -> "0$x"
            else -> {
                "$x"
            }
        }
        val month = when (y) {
            in 1..9 -> "0$y"
            else -> {
                "$y"
            }
        }
        val year = "$z"

//        val date1 = LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), 0, 0, 0, 0)
        val newDate = "$day.$month.$year"
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val date = formatter.parse(newDate)
//        println(date)

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'")
        val dateTime = sdf.format(date!!)

        return dateTime
        return ""
    }

    fun convertDateToLocalDate(dateTime: String): String {
        val formatter = DateTimeFormatter.ofPattern("mm.dd.yyyy, hh:mm:ss")
        return LocalDateTime.parse(dateTime, formatter).toString()
    }
}
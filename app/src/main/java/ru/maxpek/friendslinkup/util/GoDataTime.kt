package ru.maxpek.friendslinkup.util

import android.annotation.SuppressLint
import ru.maxpek.friendslinkup.fragment.year

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

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


    fun convertDataTimeJob(dateTime: String): String {
        return if (dateTime == ""){
            ""
        } else {
            val parsedDate = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
            parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }

    fun convertDataInput(date: List<Int>): String {
        val x = date[0]
        val y = date[1]+1
        val z = date[2]
        val day = when (x){
            in 1..9  -> "0$x"
            else -> {"$x"}
        }
        val month = when (y){
            in 1..9  -> "0$y"
            else -> {"$y"}
        }
        val year = "$z"


        val dateString = "Jan 01 2017, 07:34:27 pm"
        val newDate = "$month $day $year, 00:00:00 pm"
        val formatter = DateTimeFormatter.ofPattern("mm dd yyyy, hh:mm:ss a")
        val date = LocalDateTime.parse(dateString, formatter)

        println(date)        // 2017-01-01T19:34:27
//        LocalDateTime.parse(newDate, formatter).toString()


        return ""
    }

    fun convertDateToLocalDate(dateTime: String): String {
        val formatter = DateTimeFormatter.ofPattern("mm.dd.yyyy, hh:mm:ss")
        return LocalDateTime.parse(dateTime, formatter).toString()
    }
}
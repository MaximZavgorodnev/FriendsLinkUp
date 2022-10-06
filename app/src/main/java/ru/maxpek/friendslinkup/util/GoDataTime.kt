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

    fun convertDataInput(date: List<Int>): String{
        val x = date[0]
        val y = date[1]
        val z = date[2]
        val day = when (x){
            in 1..9  -> "0$x"
            else -> {"$x"}
        }


        val month: String
        val year: String
        return day
    }
}
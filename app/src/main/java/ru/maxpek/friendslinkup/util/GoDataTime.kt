package ru.maxpek.friendslinkup.util

import android.annotation.SuppressLint

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@SuppressLint("NewApi")
object GoDataTime {
    fun convertDataTime(dateTime: String): String {
        val parsedDate = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
        return parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    }


    fun convertDataTimeJob(dateTime: String): String {
        val parsedDate = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
        return parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}
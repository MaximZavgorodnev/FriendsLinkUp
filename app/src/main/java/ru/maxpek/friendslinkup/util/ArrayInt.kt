package ru.maxpek.friendslinkup.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object ArrayInt : ReadWriteProperty<Bundle, List<Int>?> {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): List<Int>? {
            val arrayInt = thisRef.getIntArray(property.name)
            return if (arrayInt!!.isNotEmpty()) arrayInt.toList() else{ null }

        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: List<Int>?) {
            val arrayInt = value?.toIntArray()
            thisRef.putIntArray(property.name, arrayInt)
        }
    }
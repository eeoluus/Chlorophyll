package com.example.chlorophyll.domain

import com.example.chlorophyll.viewmodels.Schedule
import com.example.chlorophyll.util.extensions.list.*
import java.util.*

fun todayEventNames(events: Schedule): List<String> {

    val today = events.keys.find {
        matchWith(it, Calendar.getInstance())
    }
    return if (today != null) {
        val names = events[today]?.map {
            it.name
        }
        names?.run {
            try {
                requireNoNulls()
            } catch (e: IllegalArgumentException) {
                emptyList()
            }
        } ?: emptyList()
    } else {
        emptyList()
    }
}

fun formatContentText(names: List<String>): String {

    with(names) {
        return when (size) {
            1 -> "$first needs watering"
            2 -> "$first and $second need watering"
            3 -> "$first, $second and $third need watering"
            else -> "$first, $second and ${size - 2} others need watering"
        }
    }
}
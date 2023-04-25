package com.example.chlorophyll.domain

import com.example.chlorophyll.data.Plant
import com.example.chlorophyll.util.extensions.calendar.day
import com.example.chlorophyll.util.extensions.calendar.month
import com.example.chlorophyll.util.extensions.calendar.year
import com.example.chlorophyll.viewmodels.Event
import com.example.chlorophyll.viewmodels.Schedule
import java.util.*

fun eventsFromPlants(plants: List<Plant>): Schedule {

    val events: Schedule = mutableMapOf()

    fun schedule(event: Event) {
        val key = events.keys.find {
            matchWith(it, event.date)
        }
        if (key == null) {
            events[event.date] = mutableListOf(event)
        } else {
            events[key]?.add(event)
        }
    }

    for (plant in plants) {
        val sequence = generateSequence(plant) {
            with(it) {
                val date = (date.clone() as Calendar).apply {
                    add(Calendar.DAY_OF_MONTH, interval)
                }
                copy(date = date)
            }
        }
        sequence.take(5).forEach(::schedule)
    }
    return events
}

fun matchWith(date: Calendar, other: Calendar): Boolean {
    return (date.year == other.year &&
            date.month == other.month &&
            date.day == other.day)
}

/* fun resolveIds(date: Calendar, schedule: Schedule): DayEvents? {
    return schedule.keys.find { matchWith(it, date) }?.let {
        schedule[it]
    }
} */
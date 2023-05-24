package com.example.chlorophyll.domain

import com.example.chlorophyll.data.Plant
import com.example.chlorophyll.util.extensions.calendar.day
import com.example.chlorophyll.util.extensions.calendar.month
import com.example.chlorophyll.util.extensions.calendar.year
import com.example.chlorophyll.viewmodels.Event
import com.example.chlorophyll.viewmodels.Schedule
import com.example.chlorophyll.viewmodels.Window
import java.time.temporal.ChronoUnit
import java.util.*

fun eventsFromPlants(plants: List<Plant>, window: Window): Schedule {

    val events: Schedule = mutableMapOf()

    val (beginning, end) = window

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

    fun firstEvent(plant: Plant): Event {

        if (plant.date.before(beginning)) {

            val origin = plant.date.toInstant()

            val distance = ChronoUnit.DAYS.between(
                origin,
                beginning.toInstant()
            )
            val offset = (distance % plant.interval).toInt()

            return plant.copy(
                date = (beginning.clone() as Calendar).apply {
                    add(Calendar.DAY_OF_MONTH, plant.interval - offset)
                }
            )
        }
        return plant
    }

    for (plant in plants) {

        val event = firstEvent(plant)

        val sequence = generateSequence(event) {
            with(it) {
                val date = (date.clone() as Calendar).apply {
                    add(Calendar.DAY_OF_MONTH, interval)
                }
                copy(date = date)
            }
        }
        sequence.takeWhile {
            it.date.before(end)
        }.forEach {
            schedule(it)
        }
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
package com.example.chlorophyll.util.extensions.calendar

import com.example.chlorophyll.viewmodels.Window
import java.util.Calendar

val Calendar.year: Int
    get() = get(Calendar.YEAR)

val Calendar.month: Int
    get() = get(Calendar.MONTH)

val Calendar.day: Int
    get() = get(Calendar.DAY_OF_MONTH)

fun Calendar.eventWindow(visibleHalf: Int): Window {

    val beginning = (this.clone() as Calendar).apply {
        add(Calendar.MONTH, - visibleHalf)
        set(Calendar.DAY_OF_MONTH, 0)
        add(Calendar.DAY_OF_MONTH, - 56)
    }
    val end = (this.clone() as Calendar).apply {
        add(Calendar.MONTH, visibleHalf)
        set(Calendar.DAY_OF_MONTH, 31)
        add(Calendar.DAY_OF_MONTH, 56)
    }
    return beginning to end
}

package com.example.chlorophyll.data

import androidx.room.TypeConverter
import java.util.Calendar

class Converters {
    @TypeConverter
    fun fromTimeInMillis(time: Long?): Calendar? {
        return time?.let {
            Calendar.getInstance().apply {
                timeInMillis = it
            }
        }
    }
    @TypeConverter
    fun timeInMillisToCalendar(calendar: Calendar?): Long? {
        return calendar?.timeInMillis
    }
}
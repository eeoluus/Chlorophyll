package com.example.chlorophyll.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.chlorophyll.AlarmReceiver
import java.util.*

class AlarmScheduler(private val context: Context) {

    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    private fun getPendingIntent(): PendingIntent {

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM_BROADCAST"
        }
        val requestCode = 0
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
    fun schedule(calendar: Calendar) {
        alarmManager?.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            getPendingIntent()
        )
    }
    fun cancel() {
        alarmManager?.cancel(
            getPendingIntent()
        )
    }
}


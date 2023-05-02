package com.example.chlorophyll

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.chlorophyll.data.PlantDatabase
import com.example.chlorophyll.data.SettingsDataStore
import com.example.chlorophyll.domain.AlarmScheduler
import com.example.chlorophyll.util.CHANNEL_ID
import kotlinx.coroutines.runBlocking
import java.util.*

class ChlorophyllApplication : Application() {

    val database: PlantDatabase by lazy {
        PlantDatabase.getInstance(this)
    }

    private suspend fun ifNotNotificationsSet() {
        with(SettingsDataStore(this)) {
            if (!existActiveReminders()) {
                val (hourOfDay, minute) = retrieveReminderTime()
                val checkTime = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }
                AlarmScheduler(this@ChlorophyllApplication).schedule(checkTime)
                markRemindersAsActive()
            }
        }
    }
    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        runBlocking {
            ifNotNotificationsSet()
        }
    }
}
package com.example.chlorophyll

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.chlorophyll.data.PlantDatabase
import com.example.chlorophyll.domain.AlarmScheduler
import com.example.chlorophyll.util.CHANNEL_ID
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.util.*

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ChlorophyllApplication : Application() {

    companion object {
        val REMINDERS_ACTIVE = booleanPreferencesKey("reminders_active")
    }
    val database: PlantDatabase by lazy {
        PlantDatabase.getInstance(this)
    }
    private suspend fun existActiveReminders(): Boolean {
        return this.dataStore.data.map {
            it[REMINDERS_ACTIVE] ?: false
        }.first()
    }
    private suspend fun markRemindersAsActive() {
        this.dataStore.edit {
            it[REMINDERS_ACTIVE] = true
        }
    }
    private suspend fun checkIfNotifications() {
        if (!existActiveReminders()) {
            with(AlarmScheduler(this)) {
                val checkTime = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 18)
                    set(Calendar.MINUTE, 20)
                }
                schedule(checkTime)
            }
            markRemindersAsActive()
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
            checkIfNotifications()
        }
    }
}
package com.example.chlorophyll.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.chlorophyll.util.DEFAULT_REMINDERS_ACTIVE
import com.example.chlorophyll.util.DEFAULT_REMINDER_HOUR_OF_DAY
import com.example.chlorophyll.util.DEFAULT_REMINDER_MINUTE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


private const val PREFERENCES_NAME = "settings"

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_NAME
)
class SettingsDataStore(private val context: Context) {

    companion object {
        val REMINDERS_ACTIVE = booleanPreferencesKey("reminders_active")
        val REMINDER_HOUR_OF_DAY = intPreferencesKey("reminder_hour_of_day")
        val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
    }
    suspend fun existActiveReminders(): Boolean {
        val flow = context.dataStore.data.map {
            it[REMINDERS_ACTIVE] ?: DEFAULT_REMINDERS_ACTIVE
        }
        return flow.first()
    }
    suspend fun retrieveReminderTime(): List<Int> {
        val flow = context.dataStore.data.map {
            listOf(
                it[REMINDER_HOUR_OF_DAY] ?: DEFAULT_REMINDER_HOUR_OF_DAY,
                it[REMINDER_MINUTE] ?: DEFAULT_REMINDER_MINUTE
            )
        }
        return flow.first()
    }
    suspend fun setReminderTimePreferences(hourOfDay: Int, minute: Int) {
        context.dataStore.edit {
            it[REMINDER_HOUR_OF_DAY] = hourOfDay
            it[REMINDER_MINUTE] = minute
        }
    }
    suspend fun markRemindersAsActive() {
        context.dataStore.edit {
            it[REMINDERS_ACTIVE] = true
        }
    }
}
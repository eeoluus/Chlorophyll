package com.example.chlorophyll.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.chlorophyll.util.DEFAULT_CALENDAR_HALF_WIDTH
import com.example.chlorophyll.util.DEFAULT_REMINDERS_ACTIVE
import com.example.chlorophyll.util.DEFAULT_REMINDER_HOUR_OF_DAY
import com.example.chlorophyll.util.DEFAULT_REMINDER_MINUTE
import kotlinx.coroutines.flow.Flow
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
        val CALENDAR_HALF_WIDTH = intPreferencesKey("calendar_half_width")
    }
    private val data = context.dataStore.data

    val windowPreference = data.map { preferences ->
        preferences[CALENDAR_HALF_WIDTH] ?: DEFAULT_CALENDAR_HALF_WIDTH
    }
    val remindersActivePreference = data.map { preferences ->
        preferences[REMINDERS_ACTIVE] ?: DEFAULT_REMINDERS_ACTIVE
    }
    val reminderTimePreference = data.map { preferences ->
        listOf(preferences[REMINDER_HOUR_OF_DAY] ?: DEFAULT_REMINDER_HOUR_OF_DAY,
               preferences[REMINDER_MINUTE] ?: DEFAULT_REMINDER_MINUTE)
    }
    suspend fun setReminderTimePreference(hourOfDay: Int, minute: Int) {
        context.dataStore.edit {
            it[REMINDER_HOUR_OF_DAY] = hourOfDay
            it[REMINDER_MINUTE] = minute
        }
    }
    suspend fun setWindowPreference(halfWidth: Int) {
        context.dataStore.edit {
            it[CALENDAR_HALF_WIDTH] = halfWidth
        }
    }
    suspend fun markRemindersAsActive() {
        context.dataStore.edit {
            it[REMINDERS_ACTIVE] = true
        }
    }
}
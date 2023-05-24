package com.example.chlorophyll

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.chlorophyll.data.SettingsDataStore
import com.example.chlorophyll.domain.AlarmScheduler
import com.example.chlorophyll.util.extensions.broadcastreceiver.goAsync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import java.util.*

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) = goAsync(Dispatchers.IO) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            val (hourOfDay, minute) =
                SettingsDataStore(context).reminderTimePreference.first()

            with(AlarmScheduler(context)) {
                val checkTime = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }
                schedule(checkTime)
            }
        }
    }
}
package com.example.chlorophyll

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.chlorophyll.data.PlantDatabase
import com.example.chlorophyll.data.SettingsDataStore
import com.example.chlorophyll.domain.AlarmScheduler
import com.example.chlorophyll.domain.eventsFromPlants
import com.example.chlorophyll.domain.formatContentText
import com.example.chlorophyll.domain.todayEventNames
import com.example.chlorophyll.ui.MainActivity
import com.example.chlorophyll.util.*
import com.example.chlorophyll.util.extensions.broadcastreceiver.goAsync
import com.example.chlorophyll.util.extensions.calendar.eventWindow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) = goAsync(Dispatchers.IO) {

        val events = eventsFromPlants(
            PlantDatabase
                .getInstance(context)
                .plantDao
                .getAll()
                .first(),
            Calendar
                .getInstance()
                .eventWindow(SettingsDataStore(context)
                    .windowPreference
                    .first()
                )
        )
        val names = todayEventNames(events)

        if (names.isNotEmpty()) {

            val contentText = formatContentText(names)
            val intentToMainActivity = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val requestCode = 0
            val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                intentToMainActivity,
                PendingIntent.FLAG_IMMUTABLE
            )
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_potted_plant)
                .setContentTitle(context.getString(R.string.watering_notification_title))
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) notify(requestCode, builder.build())
            }
        }
        with(AlarmScheduler(context.applicationContext)) {
            val nextCheckTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, DEFAULT_REMINDER_HOUR_OF_DAY)
                set(Calendar.MINUTE, DEFAULT_REMINDER_MINUTE)
                add(Calendar.HOUR_OF_DAY, 24)
            }
            schedule(nextCheckTime)
        }
    }
}
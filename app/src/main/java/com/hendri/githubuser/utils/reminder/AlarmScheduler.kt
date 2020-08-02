package com.hendri.githubuser.utils.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import com.hendri.githubuser.R
import java.time.*

object AlarmScheduler {
    private const val DAILY_ALARM_ID = 128

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleAlarm(context: Context) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)

        //get local time
        val alarmTime = LocalTime.parse(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.reminder_time_key), "09:00")
        ) ?: LocalTime.of(9, 0)

        val zoneDateTime = ZonedDateTime.of(
            LocalDateTime.of(LocalDate.now(), alarmTime),
            ZoneId.systemDefault()
        )

        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_ALARM_ID, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            zoneDateTime.toInstant().toEpochMilli(),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_ALARM_ID, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }
}
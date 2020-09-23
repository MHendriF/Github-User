package com.hendri.githubuser.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.hendri.githubuser.R
import com.hendri.githubuser.ui.main.view.activity.MainActivity
import com.hendri.githubuser.utils.reminder.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    //Content intent
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Styling notification
    val githubImg = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.github_logo
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(githubImg)
        .bigLargeIcon(null)

    // Snooze action
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS
    )

    // Build notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.github_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_notifications)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // Set style
        .setStyle(bigPicStyle)
        .setLargeIcon(githubImg)

        // Add snooze action
        .addAction(
            R.drawable.github_logo,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )

        // Set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
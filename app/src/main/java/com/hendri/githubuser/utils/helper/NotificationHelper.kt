package com.hendri.githubuser.utils.helper


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.hendri.githubuser.R
import com.hendri.githubuser.ui.main.view.activity.MainActivity


object NotificationHelper {
    data class NotificationData(
        val notificationId: Int,
        val title: String,
        val message: String,
        val type: String
    )

    fun createChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = context.getString(R.string.channel_name, name)
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(
        context: Context,
        data: NotificationData
    ): NotificationCompat.Builder {
        val channelId = context.getString(R.string.channel_name, data.type)

        return NotificationCompat.Builder(context, channelId).apply {
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setDefaults(NotificationCompat.DEFAULT_ALL)

            //content
            setSmallIcon(R.drawable.ic_notifications)
            setContentTitle(data.title)
            setContentText(data.message)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = data.type
            }
            val pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            setContentIntent(pendingIntent)
        }
    }

    fun showNotification(
        context: Context,
        data: NotificationData
    ) {
        val notificationBuilder = buildNotification(context, data)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(data.notificationId, notificationBuilder.build())
    }

    fun showAlarmNotification(
        context: Context,
        data: NotificationData
    ) {
        val channelId = context.getString(R.string.channel_id)
        val channelName = context.getString(R.string.channel_name)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationIntent = Intent(context,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(data.title)
            .setContentText(data.message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = builder.build()
        notificationManager.notify(data.notificationId, notification)
    }

}
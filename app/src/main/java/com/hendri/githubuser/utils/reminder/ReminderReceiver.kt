package com.hendri.githubuser.utils.reminder

import com.hendri.githubuser.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.hendri.githubuser.ui.main.view.activity.MainActivity
import com.hendri.githubuser.utils.helper.NotificationHelper


class ReminderReceiver : BroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_ID = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        showAlarmNotification(context)
    }

    private fun showAlarmNotification(context: Context){
        val data = NotificationHelper.NotificationData(
            NOTIFICATION_ID,
            context.getString(R.string.notification_title),
            context.getString(R.string.notification_message),
            context.getString(R.string.notification_type)
        )
        //NotificationHelper.showNotification(context, data)
        NotificationHelper.showAlarmNotification(context, data)
    }
}

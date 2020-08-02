package com.hendri.githubuser.utils.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.hendri.githubuser.R
import com.hendri.githubuser.ui.main.view.activity.MainActivity

class FavoriteUsersAppWidget : AppWidgetProvider() {

    companion object {

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            // Intent load data from database
            val intent = Intent(context, StackWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = toUri(Intent.URI_INTENT_SCHEME).toUri()
            }

            // Intent to MainActivity
            val tittleIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val tittlePendingIntent = PendingIntent.getActivity(context, 0, tittleIntent, 0)

            val views = RemoteViews(
                context.packageName,
                R.layout.favorite_users_app_widget
            ).apply {
                setRemoteAdapter(R.id.stackView, intent)
                setEmptyView(R.id.stackView, R.id.emptyView)
                setOnClickPendingIntent(R.id.tvTitleWidget, tittlePendingIntent)
            }

            //update app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
                // refresh all your widgets
                val component = context?.let { context ->
                    ComponentName(
                        context,
                        FavoriteUsersAppWidget::class.java
                    )
                }
                AppWidgetManager.getInstance(context).apply {
                    notifyAppWidgetViewDataChanged(
                        getAppWidgetIds(component),
                        R.id.ivStackWidget
                    )
                }
            }
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
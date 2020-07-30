package com.hendri.githubuser.utils.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.hendri.githubuser.R
import com.hendri.githubuser.ui.main.view.activity.MainActivity

/**
 * Implementation of App Widget functionality.
 */
class FavoriteUsersAppWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_TOUCH = "ACTION_TOUCH"
        const val EXTRA_ITEM = "EXTRA_ITEM"
        const val WIDGET_PENDING_INTENT_CODE = 32
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null)
            if (intent.action == ACTION_TOUCH) {
                Intent(context, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context?.startActivity(it)
                }
            }
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

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val intent = Intent(context, StackWidgetService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

    val touchIntent = Intent(context, FavoriteUsersAppWidget::class.java).apply {
        action = FavoriteUsersAppWidget.ACTION_TOUCH
        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
    }

    val views = RemoteViews(
        context.packageName,
        R.layout.favorite_users_app_widget
    )
    views.setRemoteAdapter(R.id.stackView, intent)
    views.setEmptyView(R.id.stackView, R.id.emptyView)
    views.setPendingIntentTemplate(
        R.id.ivStackWidget,
        PendingIntent.getBroadcast(
            context,
            FavoriteUsersAppWidget.WIDGET_PENDING_INTENT_CODE,
            touchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    )

    //update app widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}


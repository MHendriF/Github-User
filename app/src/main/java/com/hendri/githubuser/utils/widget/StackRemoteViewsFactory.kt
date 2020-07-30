package com.hendri.githubuser.utils.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hendri.githubuser.R
import com.hendri.githubuser.data.local.DatabaseBuilder
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.local.DatabaseHelperImp
import com.hendri.githubuser.data.model.User

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate() {
        dbHelper = DatabaseHelperImp(DatabaseBuilder.getInstance(context))
    }

    override fun getViewAt(p0: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.item_widget)
        val widgetItems = getFavoriteUsers()

        //image loading
        Glide.with(context)
            .asBitmap()
            .load(widgetItems[p0].avatar_url)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.no_internet_dino)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    remoteViews.setImageViewBitmap(R.id.ivStackWidget, resource)
                }

                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    remoteViews.setImageViewResource(
                        R.id.ivStackWidget,
                        R.drawable.noimage
                    )
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    remoteViews.setImageViewResource(
                        R.id.ivStackWidget,
                        R.drawable.no_internet_dino
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    remoteViews.setImageViewResource(
                        R.id.ivStackWidget,
                        R.drawable.noimage
                    )
                }
            })

        //fill intent
        val fillIntent = Intent()
        fillIntent.putExtra("widgetFill", p0)
        remoteViews.setOnClickFillInIntent(R.id.ivStackWidget, fillIntent)

        return remoteViews
    }

    private fun getFavoriteUsers(): List<User> {
        val users = ArrayList<User>()

        val cursor = dbHelper.getAllUsersAsCursor()
        cursor?.let {
            while (cursor.moveToNext()) {
                User(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(User.COLUMN_ID)),
                    login = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_LOGIN)),
                    avatar_url = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_AVATAR))
                ).also {
                    users.add(it)
                }
            }
            cursor.close()
        }

        return users
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun onDataSetChanged() {}

    override fun hasStableIds(): Boolean = false

    override fun getCount(): Int = getFavoriteUsers().size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}
}
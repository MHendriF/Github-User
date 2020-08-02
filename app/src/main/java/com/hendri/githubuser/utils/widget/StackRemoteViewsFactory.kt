package com.hendri.githubuser.utils.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
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
    private lateinit var users: List<User>

    override fun onCreate() {
        dbHelper = DatabaseHelperImp(DatabaseBuilder.getInstance(context.applicationContext))
    }

    override fun onDataSetChanged() {
        users = getUsers()
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.item_widget)

        if (!users.isNullOrEmpty()) {
            remoteViews.apply {
                users[position].apply {
                    setImageViewBitmap(
                        R.id.ivStackWidget, avatar_url?.toBitmap(context)
                    )
                    setTextViewText(
                        R.id.tvUsername, login ?: login
                    )
                }
            }
        }
        return remoteViews
    }

    private fun String.toBitmap(context: Context): Bitmap {
        var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.noimage)

        val option = RequestOptions()
            .error(R.drawable.noimage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        try {
            Glide.with(context)
                .setDefaultRequestOptions(option)
                .asBitmap()
                .load(this)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        bitmap = resource
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun getUsers(): List<User> = dbHelper.getUsers()

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun getCount(): Int = users.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}
}
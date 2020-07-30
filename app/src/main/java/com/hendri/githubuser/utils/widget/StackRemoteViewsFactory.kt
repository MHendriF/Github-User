package com.hendri.githubuser.utils.widget

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.net.toUri
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
import com.hendri.githubuser.data.provider.UserContentProvider
import com.hendri.githubuser.data.source.UserDataSource
import com.hendri.githubuser.utils.Constants
import com.hendri.githubuser.utils.USER_CONTENT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var users: List<User>
    private lateinit var dataSource: UserDataSource
    private var TAG: String = "Trace"

    override fun onCreate() {
        dbHelper = DatabaseHelperImp(DatabaseBuilder.getInstance(context))
    }

    override fun onDataSetChanged() {
        users = getFavoriteUsers()
        //dataSource = UserDataSource(context.contentResolver)
        //users = getFavoriteUser()
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.item_widget)

        if (!users.isNullOrEmpty()) {
            // Set item stack view
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

    suspend fun getFavoriteUser() : List<User>{
        return withContext(Dispatchers.IO){
            dataSource.fetchUsers()
        }
    }

    private fun getFavoriteUsers(): List<User> {
        val users = ArrayList<User>()
        val cursor = dbHelper.getAllUsersAsCursor()
        //val cursor = context.contentResolver?.query(USER_CONTENT_URI.toUri(), null, null, null, null)
        //val cursor = context.contentResolver?.query(Constants.CONTENT_URI, null, null, null, null)
        cursor?.let {
            while (cursor.moveToNext()) {
                User(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(User.COLUMN_ID)),
                    login = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_LOGIN)),
                    avatar_url = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_AVATAR)),
                    html_url = cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_HTML_URL))
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

    override fun hasStableIds(): Boolean = false

    override fun getCount(): Int = users.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}
}
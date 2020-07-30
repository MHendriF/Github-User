package com.hendri.githubuser.data.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.hendri.githubuser.data.local.AppDatabase
import com.hendri.githubuser.data.local.DatabaseBuilder
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.local.DatabaseHelperImp
import com.hendri.githubuser.data.local.dao.UserDao
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.utils.DATABASE_AUTHORITY
import com.hendri.githubuser.utils.USER_TABLE_NAME
import com.hendri.githubuser.utils.widget.FavoriteUsersAppWidget

class UserContentProvider : ContentProvider() {

    private lateinit var mContext: Context

    companion object {
        private const val USER = 1
        private const val USER_ID = 2

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var dbHelper: DatabaseHelper

        init {
            // content://com.hendri.github/users
            MATCHER.addURI(DATABASE_AUTHORITY, USER_TABLE_NAME, USER)

            // content://com.hendri.github/users/:id
            MATCHER.addURI(DATABASE_AUTHORITY, "$USER_TABLE_NAME/#", USER_ID)
        }
    }


    override fun onCreate(): Boolean {
        mContext = context as Context
        dbHelper = DatabaseHelperImp(DatabaseBuilder.getInstance(mContext.applicationContext))
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (MATCHER.match(uri)) {
            USER -> dbHelper.getAllUsersAsCursor()
            USER_ID -> dbHelper.getUserAsCursor((ContentUris.parseId(uri)))
            else -> null
        }.apply {
            this?.setNotificationUri(mContext.contentResolver, uri)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (MATCHER.match(uri)) {
            USER -> {
                val id: Long = dbHelper.insertUser(User.fromContentValues(values))
                mContext.contentResolver.notifyChange(uri, null)
                ContentUris.withAppendedId(uri, id)
            }
            USER_ID -> {
                throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return when (MATCHER.match(uri)) {
            USER -> {
                throw IllegalArgumentException("Invalid URI, cannot update without ID: $uri")
            }
            USER_ID -> {
                val id: Int = dbHelper.updateUser(User.fromContentValues(values))
                mContext.contentResolver.notifyChange(uri, null)
                id
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (MATCHER.match(uri)) {
            USER -> {
                throw IllegalArgumentException("Invalid URI, cannot update without ID: $uri")
            }
            USER_ID -> {
                val id: Int = dbHelper.deleteUserById(ContentUris.parseId(uri))
                mContext.contentResolver.notifyChange(uri, null)
                id
            }
            else -> {
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    private fun refreshWidgetUser() {
        // Refresh data in UserWidget
        FavoriteUsersAppWidget.sendRefreshBroadcast(mContext.applicationContext)
    }

}

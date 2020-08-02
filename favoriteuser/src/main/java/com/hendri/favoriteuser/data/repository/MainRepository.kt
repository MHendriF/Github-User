package com.hendri.favoriteuser.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.hendri.favoriteuser.data.model.User
import com.hendri.favoriteuser.utils.Constants
import com.hendri.favoriteuser.utils.USER_CONTENT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(
    private val context: Context
) {

    suspend fun getFavoriteUsers() : List<User>{
        return withContext(Dispatchers.IO){
            getUserCursorAsModel()
        }
    }

    private fun getUserCursorAsModel(): List<User> {
        val users: MutableList<User> = mutableListOf()
        val cursor = context.contentResolver?.query(USER_CONTENT_URI.toUri(), null, null, null, null)
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
        return users.toList()
    }

}
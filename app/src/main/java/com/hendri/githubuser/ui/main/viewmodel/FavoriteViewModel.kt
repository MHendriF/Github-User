package com.hendri.githubuser.ui.main.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.utils.Resource
import com.hendri.githubuser.utils.USER_CONTENT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val dbHelper: DatabaseHelper,
    private val context: Context
) : ViewModel() {

    fun fetchUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = getUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun deleteUserById(user: User) = viewModelScope.launch(Dispatchers.IO) {
        val uri = "$USER_CONTENT_URI/${user.id}".toUri()
        context.contentResolver.delete(uri, null, null)
    }

    private fun getUsers(): List<User> {
        val users = ArrayList<User>()
        val cursor = context.contentResolver?.query(USER_CONTENT_URI.toUri(), null, null, null, null)
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
}
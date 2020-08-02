package com.hendri.githubuser.data.repository

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.response.UserResponse
import com.hendri.githubuser.utils.USER_CONTENT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

class MainRepository(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val context: Context
) {

    suspend fun searchUsers(username: String?) : Response<UserResponse<User>> {
        Timber.d("Repo :: searchUsers($username)")
        return withContext(Dispatchers.IO){
            apiHelper.searchUsers(username)
        }
    }

    suspend fun getUser(username: String?) : User {
        Timber.d("Repo :: getUser($username)")
        return withContext(Dispatchers.IO){
            apiHelper.detailUser(username)
        }
    }

    suspend fun getFollowers(username: String?) : List<User>{
        Timber.d("Repo :: getFollowers($username)")
        return withContext(Dispatchers.IO){
            apiHelper.getFollowers(username)
        }
    }

    suspend fun getFollowing(username: String?) : List<User>{
        Timber.d("Repo :: getFollowing($username)")
        return withContext(Dispatchers.IO){
            apiHelper.getFollowing(username)
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAllUsers() : LiveData<List<User>>{
        return dbHelper.getAllUsers()
    }

    suspend fun insertUser(user: User) {
        Timber.d("Repo :: insertUser($user)")
        withContext(Dispatchers.IO) {
            context.contentResolver.insert(USER_CONTENT_URI.toUri(), User.toContentValues(user))
        }
    }

    suspend fun deleteUserById(user: User) {
        Timber.d("Repo :: deleteUserById($user)")
        withContext(Dispatchers.IO) {
            val uri = "$USER_CONTENT_URI/${user.id}".toUri()
            context.contentResolver.delete(uri, null, null)
        }
    }

    suspend fun getFavoriteUsers() : List<User>{
        return withContext(Dispatchers.IO){
            getUserCursorAsModel()
        }
    }

    private fun getUserCursorAsModel(): List<User> {
        val users: MutableList<User> = mutableListOf()
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
        return users.toList()
    }

}
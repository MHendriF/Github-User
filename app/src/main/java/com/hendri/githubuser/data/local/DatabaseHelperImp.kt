package com.hendri.githubuser.data.local

import android.database.Cursor
import androidx.lifecycle.LiveData
import com.hendri.githubuser.data.model.User

class DatabaseHelperImp(private val appDatabase: AppDatabase) : DatabaseHelper {

    override fun getUsers(): List<User> = appDatabase.userDao().getUsers()

    override fun getAllUsersAsCursor(): Cursor? = appDatabase.userDao().getAllUsersAsCursor()

    override fun getUserAsCursor(id: Long?): Cursor? = appDatabase.userDao().getUserAsCursor(id)

    override fun insertUser(user: User): Long = appDatabase.userDao().insertUser(user)

    override fun updateUser(user: User): Int = appDatabase.userDao().updateUser(user)

    override fun deleteUser(user: User): Int = appDatabase.userDao().deleteUser(user)

    override fun deleteUserById(id: Long?): Int = appDatabase.userDao().deleteUserById(id)

    override fun getAllUsers(): LiveData<List<User>> = appDatabase.userDao().getAllUsers()
}
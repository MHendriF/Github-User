package com.hendri.githubuser.data.local

import androidx.lifecycle.LiveData
import com.hendri.githubuser.data.model.User

class DatabaseHelperImp(private val appDatabase: AppDatabase) : DatabaseHelper {

    override suspend fun getUsers(): LiveData<List<User>> = appDatabase.userDao().getUsers()

    override suspend fun getUsers2(): List<User> = appDatabase.userDao().getUsers2()

    override suspend fun insert(user: User) = appDatabase.userDao().insert(user)

    override suspend fun delete(user: User) = appDatabase.userDao().delete(user)

    override suspend fun getUserDetails(username: String?): User? = appDatabase.userDao().getUserDetails(username)

    override suspend fun insertAll(users: List<User>) {}

    override suspend fun deleteAll() = appDatabase.userDao().deleteAll()

}
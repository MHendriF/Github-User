package com.hendri.githubuser.data.local

import android.database.Cursor
import androidx.lifecycle.LiveData
import com.hendri.githubuser.data.model.User

interface DatabaseHelper {

    suspend fun getUsers(): LiveData<List<User>>

    suspend fun getUsers2(): List<User>

    suspend fun insert(user: User)

    suspend fun delete(user: User)

    suspend fun getUserDetails(username: String?): User?

    suspend fun insertAll(users: List<User>)

    suspend fun deleteAll()

    fun getAllUsersAsCursor(): Cursor?

    fun insertUser(user: User): Long

    fun updateUser(user: User): Int

    fun deleteUser(user: User): Int

    fun deleteUserById(id: Long?): Int
}
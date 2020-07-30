package com.hendri.githubuser.data.local

import android.database.Cursor
import androidx.lifecycle.LiveData
import com.hendri.githubuser.data.model.User

interface DatabaseHelper {

    fun getUsers(): List<User>

    fun getAllUsersAsCursor(): Cursor?

    fun getUserAsCursor(id: Long?): Cursor?

    fun insertUser(user: User): Long

    fun updateUser(user: User): Int

    fun deleteUser(user: User): Int

    fun deleteUserById(id: Long?): Int
}
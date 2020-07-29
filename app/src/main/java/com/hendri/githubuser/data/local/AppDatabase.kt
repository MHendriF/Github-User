package com.hendri.githubuser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hendri.githubuser.data.local.dao.UserDao
import com.hendri.githubuser.data.model.User

@Database(entities = [User::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
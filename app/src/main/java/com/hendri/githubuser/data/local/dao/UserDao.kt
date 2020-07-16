package com.hendri.githubuser.data.local.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.hendri.githubuser.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("SELECT * from user_table ORDER BY login ASC")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT * from user_table ORDER BY login ASC")
    fun getUsers2(): List<User>

    @Query("SELECT * from user_table WHERE login = :username")
    fun getUserDetails(username: String?): User?

    @Query("SELECT * from user_table ORDER BY login ASC")
    fun getUsersProvider(): Cursor

    @Query("SELECT * from user_table ORDER BY login ASC")
    fun getWidgets(): List<User>
}
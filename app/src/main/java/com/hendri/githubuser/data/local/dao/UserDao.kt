package com.hendri.githubuser.data.local.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.hendri.githubuser.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user : User) : Long

    @Update
    fun updateUser(user : User) : Int

    @Delete
    fun deleteUser(user : User) : Int

    @Query("SELECT * from users ORDER BY login ASC")
    fun getUsers(): List<User>

    @Query("SELECT * from users ORDER BY login ASC")
    fun getAllUsersAsCursor() : Cursor?

    @Query("SELECT * from users WHERE id = :id")
    fun getUserAsCursor(id : Long?) : Cursor?

    @Query("DELETE from users WHERE id = :id")
    fun deleteUserById(id: Long?): Int
}
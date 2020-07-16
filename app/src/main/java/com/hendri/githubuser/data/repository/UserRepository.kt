package com.hendri.githubuser.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hendri.githubuser.data.local.dao.UserDao
import com.hendri.githubuser.data.model.User

class UserRepository(private val userDao: UserDao) {

    private val favorite: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun insert(user: User) {
        userDao.insert(user)
        favorite.value = true
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
        favorite.value = false
    }

    fun getFavoriteUsers(userDao: UserDao) = userDao.getUsers()

    val isFavorite: LiveData<Boolean> = favorite
}
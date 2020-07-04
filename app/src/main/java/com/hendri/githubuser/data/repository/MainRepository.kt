package com.hendri.githubuser.data.repository

import com.hendri.githubuser.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getUsers() = apiHelper.getUsers()

    suspend fun searchUsers() = apiHelper.searchUsers()
}
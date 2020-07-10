package com.hendri.githubuser.data.repository

import com.hendri.githubuser.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getUsers() = apiHelper.getUsers()

    suspend fun searchUsers(username: String?) = apiHelper.searchUsers(username)

    suspend fun detailUser(username: String?) = apiHelper.detailUser(username)

    suspend fun getFollowing(username: String?) = apiHelper.getFollowing(username)

    suspend fun getFollowers(username: String?) = apiHelper.getFollowers(username)
}
package com.hendri.githubuser.data.repository

import com.hendri.githubuser.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun searchUsers() = apiHelper.searchUsers()

    suspend fun detailUser(username: String?) = apiHelper.detailUser(username)

    suspend fun getFollowing(username: String?) = apiHelper.getFollowing(username)

    suspend fun getFollowers(username: String?) = apiHelper.getFollowers(username)
}
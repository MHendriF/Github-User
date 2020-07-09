package com.hendri.githubuser.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun searchUsers() = apiService.searchUsers()

    suspend fun detailUser(username: String?) = apiService.detailUser(username)

    suspend fun getFollowing(username: String?) = apiService.getFollowing(username)

    suspend fun getFollowers(username: String?) = apiService.getFollowers(username)
}
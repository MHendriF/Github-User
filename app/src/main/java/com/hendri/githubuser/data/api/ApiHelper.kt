package com.hendri.githubuser.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun getUsers() = apiService.getUsers()

    suspend fun searchUsers() = apiService.searchUsers()

    suspend fun detailUser() = apiService.detailUser()

    suspend fun getFollowing() = apiService.getFollowing()

    suspend fun getFollowers() = apiService.getFollowers()

}
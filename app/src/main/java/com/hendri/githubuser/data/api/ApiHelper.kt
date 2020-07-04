package com.hendri.githubuser.data.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun getUsers() = apiService.getUsers()

    suspend fun searchUsers() = apiService.searchUsers()
}
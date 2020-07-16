package com.hendri.githubuser.data.api

import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.response.UserResponse
import retrofit2.Response

class ApiHelperImp(private val apiService: ApiService) : ApiHelper {
    override suspend fun searchUsers(username: String?): Response<UserResponse<User>> = apiService.searchUsers(username)

    override suspend fun detailUser(username: String?): User = apiService.detailUser(username)

    override suspend fun getFollowing(username: String?): List<User> = apiService.getFollowing(username)

    override suspend fun getFollowers(username: String?): List<User> = apiService.getFollowers(username)
}
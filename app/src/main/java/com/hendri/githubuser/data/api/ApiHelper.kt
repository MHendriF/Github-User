package com.hendri.githubuser.data.api

import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.response.UserResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun searchUsers(username: String?) : Response<UserResponse<User>>

    suspend fun detailUser(username: String?) : User

    suspend fun getFollowing(username: String?) : List<User>

    suspend fun getFollowers(username: String?) : List<User>
}
package com.hendri.githubuser.data.api

import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("search/users?q=he")
    @Headers("Authorization: token a1fde36ef3613320c0e0f935440bb9c3be25e4f0")
    suspend fun searchUsers() : Response<UserResponse<User>>

    @GET("users/mhendrif")
    @Headers("Authorization: token a1fde36ef3613320c0e0f935440bb9c3be25e4f0")
    suspend fun detailUser() : User

    @GET("users/mhendrif/following")
    @Headers("Authorization: token a1fde36ef3613320c0e0f935440bb9c3be25e4f0")
    suspend fun getFollowing() : List<User>

    @GET("users/mhendrif/followers")
    @Headers("Authorization: token a1fde36ef3613320c0e0f935440bb9c3be25e4f0")
    suspend fun getFollowers() : List<User>
}
package com.hendri.githubuser.data.api

import com.hendri.githubuser.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("search/users?q=he")
    @Headers("Authorization: token a1fde36ef3613320c0e0f935440bb9c3be25e4f0")
    suspend fun searchUsers() : Call<UserResponse>
}
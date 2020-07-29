package com.hendri.githubuser.data.api

import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users?")
    suspend fun searchUsers(@Query("q") username: String?) : Response<UserResponse<User>>

    @GET("users/{username}")
    suspend fun detailUser(@Path("username") username: String?) : User

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String?) : List<User>

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String?) : List<User>
}
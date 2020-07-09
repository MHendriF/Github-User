package com.hendri.githubuser.data.api

import com.hendri.githubuser.BuildConfig
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {
    @GET("search/users?q=he")
    @Headers("Authorization: token ${BuildConfig.TOKEN_GITHUB}")
    suspend fun searchUsers() : Response<UserResponse<User>>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.TOKEN_GITHUB}")
    suspend fun detailUser(@Path("username") username: String?) : User

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.TOKEN_GITHUB}")
    suspend fun getFollowing(@Path("username") username: String?) : List<User>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.TOKEN_GITHUB}")
    suspend fun getFollowers(@Path("username") username: String?) : List<User>
}
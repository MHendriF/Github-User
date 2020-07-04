package com.hendri.githubuser.network

import com.hendri.githubuser.data.api.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiInterface {
    @GET("search/users?q={username}")
    @Headers("Authorization: token <Personal Access Token>")
    suspend fun searchUser(
        @Path("username") username: String
    ) : Call<UserResponse>
}
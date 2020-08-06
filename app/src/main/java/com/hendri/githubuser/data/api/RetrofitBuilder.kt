package com.hendri.githubuser.data.api

import com.hendri.githubuser.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = BuildConfig.API_BASE_URL
    private const val TOKEN_GITHUB = BuildConfig.TOKEN_GITHUB

    private val httpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(
            Interceptor { chain ->
                return@Interceptor chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "token $TOKEN_GITHUB")
                        .build()
                )
            }
        )
        this.addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .build()

    val apiService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
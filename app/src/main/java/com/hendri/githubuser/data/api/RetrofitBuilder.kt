package com.hendri.githubuser.data.api

import com.hendri.githubuser.BuildConfig
import com.hendri.githubuser.utils.API_BASE_URL_DEMO
import com.hendri.githubuser.utils.TOKEN_GITHUB_DEMO
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    //private const val BASE_URL = BuildConfig.API_BASE_URL
    //private const val TOKEN_GITHUB = BuildConfig.TOKEN_GITHUB

    //for demo purpose only
    private const val BASE_URL = API_BASE_URL_DEMO
    private const val TOKEN_GITHUB = TOKEN_GITHUB_DEMO

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
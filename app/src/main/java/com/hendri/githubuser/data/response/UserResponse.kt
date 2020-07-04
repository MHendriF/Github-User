package com.hendri.githubuser.data.response

import com.google.gson.annotations.SerializedName
import com.hendri.githubuser.data.model.User

data class UserResponse<T> (
    @SerializedName("total_count")
    val total: Int?,
    @SerializedName("incomplete_results")
    val result : Boolean?,
    @SerializedName("items")
    var dataUsers: List<User>? = null
)

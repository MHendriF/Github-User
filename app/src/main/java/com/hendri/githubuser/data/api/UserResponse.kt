package com.hendri.githubuser.data.api

import com.google.gson.annotations.SerializedName
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.model.User2
import java.util.*

data class UserResponse (
    @SerializedName("total_count")
    val total: Int?,
    @SerializedName("incomplete_results")
    val result : Boolean?,
    @SerializedName("items")
    var datas: List<User2>? = null
){
    fun getUsersData(): List<User2?>? {
        return datas
    }
}

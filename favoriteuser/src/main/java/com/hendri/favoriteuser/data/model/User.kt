package com.hendri.favoriteuser.data.model

import android.content.ContentValues
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Long? = 0,
    var login: String? = "",
    var avatar_url: String? = "",
    var html_url: String? = "",
    var url: String? = "",
    var name: String? = "",
    var company: String? = "",
    var blog: String? = "",
    var location: String? = "",
    var email: String? = "",
    var bio: String? = "",
    var node_id: String? = "",
    var organizations_url: String? = "",
    var repos_url: String? = "",
    var followers: Int? = 0,
    var following: Int? = 0,
    var public_repos: Int? = 0
) : Parcelable {

    companion object {
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_AVATAR = "avatar_url"
        const val COLUMN_HTML_URL = "html_url"
    }
}
package com.hendri.githubuser.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Int?,
    var node_id: String?,
    var login: String?,
    var avatar_url: String?,
    var name: String?,
    var company: String?,
    var blog: String?,
    var location: String?,
    var email: String?,
    var bio: String?,
    var url: String?,
    var html_url: String?,
    var followers_url: String?,
    var following_url: String?,
    var subscriptions_url: String?,
    var organizations_url: String?,
    var repos_url: String?,
    var followers: Int?,
    var following: Int?,
    var public_repos: Int?
) : Parcelable
package com.hendri.githubuser.data.model

data class User(
    var id: Int?,
    var login: String?,
    var avatar_url: String?,
    var company: String?,
    var location: String?,
    var repository: String?,
    var bio: String?,
    var followers_url: String?,
    var following_url: String?,
    var follower: Int?,
    var following: Int?
)
package com.hendri.githubuser.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user_table")
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = false)
    var id: Int?,
    var node_id: String?,
    @ColumnInfo(name = "login")
    var login: String?,
    var avatar_url: String?,
    @ColumnInfo(name = "name")
    var name: String?,
    var company: String?,
    var blog: String?,
    var location: String?,
    @ColumnInfo(name = "email")
    var email: String?,
    var bio: String?,
    var url: String?,
    var html_url: String?,
    var organizations_url: String?,
    var repos_url: String?,
    var followers: Int?,
    var following: Int?,
    var public_repos: Int?
) : Parcelable
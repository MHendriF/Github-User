package com.hendri.githubuser.data.model

import android.os.Parcel
import android.os.Parcelable

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
    var following: Int?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeValue(id)
        dest?.writeString(node_id)
        dest?.writeString(login)
        dest?.writeString(avatar_url)
        dest?.writeString(name)
        dest?.writeString(company)
        dest?.writeString(blog)
        dest?.writeString(location)
        dest?.writeString(email)
        dest?.writeString(bio)
        dest?.writeString(url)
        dest?.writeString(html_url)
        dest?.writeString(followers_url)
        dest?.writeString(following_url)
        dest?.writeString(subscriptions_url)
        dest?.writeString(organizations_url)
        dest?.writeString(repos_url)
        dest?.writeValue(followers)
        dest?.writeValue(following)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
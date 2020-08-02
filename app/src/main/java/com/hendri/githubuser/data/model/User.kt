package com.hendri.githubuser.data.model

import android.content.ContentValues
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hendri.githubuser.utils.USER_TABLE_NAME
import kotlinx.android.parcel.Parcelize

@Entity(tableName = USER_TABLE_NAME)
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COLUMN_ID)
    var id: Long? = 0,
    @ColumnInfo(name = COLUMN_LOGIN)
    var login: String? = "",
    @ColumnInfo(name = COLUMN_AVATAR)
    var avatar_url: String? = "",
    @ColumnInfo(name = COLUMN_HTML_URL)
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
        const val COLUMN_ID = "id"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_AVATAR = "avatar_url"
        const val COLUMN_HTML_URL = "html_url"

        fun toUserModel(values: ContentValues?): User {
            return User().apply {
                if (values != null) {
                    if (values.containsKey(COLUMN_ID)) {
                        this.id = values.getAsLong(COLUMN_ID)
                    }
                    if (values.containsKey(COLUMN_LOGIN)) {
                        this.login = values.getAsString(COLUMN_LOGIN)
                    }
                    if (values.containsKey(COLUMN_AVATAR)) {
                        this.avatar_url = values.getAsString(COLUMN_AVATAR)
                    }
                    if (values.containsKey(COLUMN_HTML_URL)) {
                        this.html_url = values.getAsString(COLUMN_HTML_URL)
                    }
                }
            }
        }

        fun toContentValues(user: User): ContentValues =
            ContentValues().apply {
                put(COLUMN_ID, user.id)
                put(COLUMN_LOGIN, user.login)
                put(COLUMN_AVATAR, user.avatar_url)
                put(COLUMN_HTML_URL, user.html_url)
            }
    }
}
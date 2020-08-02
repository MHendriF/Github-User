package com.hendri.favoriteuser.utils

import android.net.Uri
import com.hendri.favoriteuser.data.model.User

object Constants {
    private const val AUTHORITY = "com.hendri.githubuser"
    private const val SCHEME = "content"

    val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(User.TABLE_NAME)
        .build()
}
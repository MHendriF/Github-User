package com.hendri.githubuser.utils

/* --- Database --- */
const val DATABASE_NAME = "db_user_github"
const val DATABASE_AUTHORITY = "com.hendri.githubuser"
const val DATABASE_SCHEME = "content"
const val DATABASE_CONTENT_URI = "$DATABASE_SCHEME://$DATABASE_AUTHORITY"

/* --- Table User --- */
const val USER_TABLE_NAME = "users"
const val USER_CONTENT_URI = "$DATABASE_CONTENT_URI/$USER_TABLE_NAME"
package com.hendri.githubuser.utils

/* --- Database --- */
const val DATABASE_NAME = "db_user_github"
const val DATABASE_AUTHORITY = "com.hendri.githubuser"
const val DATABASE_SCHEME = "content"
const val DATABASE_CONTENT_URI = "$DATABASE_SCHEME://$DATABASE_AUTHORITY"

/* --- Table User --- */
const val USER_TABLE_NAME = "users"
const val USER_CONTENT_URI = "$DATABASE_CONTENT_URI/$USER_TABLE_NAME"

/* --- API Demo --- */
const val API_BASE_URL_DEMO = "https://api.github.com/"
const val TOKEN_GITHUB_DEMO = "a1fde36ef3613320c0e0f935440bb9c3be25e4f0"
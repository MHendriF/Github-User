package com.hendri.githubuser.ui.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.ui.main.viewmodel.*

class ViewModelFactory(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(apiHelper, dbHelper) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                return DetailViewModel(apiHelper, dbHelper) as T
            }
            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> {
                return FollowersViewModel(apiHelper, dbHelper) as T
            }
            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> {
                return FollowingViewModel(apiHelper, dbHelper) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                return FavoriteViewModel(apiHelper, dbHelper) as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
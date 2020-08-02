package com.hendri.githubuser.ui.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.repository.MainRepository
import com.hendri.githubuser.ui.main.viewmodel.*

class ViewModelFactory(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val context: Context,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(MainRepository(apiHelper, dbHelper, context)) as T
            }
            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> {
                return FollowersViewModel(MainRepository(apiHelper, dbHelper, context)) as T
            }
            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> {
                return FollowingViewModel(MainRepository(apiHelper, dbHelper, context)) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                return DetailViewModel(MainRepository(apiHelper, dbHelper, context)) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                return FavoriteViewModel(MainRepository(apiHelper, dbHelper, context), application) as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
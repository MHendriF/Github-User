package com.hendri.favoriteuser.ui.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hendri.favoriteuser.data.repository.MainRepository
import com.hendri.favoriteuser.ui.main.viewmodel.FavoriteViewModel

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        when {
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                return FavoriteViewModel(MainRepository(context)) as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
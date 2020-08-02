package com.hendri.favoriteuser.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hendri.favoriteuser.data.model.User
import com.hendri.favoriteuser.data.repository.MainRepository

class FavoriteViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    var getFavoriteUsers: LiveData<List<User>> = liveData {
        emit(mainRepository.getFavoriteUsers())
    }
}
package com.hendri.favoriteuser.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.hendri.favoriteuser.data.model.User
import com.hendri.favoriteuser.data.repository.MainRepository

class FavoriteViewModel(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    var getFavoriteUsers: LiveData<List<User>> = liveData {
        emit(mainRepository.getFavoriteUsers())
    }
}
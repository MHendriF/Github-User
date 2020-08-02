package com.hendri.githubuser.ui.main.viewmodel

import android.app.Application
import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.repository.MainRepository
import com.hendri.githubuser.utils.Resource
import com.hendri.githubuser.utils.USER_CONTENT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    val favoriteUsers: LiveData<List<User>> = mainRepository.getAllUsers()

    var myUsers: LiveData<List<User>> = liveData {
        emit(mainRepository.getFavoriteUsers())
    }

    fun deleteUserById(user: User) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.deleteUserById(user)
    }

}
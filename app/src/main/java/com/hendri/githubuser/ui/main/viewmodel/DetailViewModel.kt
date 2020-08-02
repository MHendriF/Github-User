package com.hendri.githubuser.ui.main.viewmodel

import androidx.lifecycle.*
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.repository.MainRepository
import com.hendri.githubuser.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun detailUser(username: String?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getUser(username)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.insertUser(user)
    }
}
package com.hendri.githubuser.ui.main.viewmodel

import androidx.lifecycle.*
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    fun detailUser(username: String?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiHelper.detailUser(username)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        dbHelper.insertUser(user)
    }
}
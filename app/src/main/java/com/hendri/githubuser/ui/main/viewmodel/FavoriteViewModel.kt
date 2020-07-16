package com.hendri.githubuser.ui.main.viewmodel

import androidx.lifecycle.*
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val users = MutableLiveData<Resource<List<User>>>()

    fun fetchUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dbHelper.getUsers2()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getUsers() = viewModelScope.launch(Dispatchers.IO) {
        dbHelper.getUsers()
    }
}
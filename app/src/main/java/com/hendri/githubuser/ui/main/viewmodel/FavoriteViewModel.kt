package com.hendri.githubuser.ui.main.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.data.source.UserDataSource
import com.hendri.githubuser.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val dbHelper: DatabaseHelper,
    context: Context
) : ViewModel() {

    private var dataSource: UserDataSource = UserDataSource(context.contentResolver)

    fun fetchUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = dbHelper.getUsers()))
            //emit(Resource.success(data =  dataSource.fetchUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun deleteUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        dbHelper.deleteUser(user)
    }

    fun getUsers(user: User) = viewModelScope.launch(Dispatchers.IO) {
        dataSource.fetchUsers()
    }
}
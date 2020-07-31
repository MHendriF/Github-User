package com.hendri.githubuser.ui.main.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.data.model.User
import com.hendri.githubuser.utils.Resource
import com.hendri.githubuser.utils.USER_CONTENT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val context: Context
) : ViewModel() {

    fun detailUser(username: String?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiHelper.detailUser(username)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        context.contentResolver.insert(USER_CONTENT_URI.toUri(), User.toContentValues(user))
    }
}
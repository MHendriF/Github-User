package com.hendri.githubuser.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hendri.githubuser.data.api.ApiHelper
import com.hendri.githubuser.data.local.DatabaseHelper
import com.hendri.githubuser.utils.Resource
import kotlinx.coroutines.Dispatchers

class FollowingViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {
    fun getFollowing(username: String?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiHelper.getFollowing(username)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}

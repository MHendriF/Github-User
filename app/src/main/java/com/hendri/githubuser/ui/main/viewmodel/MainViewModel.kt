package com.hendri.githubuser.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hendri.githubuser.data.repository.MainRepository
import com.hendri.githubuser.utils.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            //emit(Resource.success(data = mainRepository.getUsers()))
            //emit(Resource.success(data = mainRepository.searchUsers2()))

            val response = mainRepository.searchUsers()
            if (response.isSuccessful){
                Log.d("Trace", "getUsers: "+response.body()?.dataUsers)
                emit(Resource.success(data = response.body()?.dataUsers))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}
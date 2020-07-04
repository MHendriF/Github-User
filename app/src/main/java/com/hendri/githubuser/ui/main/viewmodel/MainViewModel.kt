package com.hendri.githubuser.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hendri.githubuser.data.repository.MainRepository
import com.hendri.githubuser.utils.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
//            val response2 = mainRepository.searchUsers()
//            response2.enqueue(object : Callback<UserResponse> {
//                override fun onResponse(call: Call<UserResponse>?, response: Response<UserResponse>?) {
//                //    var items = response?.body()
//              //      item_users = items?.users
//                    if (response != null) {
//                        if(response.isSuccessful){
//                            if (response != null) {
//                                emit(Resource.success(data = response.body()))
//                                //data.value = response.body()
//                            }
//                        } else{
//                            //status.value = true
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<UserResponse>?, t: Throwable?) {
//                    Log.v("Error", t.toString())
//                }
//            })

            emit(Resource.success(data = mainRepository.getUsers()))
            //emit(Resource.success(data = mainRepository.searchUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}
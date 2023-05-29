package com.dicoding.autisdetection.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.autisdetection.api.ApiConfig
import com.dicoding.autisdetection.responses.RegisterRequest
import com.dicoding.autisdetection.responses.RegisterResponses
import com.dicoding.autisdetection.setting.SharedPreference
import retrofit2.Call
import retrofit2.Response

class RegisterViewModel(private val sharedPreferences: SharedPreference) : ViewModel(){

    private val _isRegister:MutableLiveData<RegisterResponses> = MutableLiveData()

    fun _getRegister():LiveData<RegisterResponses>{
        return _isRegister
    }

    private val isRegisterd: MutableLiveData<Boolean> = MutableLiveData()
    fun isRegisterd(): LiveData<Boolean>{
        return isRegisterd
    }

    private val isSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    fun isSuccessful(): LiveData<Boolean>{
        return isSuccessful
    }


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    fun register(name: String, username: String, email: String, password: String) {
        _isLoading.value = true
        val registerRequest = RegisterRequest(name, username, email, password)
        val client = ApiConfig.getApiService().register(registerRequest)
        client.enqueue(object : retrofit2.Callback<RegisterResponses> {
            override fun onResponse(
                call: Call<RegisterResponses>,
                response: Response<RegisterResponses>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isRegister.value = response.body()
                    isSuccessful.value = true
                    isRegisterd.value = true
                    Log.d("RegisterViewModel", "onResponse: ${response.body()}")
                } else {
                    isSuccessful.value = false
                    isRegisterd.value = false
                    Log.d("RegisterViewModel", "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponses>, t: Throwable) {
                _isLoading.value = false
                isSuccessful.value = false
                isRegisterd.value = false
                Log.d("RegisterViewModel", "onFailure: ${t.message}")
            }
        })
    }





}
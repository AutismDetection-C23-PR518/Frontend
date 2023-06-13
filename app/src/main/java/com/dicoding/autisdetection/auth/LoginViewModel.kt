package com.dicoding.autisdetection.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.autisdetection.api.ApiConfig
import com.dicoding.autisdetection.responses.LoginRequest
import com.dicoding.autisdetection.responses.LoginResponses
import com.dicoding.autisdetection.setting.SharedPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class LoginViewModel(private val sharedPreferences: SharedPreference) : ViewModel() {

    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean>
        get() = _isLogged

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val isSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    fun isSuccessful(): LiveData<Boolean> {
        return isSuccessful
    }

    fun login(username: String, password: String) {
        _isLoading.value = true
        val loginRequest = LoginRequest(username, password)
        val client = ApiConfig.getApiService().login(loginRequest)
        client.enqueue(object : retrofit2.Callback<LoginResponses> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponses>,
                response: retrofit2.Response<LoginResponses>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    _isLogged.value = true
                    isSuccessful.value = true
                    viewModelScope.launch {
                        sharedPreferences.saveToken(loginResponse?.accessToken.toString())
                        sharedPreferences.saveId(loginResponse?.result?.id.toString())
                    }
                    Log.d("LoginViewModel", "onResponse: ${response.body()?.accessToken}")
                    Log.d("LoginViewModel", "onResponse: ${response.body()?.result?.id}")
                } else {
                    _isLogged.value = false
                    isSuccessful.value = false
                    Log.d("LoginViewModel", "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponses>, t: Throwable) {
                _isLoading.value = false
                _isLogged.value = false
                isSuccessful.value = false
                Log.d("LoginViewModel", "onFailure: ${t.message}")
            }
        })
    }
}

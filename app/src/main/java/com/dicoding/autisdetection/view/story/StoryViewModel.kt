package com.dicoding.autisdetection.view.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.autisdetection.api.ApiConfig
import com.dicoding.autisdetection.responses.StoryRequest
import com.dicoding.autisdetection.responses.StoryResponses
import com.dicoding.autisdetection.setting.SharedPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(private val sharedPreferences: SharedPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading





    fun sendStory(deskripsi: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = sharedPreferences.getToken().first()
            val storyRequest = StoryRequest(deskripsi)
            val call = ApiConfig.getApiService().postStories("Bearer $token", storyRequest)
            call.enqueue(object : Callback<StoryResponses> {
                override fun onResponse(
                    call: Call<StoryResponses>,
                    response: Response<StoryResponses>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val story = response.body()
                        Log.d("StoryViewModel", "onResponse: ${response.body()}")
                    } else {
                        Log.d("StoryViewModel", "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<StoryResponses>, t: Throwable) {
                    _isLoading.value = false
                    Log.d("StoryViewModel", "onFailure: ${t.message}")
                }
            })
        }
    }





}
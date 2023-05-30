package com.dicoding.autisdetection.view.main.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.autisdetection.api.ApiConfig
import com.dicoding.autisdetection.responses.StoryResponses
import com.dicoding.autisdetection.setting.SharedPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val sharedPreferences: SharedPreference) : ViewModel() {

    private val dataStory = MutableLiveData<List<StoryResponses>>()
    val story: LiveData<List<StoryResponses>>
        get() = dataStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun getStories() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = sharedPreferences.getToken().first()
            ApiConfig.getApiService().getAllPost("Bearer $token")
                .enqueue(object : retrofit2.Callback<List<StoryResponses>> {
                    override fun onResponse(
                        call: retrofit2.Call<List<StoryResponses>>,
                        response: retrofit2.Response<List<StoryResponses>>
                    ) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            dataStory.value = response.body()
                            Log.d("HomeViewModel", "onResponse: ${response.body()}")
                        } else {
                            Log.d(
                                "HomeViewModel",
                                "Unsuccessful response: ${response.code()}"
                            )
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<List<StoryResponses>>,
                        t: Throwable
                    ) {
                        _isLoading.value = false
                        Log.d("HomeViewModel", "onFailure: ${t.message}")
                    }
                })
        }
    }
}

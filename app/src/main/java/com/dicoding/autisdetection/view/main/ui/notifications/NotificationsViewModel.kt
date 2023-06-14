package com.dicoding.autisdetection.view.main.ui.notifications

import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.autisdetection.api.ApiConfig
import com.dicoding.autisdetection.responses.*
import com.dicoding.autisdetection.setting.SharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsViewModel(private val sharedPreferences: SharedPreference) : ViewModel() {

    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean>
        get() = _isLogged

    private val getUserById = MutableLiveData<UserResponses>()
    val getUser: LiveData<UserResponses>
        get() = getUserById


    private val isSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    fun isSuccessful(): LiveData<Boolean>{
        return isSuccessful
    }


    private val dataStory = MutableLiveData<List<StoryResponses>>()
    val story: LiveData<List<StoryResponses>>
        get() = dataStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun getUserId(userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val token = sharedPreferences.getToken().first()
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getUser("Bearer $token", userId).execute()
                }
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    userResponse?.let { userResponse ->
                        val user = UserResponses(
                            userResponse.id_user,
                            userResponse.username,
                            userResponse.email,
                            userResponse.name
                        )
                        getUserById.postValue(user)
                        if (user.name != null) {
                            Log.d("Nama User", user.name)
                        } else {
                            Log.d("Nama User", "Name is null")
                        }
                    }
                } else {
                    Log.e("User", "Failed to get user, response code: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("User", "Failed to get user", e)
            }
        }
    }


    fun updateUser(id: Int, name: String, username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = sharedPreferences.getToken().first()
            val update = Updatereq(name, username, email, password)
            ApiConfig.getApiService().updateProfile("Bearer $token", id, update)
                .enqueue(object : retrofit2.Callback<Updatereq> {
                    override fun onResponse(
                        call: Call<Updatereq>,
                        response: Response<Updatereq>
                    ) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            // Perubahan berhasil disimpan
                            isSuccessful.value = true
                            Log.d("Update", "onResponse: ${response.body()}")
                            Log.d("Update", "onResponse: $token")
                            // Refresh data pengguna setelah pembaruan
                        } else {
                            // Gagal menyimpan perubahan
                            isSuccessful.value = false
                            val errorMessage = response.errorBody()?.string()
                            Log.d("Update", "onResponse (error): ${response.code()}")
                            Log.d("Update", "onResponse (error): $errorMessage")
                        }
                    }

                    override fun onFailure(call: Call<Updatereq>, t: Throwable) {
                        // Gagal melakukan permintaan jaringan
                        _isLoading.value = false
                        isSuccessful.value = false
                    }
                })
        }
    }






    fun getStories(id : Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = sharedPreferences.getToken().first()
            ApiConfig.getApiService().getPostId("Bearer $token", id)
                .enqueue(object : retrofit2.Callback<List<StoryResponses>> {
                    override fun onResponse(
                        call: retrofit2.Call<List<StoryResponses>>,
                        response: retrofit2.Response<List<StoryResponses>>
                    ) {
                        _isLoading.value = false
                        if (response.isSuccessful) {
                            val sortedStories = response.body()?.sortedByDescending { it.createdAt }
                            dataStory.value = sortedStories as List<StoryResponses>
                            Log.d("HomeViewModel", "onResponse: $sortedStories")
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




    fun logout() {
        viewModelScope.launch {
            sharedPreferences.logout()
            _isLogged.postValue(false)
        }
    }
}

package com.dicoding.autisdetection.view.main.ui.notifications

import android.util.Log
import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.autisdetection.api.ApiConfig
import com.dicoding.autisdetection.responses.StoryResponses
import com.dicoding.autisdetection.responses.UserResponses
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

    private val getUserById = MutableLiveData<UserResponses>()
    val getUser: LiveData<UserResponses>
        get() = getUserById

    private val getPostById = MutableLiveData<StoryResponses?>()
    val getPost: LiveData<StoryResponses?>
        get() = getPostById


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    fun getUserId(userid: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val token = sharedPreferences.getToken().first()
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getApiService().getUser("Bearer $token", userid).execute()
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



//    fun getPostId(postid: Int) {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val token = sharedPreferences.getToken().first()
//                val response = withContext(Dispatchers.IO) {
//                    ApiConfig.getApiService().getPostId("Bearer $token", postid).execute()
//                }
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    val postResponse = response.body()
//                    postResponse?.let { postResponse ->
//                        val post = StoryResponses(
//                            postResponse.id_post,
//                            postResponse.id_user,
//                            postResponse.title,
//                            postResponse.description,
//                            postResponse.image,
//                            postResponse.created_at,
//                            postResponse.updated_at
//                        )
//                        getPostById.postValue(post)
//                        if (post.idPost != null) {
//                            Log.d("Judul Post", post.post)
//                        } else {
//                            Log.d("Judul Post", "Title is null")
//                        }
//                    }
//                } else {
//                    Log.e("Post", "Failed to get post, response code: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                Log.e("Post", "Failed to get post", e)
//            }
//        }
//    }




    fun logout() {
        viewModelScope.launch {
            sharedPreferences.logout()
            _isLogged.postValue(false)
        }
    }
}
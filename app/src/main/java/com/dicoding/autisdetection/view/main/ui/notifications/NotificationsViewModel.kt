package com.dicoding.autisdetection.view.main.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.autisdetection.setting.SharedPreference
import kotlinx.coroutines.launch

class NotificationsViewModel(private val sharedPreferences: SharedPreference) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    private val _isLogged = MutableLiveData<Boolean>()

    fun logout() {
        viewModelScope.launch {
            sharedPreferences.logout()
            _isLogged.postValue(false)
        }
    }
}
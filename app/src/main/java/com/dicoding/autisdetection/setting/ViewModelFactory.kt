package com.dicoding.autisdetection.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.autisdetection.auth.LoginViewModel
import com.dicoding.autisdetection.auth.RegisterViewModel
import com.dicoding.autisdetection.view.main.ui.home.HomeViewModel
import com.dicoding.autisdetection.view.main.ui.notifications.NotificationsViewModel

class ViewModelFactory(private val preference: SharedPreference, private val context: Context) : ViewModelProvider.Factory  {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(preference) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(preference) as T
            }
            modelClass.isAssignableFrom(NotificationsViewModel::class.java) -> {
                NotificationsViewModel(preference) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(preference) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }


}
package com.dicoding.autisdetection.screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.dicoding.autisdetection.databinding.ActivitySplashScreenBinding
import com.dicoding.autisdetection.setting.SharedPreference
import com.dicoding.autisdetection.view.main.HomeActivity
import com.dicoding.autisdetection.view.main.MainActivity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val preference = SharedPreference.getInstance(dataStore)

        lifecycleScope.launch {
            val token = preference.getToken().firstOrNull()

            Handler(Looper.getMainLooper()).postDelayed({
                if (token.isNullOrEmpty()) {
                    val intent = Intent(this@SplashScreen, OnboardingActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashScreen, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 2000)

        }
    }
}
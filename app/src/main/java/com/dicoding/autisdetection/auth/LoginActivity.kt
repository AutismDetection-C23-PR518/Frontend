package com.dicoding.autisdetection.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.autisdetection.databinding.ActivityLoginBinding
import com.dicoding.autisdetection.setting.SharedPreference
import com.dicoding.autisdetection.setting.ViewModelFactory
import com.dicoding.autisdetection.view.main.HomeActivity
import com.dicoding.autisdetection.view.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(SharedPreference.getInstance(dataStore), this))[LoginViewModel::class.java]

        supportActionBar?.hide()

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty()) {
                binding.etUsername.error = "Username required"
                binding.etUsername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Password required"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            viewModel.login(username, password)
        }


       viewModel.isLoading.observe(this){
           loadings ->
              binding.loading.visibility = if (loadings) View.VISIBLE else View.GONE
              binding.btnLogin.isEnabled = !loadings
              binding.btnSignUp.isEnabled = !loadings
              binding.etPassword.isEnabled = !loadings
              binding.etUsername.isEnabled = !loadings
       }


        viewModel.isLogged.observe(this) { isLogged ->
            if (isLogged) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                binding.loading.visibility = View.GONE
                MaterialAlertDialogBuilder(this)
                    .setTitle("Login Failed")
                    .setMessage("Username or password is incorrect")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }





    }
}
package com.dicoding.autisdetection.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.autisdetection.databinding.ActivityRegisterBinding
import com.dicoding.autisdetection.setting.SharedPreference
import com.dicoding.autisdetection.setting.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory(SharedPreference.getInstance(dataStore), this))[RegisterViewModel::class.java]

        supportActionBar?.hide()
        binding.btnSignUp.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (name.isEmpty()) {
                binding.etName.error = "Name required"
                binding.etName.requestFocus()
                return@setOnClickListener
            }

            viewModel.register(name, username, email, password)

        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        viewModel.isRegisterd().observe(this) { isRegistered ->
            if (isRegistered) {
                Toast.makeText(this, "Registrasi berhasil! Silakan login untuk melanjutkan.", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSignUp.isEnabled = !isLoading
            binding.etEmail.isEnabled = !isLoading
            binding.etName.isEnabled = !isLoading
            binding.etPassword.isEnabled = !isLoading
            binding.etUsername.isEnabled = !isLoading
        }






    }
}
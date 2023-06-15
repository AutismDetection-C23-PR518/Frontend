package com.dicoding.autisdetection.view.main.edit

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.autisdetection.auth.LoginViewModel
import com.dicoding.autisdetection.databinding.ActivityEditProfileBinding
import com.dicoding.autisdetection.setting.SharedPreference
import com.dicoding.autisdetection.setting.ViewModelFactory
import com.dicoding.autisdetection.view.main.HomeActivity
import com.dicoding.autisdetection.view.main.ui.notifications.NotificationsViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var viewModel: NotificationsViewModel
    var userid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        supportActionBar?.hide()
        val preference = SharedPreference.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(preference, this)).get(NotificationsViewModel::class.java)


        viewModel.getUser.observe(this){user ->
            binding.editTextName.setText(user.username)
            binding.editTextEmail.setText(user.name)
            binding.editTextUsername.setText(user.email)
        }

        lifecycleScope.launch {
            userid = preference.getId().firstOrNull()?.toIntOrNull() ?: 0
            viewModel.getUserId(userid)
        }

        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val username = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            viewModel.updateUser(userid, name, username, email , password)
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }


        viewModel.isLoading.observe(this){
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }
}
package com.dicoding.autisdetection.view.story

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.dicoding.autisdetection.R
import com.dicoding.autisdetection.databinding.ActivityAddStoryBinding
import com.dicoding.autisdetection.setting.SharedPreference
import com.dicoding.autisdetection.setting.ViewModelFactory
import com.dicoding.autisdetection.view.main.HomeActivity

class AddStory : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: StoryViewModel
    private val REQUEST_ADD_STORY = 100
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this, ViewModelFactory(SharedPreference.getInstance(dataStore), this))[StoryViewModel::class.java]


        viewModel.isLoading.observe(this) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
            binding.deskripsiEditText.isEnabled = !it
        }

        binding.cancelButton.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }



        binding.uploadButton.setOnClickListener {
            val deskripsi = binding.deskripsiEditText.text.toString()
            if (deskripsi.isEmpty()) {
                binding.deskripsiEditText.error = "Deskripsi tidak boleh kosong"
                binding.deskripsiEditText.requestFocus()
                return@setOnClickListener
            }

            viewModel.sendStory(deskripsi)
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }



    }
}
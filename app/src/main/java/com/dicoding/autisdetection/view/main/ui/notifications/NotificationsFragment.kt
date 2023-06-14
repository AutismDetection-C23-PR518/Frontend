package com.dicoding.autisdetection.view.main.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.autisdetection.auth.LoginActivity
import com.dicoding.autisdetection.auth.LoginViewModel
import com.dicoding.autisdetection.databinding.FragmentNotificationsBinding
import com.dicoding.autisdetection.responses.StoryResponses
import com.dicoding.autisdetection.setting.SharedPreference
import com.dicoding.autisdetection.setting.ViewModelFactory
import com.dicoding.autisdetection.view.main.edit.EditProfileActivity
import com.dicoding.autisdetection.view.main.ui.dashboard.DashboardViewModel
import com.dicoding.autisdetection.view.main.ui.home.AdapterHome
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: NotificationsViewModel
    private lateinit var adapter: AdapterHome
    private var userid = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedPreferences = SharedPreference.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(sharedPreferences, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvList.layoutManager = LinearLayoutManager(requireContext())

        binding.btnLogout.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    viewModel.logout()
                    requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            userid = sharedPreferences.getId().firstOrNull()?.toIntOrNull() ?: 0
            viewModel.getUserId(userid)
            viewModel.getStories(userid)
        }

        viewModel.story.observe(viewLifecycleOwner) { posts ->
            val sortedStories = posts.sortedByDescending { it.createdAt}
            adapter = AdapterHome(sortedStories)
            binding.rvList.adapter = adapter
        }


        viewModel.getUser.observe(viewLifecycleOwner) { user ->
            binding.TvNama.text = user.name
            binding.TvUsername.text = user.username
        }

        Log.d("roli", userid.toString())

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.loadings.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

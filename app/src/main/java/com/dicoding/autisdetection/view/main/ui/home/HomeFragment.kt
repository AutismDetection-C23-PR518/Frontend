package com.dicoding.autisdetection.view.main.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.autisdetection.databinding.FragmentHomeBinding
import com.dicoding.autisdetection.setting.SharedPreference
import com.dicoding.autisdetection.setting.ViewModelFactory
import com.dicoding.autisdetection.view.main.ui.notifications.NotificationsViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: AdapterHome

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val sharedPreferences = SharedPreference.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(sharedPreferences, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getStories()

        viewModel.story.observe(viewLifecycleOwner) { stories ->
            adapter = AdapterHome(stories)
            binding.rvList.adapter = adapter
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipe.isRefreshing = it
        }

        binding.swipe.setOnRefreshListener {
            viewModel.getStories()
            binding.swipe.isRefreshing = false
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
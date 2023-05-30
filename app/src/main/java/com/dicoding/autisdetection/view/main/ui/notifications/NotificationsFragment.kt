package com.dicoding.autisdetection.view.main.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.autisdetection.auth.LoginActivity
import com.dicoding.autisdetection.auth.LoginViewModel
import com.dicoding.autisdetection.databinding.FragmentNotificationsBinding
import com.dicoding.autisdetection.setting.SharedPreference
import com.dicoding.autisdetection.setting.ViewModelFactory
import com.dicoding.autisdetection.view.main.ui.dashboard.DashboardViewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: NotificationsViewModel

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


        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            true
        }





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
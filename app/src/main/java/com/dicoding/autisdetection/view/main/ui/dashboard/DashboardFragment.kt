package com.dicoding.autisdetection.view.main.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.autisdetection.data.ResultDao
import com.dicoding.autisdetection.data.ResultDatabase
import com.dicoding.autisdetection.databinding.FragmentDashboardBinding
import com.dicoding.autisdetection.view.story.AdapterStory
import com.dicoding.autisdetection.view.test.ScreeningTestActivity
import kotlinx.coroutines.runBlocking

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var adapter: AdapterStory

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Declare ResultDatabase and ResultDao
    private lateinit var resultDatabase: ResultDatabase
    private lateinit var resultDao: ResultDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize ResultDatabase and ResultDao
        resultDatabase = ResultDatabase.getDatabase(requireContext())
        resultDao = resultDatabase.resultDao()

        // Fetch data from the database
        val resultList = runBlocking { resultDao.getAllResult() }

        // Create the adapter
        adapter = AdapterStory(resultList)

        // Set the adapter to the RecyclerView
        binding.recyclerView.adapter = adapter

        // Set RecyclerView layout manager as LinearLayoutManager with horizontal orientation
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.start.setOnClickListener {
            startActivity(Intent(activity, ScreeningTestActivity::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

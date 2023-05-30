package com.dicoding.autisdetection.view.main.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.autisdetection.databinding.ListBinding
import com.dicoding.autisdetection.responses.StoryResponses
import com.dicoding.autisdetection.setting.Constanta

class AdapterHome(private val stories: List<StoryResponses>) :
    RecyclerView.Adapter<AdapterHome.AdapterViewHolder>() {

    inner class AdapterViewHolder(private val binding: ListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryResponses) {
            // Bind data to views
            binding.story.text = story.stori
            binding.date.text = Constanta.convertDbDateToDisplayDate(story.created_at)
            // Bind other views if needed
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterHome.AdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListBinding.inflate(inflater, parent, false)
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterHome.AdapterViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int {
        return stories.size
    }
}

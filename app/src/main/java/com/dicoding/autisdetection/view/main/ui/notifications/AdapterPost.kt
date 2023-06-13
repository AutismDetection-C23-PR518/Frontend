package com.dicoding.autisdetection.view.main.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.autisdetection.databinding.CardBinding
import com.dicoding.autisdetection.responses.StoryResponses
import com.dicoding.autisdetection.setting.Constanta

class AdapterPost(private val stories: List<StoryResponses>) : RecyclerView.Adapter<AdapterPost.AdapterViewHolder>() {

    inner class AdapterViewHolder(private val binding: CardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryResponses) {
            // Bind data to views
            binding.story.text = story.post
            binding.date.text = Constanta.convertDbDateToDisplayDate(story.createdAt)
            binding.titleText.text = story.user?.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPost.AdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardBinding.inflate(inflater, parent, false)
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterPost.AdapterViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int {
        return stories.size
    }
}



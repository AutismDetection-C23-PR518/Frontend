package com.dicoding.autisdetection.view.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.autisdetection.data.EntityResult
import com.dicoding.autisdetection.databinding.ListResultCardBinding

class AdapterStory(private val resultList: List<EntityResult>) : RecyclerView.Adapter<AdapterStory.ResultViewHolder>() {

    inner class ResultViewHolder(private val binding: ListResultCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: EntityResult) {
            binding.resultTextView.text = result.result
            binding.skorTextView.text = result.skor
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListResultCardBinding.inflate(inflater, parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = resultList[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int {
        return resultList.size
    }
}

package com.example.memescollection.view.adapters

import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memescollection.databinding.MemeItemLayoutBinding
import com.example.memescollection.model.Meme
import com.squareup.picasso.Picasso

class MemesAdapter(private val data: List<Meme>, private val downloadImage: (Meme) -> Unit) :
    RecyclerView.Adapter<MemesAdapter.MemesViewHolder>() {

    class MemesViewHolder(private val binding: MemeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(dataItem: Meme, downloadImage: (Meme) -> Unit) {
            Picasso.get().load(dataItem.url).into(binding.ivMemeImage)
            binding.tvMemeTitle.text = dataItem.name

            binding.acbSave.setOnClickListener{
                downloadImage(dataItem)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemesAdapter.MemesViewHolder {
        return MemesViewHolder(
            MemeItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MemesAdapter.MemesViewHolder, position: Int) {
        return holder.onBind(data[position], downloadImage)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
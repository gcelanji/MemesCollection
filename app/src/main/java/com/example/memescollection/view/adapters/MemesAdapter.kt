package com.example.memescollection.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memescollection.databinding.MemeItemLayoutBinding
import com.example.memescollection.model.Meme
import com.squareup.picasso.Picasso

class MemesAdapter(private val data: List<Meme>) :
    RecyclerView.Adapter<MemesAdapter.MemesViewHolder>() {

    class MemesViewHolder(private val binding: MemeItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(dataItem: Meme) {
            Picasso.get().load(dataItem.url).into(binding.ivMemeImage)
            binding.tvMemeTitle.text = dataItem.name
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
        return holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
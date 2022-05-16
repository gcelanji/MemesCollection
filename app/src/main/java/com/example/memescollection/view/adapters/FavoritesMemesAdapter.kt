package com.example.memescollection.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memescollection.databinding.MemeFavoriteItemLayoutBinding
import com.example.memescollection.databinding.MemeItemLayoutBinding
import com.example.memescollection.model.Meme
import com.squareup.picasso.Picasso

private const val TAG = "MemesAdapter"
class FavoritesMemesAdapter(
    private val data: List<Meme>,
    private val downloadImage: (Meme) -> Unit,
    private val removeFromFavorites: (Meme) -> Unit
) :
    RecyclerView.Adapter<FavoritesMemesAdapter.FavoritesMemesViewHolder>() {

    class FavoritesMemesViewHolder(private val binding: MemeFavoriteItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(dataItem: Meme, downloadImage: (Meme) -> Unit, addToFavorites: (Meme) -> Unit) {
            Picasso.get().load(dataItem.url).into(binding.ivMemeImage)
            binding.tvMemeTitle.text = dataItem.name

            binding.acibSave.setOnClickListener {
                downloadImage(dataItem)
            }

            binding.acibUnlike.setOnClickListener {
                Log.d(TAG, "onBind: UnLike button clicked")
                addToFavorites(dataItem)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesMemesAdapter.FavoritesMemesViewHolder {
        return FavoritesMemesViewHolder(
            MemeFavoriteItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritesMemesAdapter.FavoritesMemesViewHolder, position: Int) {
        return holder.onBind(data[position], downloadImage, removeFromFavorites)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
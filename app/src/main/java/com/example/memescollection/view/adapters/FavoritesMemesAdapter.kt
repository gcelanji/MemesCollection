package com.example.memescollection.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memescollection.databinding.MemeFavoriteItemLayoutBinding
import com.example.memescollection.model.Meme
import com.squareup.picasso.Picasso

private const val TAG = "MemesAdapter"

class FavoritesMemesAdapter(
    private val data: MutableList<Meme>,
    private val downloadImage: (Meme) -> Unit,
    private val deleteFromFavorites: (Meme) -> Unit
) :
    RecyclerView.Adapter<FavoritesMemesAdapter.FavoritesMemesViewHolder>() {

    class FavoritesMemesViewHolder(private val binding: MemeFavoriteItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(dataItem: Meme, downloadImage: (Meme) -> Unit, deleteFromFavorites: (Meme) -> Unit) {
            if(dataItem.name == "No Favorites Found"){
                binding.acbRemove.visibility = View.INVISIBLE
                binding.acibSave.visibility = View.INVISIBLE
                binding.ivMemeImage.visibility = View.INVISIBLE
            }
            Picasso.get().load(dataItem.url).into(binding.ivMemeImage)
            binding.tvMemeTitle.text = dataItem.name

            binding.acibSave.setOnClickListener {
                downloadImage(dataItem)
            }

            binding.acbRemove.setOnClickListener {
                Log.d(TAG, "onBind: UnLike button clicked")
                deleteFromFavorites(dataItem)

            }


        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesMemesViewHolder {
        return FavoritesMemesViewHolder(
            MemeFavoriteItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoritesMemesViewHolder, position: Int) {
        return holder.onBind(data[position], downloadImage, deleteFromFavorites)
    }

    override fun getItemCount(): Int {
        return data.size
    }



}
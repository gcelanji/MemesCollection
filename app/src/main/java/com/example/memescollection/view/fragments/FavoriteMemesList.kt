package com.example.memescollection.view.fragments

import android.R
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memescollection.common.ImageDownloader
import com.example.memescollection.databinding.MemeItemsListLayoutBinding
import com.example.memescollection.model.Meme
import com.example.memescollection.model.database.MemeEntity
import com.example.memescollection.view.adapters.FavoritesMemesAdapter
import com.example.memescollection.viewmodel.MemesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "FavoriteMemesList"

@AndroidEntryPoint
class FavoriteMemesList : Fragment() {

    private lateinit var binding: MemeItemsListLayoutBinding
    private lateinit var snackbar: Snackbar
    private val viewModel: MemesViewModel by lazy {
        ViewModelProvider(this)[MemesViewModel::class.java]
    }
    private lateinit var adapter: FavoritesMemesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = MemeItemsListLayoutBinding.inflate(
            inflater,
            container,
            false
        )

        initObservables()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObservables() {
        viewModel.favoriteMemes.observe(viewLifecycleOwner) {
            updateUI(it)
            adapter.notifyDataSetChanged()
            Log.d(TAG, "initObservables: ${it[0].name}")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateUI(list: List<MemeEntity>) {
        list.let { listMemeEntity ->
            listMemeEntity.map {
                Meme(it.name, it.url)
            }.let {
                Log.d(TAG, "updateUI: $it")
                updateAdapter(it)
                adapter.notifyDataSetChanged()
            }
        }

    }

    private fun updateAdapter(data: List<Meme>) {
        adapter = FavoritesMemesAdapter(
            data = data as MutableList<Meme>,
            downloadImage = {
                downloadMeme(it)
            },
            deleteFromFavorites = { deleteFromFavorites(it) })

        binding.rvMemesList.layoutManager = LinearLayoutManager(context)
        binding.rvMemesList.adapter = adapter
        binding.progressbar.visibility = View.INVISIBLE
    }


    private fun downloadMeme(meme: Meme) {
        val mgr = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        ImageDownloader.download(meme, mgr)
        snackbar = Snackbar.make(
            requireActivity().findViewById(R.id.content),
            "Download Completed", Snackbar.LENGTH_SHORT
        ).setAction("DOWNLOADS") {
            startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
        }
        snackbar.show()
    }

    private fun deleteFromFavorites(item: Meme) {
        viewModel.deleteFromFavorites(item)
        viewModel.getFavoriteMemesList()
        snackbar = Snackbar.make(
            requireActivity().findViewById(R.id.content),
            "Deleting...", Snackbar.LENGTH_SHORT
        )
        snackbar.show()
    }

}
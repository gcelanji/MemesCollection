package com.example.memescollection.view.fragments

import android.R
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memescollection.databinding.MemeItemsListLayoutBinding
import com.example.memescollection.model.Meme
import com.example.memescollection.model.database.MemeEntity
import com.example.memescollection.view.adapters.MemesAdapter
import com.example.memescollection.viewmodel.MemesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

private const val TAG = "FavoriteMemesList"

@AndroidEntryPoint
class FavoriteMemesList : Fragment() {

    private lateinit var binding: MemeItemsListLayoutBinding
    private lateinit var snackbar: Snackbar
    private val viewModel: MemesViewModel by lazy {
        ViewModelProvider(this)[MemesViewModel::class.java]
    }
    private lateinit var adapter: MemesAdapter

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

    private fun initObservables() {
        viewModel.getFavoriteMemesList()
        viewModel.favoriteMemes.observe(viewLifecycleOwner){
            updateUI(it)
            Log.d(TAG, "initObservables: ${it[0].name}")
        }
    }

    private fun updateUI(list: List<MemeEntity>) {
        list.let { listMemeEntity ->
            listMemeEntity.map {
                Meme(it.name, it.url)
            }.let {
                Log.d(TAG, "updateUI: ${it.toString()}")
                updateAdapter(it)
            }
        }

    }

    private fun updateAdapter(data: List<Meme>){
        adapter = MemesAdapter(
            data = data,
            downloadImage = { download(it) },
            addToFavorites = { addToFavorites(it) })

        binding.rvMemesList.layoutManager = LinearLayoutManager(context)
        binding.rvMemesList.adapter = adapter
        binding.progressbar.visibility = View.INVISIBLE
    }

    private fun download(item: Meme) {
        downloadFile(item.url, item.name)
    }

    private fun downloadFile(URl: String?, imageName: String?) {
        val direct = File(
            Environment.getExternalStorageDirectory()
                .toString()
        )
        if (!direct.exists()) {
            direct.mkdirs()
        }
        val mgr = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(URl)
        val request = DownloadManager.Request(
            downloadUri
        )
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI
                    or DownloadManager.Request.NETWORK_MOBILE
        )
            .setAllowedOverRoaming(false).setTitle("$imageName")
            .setDescription("Something useful. No, really.")
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${imageName}.jpeg"
        )
        mgr.enqueue(request)

        snackbar = Snackbar.make(
            requireActivity().findViewById(R.id.content),
            "Download Completed", Snackbar.LENGTH_LONG
        ).setAction("Downloads", View.OnClickListener {
            startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
        }

        )
        snackbar.show()
    }

    private fun addToFavorites(item: Meme) {
        viewModel.addToFavorites(item)
    }
}
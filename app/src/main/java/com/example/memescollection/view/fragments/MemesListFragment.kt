package com.example.memescollection.view.fragments

import android.R
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memescollection.databinding.MemeItemsListLayoutBinding
import com.example.memescollection.model.Meme
import com.example.memescollection.model.UIState
import com.example.memescollection.view.adapters.MemesAdapter
import com.example.memescollection.viewmodel.MemesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


private const val TAG = "MemesListFragment"

@AndroidEntryPoint
class MemesListFragment : Fragment() {

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
        viewModel.getMemesList()
        viewModel.memes.observe(viewLifecycleOwner) { response ->
            when (response) {
                is UIState.ResponseMemesList -> {
                    Log.d(TAG, "initObservables: $response")
                    // bind data to the view
                    // Delay 3 seconds just to show the Progress Bar :P
                    Handler(Looper.getMainLooper()).postDelayed({
                        updateUI(responseData = response.data.data.memes)
                    }, 3000)

                }
                is UIState.Error -> {
                    // show error message
                    Log.d(TAG, "initObservables: ${response.errorMessage}")
                }
                is UIState.Loading -> {
                    // show a progress bar
                    binding.progressbar.visibility = View.VISIBLE
                    Log.d(TAG, "initObservables: ${response.isLoading}")
                }
            }

        }
    }

    private fun updateUI(responseData: List<Meme>) {
        adapter = MemesAdapter(data = responseData) {
            download(it)
        }
        binding.rvMemesList.layoutManager = LinearLayoutManager(context)
        binding.rvMemesList.adapter = adapter
        binding.progressbar.visibility = View.INVISIBLE
    }

    private fun download(item: Meme) {
        downloadFile(item.url, item.name)
    }

    private fun downloadFile(URl: String?, imageName : String?) {
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
            .setAllowedOverRoaming(false).setTitle("Demo")
            .setDescription("Something useful. No, really.")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${imageName}.jpeg")
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


}
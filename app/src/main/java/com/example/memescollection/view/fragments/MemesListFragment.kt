package com.example.memescollection.view.fragments

import android.R
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memescollection.common.ImageDownloader
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

    override fun onResume() {
        super.onResume()
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()
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
        adapter = MemesAdapter(
            data = responseData,
            downloadImage = { downloadMeme(it) },
            addToFavorites = { addToFavorites(it) })
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

    private fun addToFavorites(item: Meme) {
        viewModel.addToFavorites(item)
        snackbar = Snackbar.make(
            requireActivity().findViewById(R.id.content),
            "Added to Favorites", Snackbar.LENGTH_SHORT
        )
        snackbar.show()
    }

}
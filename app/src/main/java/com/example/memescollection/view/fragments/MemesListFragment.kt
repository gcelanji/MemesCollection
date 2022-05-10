package com.example.memescollection.view.fragments

import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MemesListFragment"

@AndroidEntryPoint
class MemesListFragment : Fragment() {

    private lateinit var binding: MemeItemsListLayoutBinding
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
                    updateUI(responseData = response.data.data.memes)
                }
                is UIState.Error -> {
                    // show error message
                    Log.d(TAG, "initObservables: $response")
                }
                is UIState.Loading -> {
                    // show a progress bar
                    Log.d(TAG, "initObservables: $response")
                }
            }

        }
    }

    private fun updateUI(responseData: List<Meme>) {
        adapter = MemesAdapter(data = responseData)
        binding.rvMemesList.layoutManager = LinearLayoutManager(context)
        binding.rvMemesList.adapter = adapter
    }

}
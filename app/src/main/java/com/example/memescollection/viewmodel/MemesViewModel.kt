package com.example.memescollection.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memescollection.model.Repository
import com.example.memescollection.model.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemesViewModel @Inject constructor(
    private val repository: Repository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _memes = MutableLiveData<UIState>()
    val memes: LiveData<UIState> get() = _memes

//    init {
//        getMemesList()
//    }

    fun getMemesList() {
        viewModelScope.launch(ioDispatcher) {
            repository.getMemes().collect {
                _memes.postValue(it)
            }
        }

    }

}
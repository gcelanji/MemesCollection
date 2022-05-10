package com.example.memescollection.model

sealed class UIState {
    data class ResponseMemesList(val data: MemesResponse) : UIState()
    data class Error(val errorMessage: String) : UIState()
    data class Loading(val isLoading: Boolean = false) : UIState()
}
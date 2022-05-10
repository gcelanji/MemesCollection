package com.example.memescollection.model

import com.example.memescollection.model.remote.MemesApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Repository {
    fun getMemes(): Flow<UIState>
}

class RepositoryImpl(private val apiService: MemesApiService) : Repository {

    override fun getMemes(): Flow<UIState> {
        return flow {
            emit(UIState.Loading(true))
            val response = apiService.getMemes()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(UIState.ResponseMemesList(it))
                } ?: UIState.Error(response.message())
            } else {
                UIState.Error(response.message())
            }
        }
    }

}
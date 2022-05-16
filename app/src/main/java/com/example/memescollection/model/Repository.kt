package com.example.memescollection.model

import com.example.memescollection.model.database.MemeDAO
import com.example.memescollection.model.database.MemeEntity
import com.example.memescollection.model.remote.MemesApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Repository {
    fun getMemes(): Flow<UIState>
    suspend fun updateMemeToDB(meme: Meme)
    suspend fun deleteMemeFromDB(meme: Meme)
    suspend fun getMemesFromDB(): List<MemeEntity>
}

class RepositoryImpl(private val apiService: MemesApiService, private val memeDAO: MemeDAO) :
    Repository {

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

    override suspend fun updateMemeToDB(meme: Meme) {
        memeDAO.updateMemeToDatabase(MemeEntity(meme.url, meme.name))
    }

    override suspend fun deleteMemeFromDB(meme: Meme) {
        memeDAO.deleteMemeFromDatabase(MemeEntity(meme.url, meme.name))
    }

    override suspend fun getMemesFromDB(): List<MemeEntity> {
        val list = memeDAO.getMemesFromDatabase()
        val emptyList = listOf(MemeEntity("-","No Favorites Found"))
        return list.ifEmpty {
            emptyList
        }

    }


}
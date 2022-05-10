package com.example.memescollection.model.remote

import com.example.memescollection.common.END_POINT
import com.example.memescollection.model.MemesResponse
import retrofit2.Response
import retrofit2.http.GET


interface MemesApiService {

    @GET(END_POINT)
    suspend fun getMemes(): Response<MemesResponse>
}
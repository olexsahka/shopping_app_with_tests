package com.example.testsmitch.data.remote

import com.example.testsmitch.BuildConfig
import com.example.testsmitch.data.remote.responses.ImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayAPI {
    @GET("/api/")
    suspend fun searchForImage(
        @Query("q") searchQuery: String,
        @Query("key") api: String = BuildConfig.API_KEY
    ) : Response<ImageResponse>
}
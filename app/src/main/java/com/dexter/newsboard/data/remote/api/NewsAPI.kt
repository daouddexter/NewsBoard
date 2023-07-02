package com.dexter.newsboard.data.remote.api

import com.dexter.newsboard.BuildConfig
import com.dexter.newsboard.data.remote.api.model.HeadlineAPIData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("top-headlines")
    suspend fun fetchHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apikey: String = BuildConfig.NEWS_API_KEY
    ): Response<HeadlineAPIData>
}
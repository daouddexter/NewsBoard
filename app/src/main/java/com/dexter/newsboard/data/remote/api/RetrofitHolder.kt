package com.dexter.newsboard.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitHolder {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(APIConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val newsAPI = retrofit.create<NewsAPI>()
}
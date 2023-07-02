package com.dexter.newsboard.data.remote.datasource

import android.util.Log
import com.dexter.newsboard.data.remote.api.NewsAPI
import com.dexter.newsboard.data.remote.api.model.HeadlineAPIData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NewsRemoteDataSource(
    private val newsAPI: NewsAPI,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun fetchHeadlines(country: String): HeadlineAPIData? =
        withContext(ioDispatcher) {
            newsAPI.fetchHeadlines(country).let { response ->
                if (response.isSuccessful) {
                    Log.i("NewsRemoteDataSource", "Fetch headlines successfull")
                } else {
                    Log.e("NewsRemoteDataSource", "Error fetching headlines")
                    response.errorBody()?.toString()?.let { Log.e("NewsRemoteDataSource", it) }
                }
                response.body()
            }
        }

}
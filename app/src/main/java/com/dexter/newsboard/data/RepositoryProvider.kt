package com.dexter.newsboard.data

import com.dexter.newsboard.data.converters.NewsDataPreferences
import com.dexter.newsboard.data.local.NewsDatabase
import com.dexter.newsboard.data.local.datasource.NewsLocalDataSource
import com.dexter.newsboard.data.remote.api.RetrofitHolder
import com.dexter.newsboard.data.remote.datasource.NewsRemoteDataSource
import com.dexter.newsboard.data.repository.NewsRepository
import com.dexter.newsboard.data.repository.Repository
import kotlinx.coroutines.Dispatchers

object RepositoryProvider {

    const val NEWS_REPOSITORY = "NEWS_REPOSITORY"

    private val ioDispatcher = Dispatchers.IO

    private val newsRemoteDataSource = NewsRemoteDataSource(RetrofitHolder.newsAPI, ioDispatcher)
    private val newsLocalDataSource = NewsLocalDataSource(
        NewsDatabase.newsDB.headlinesDao(),
        ioDispatcher
    )
    private val newsDataPreferences = NewsDataPreferences()

    private val serviceMap = mapOf<String, Repository>((NEWS_REPOSITORY to buildNewsRepository()))

    private fun buildNewsRepository(): NewsRepository = NewsRepository(
        newsRemoteDataSource = newsRemoteDataSource,
        newsLocalDataSource = newsLocalDataSource,
        newsDataPreferences = newsDataPreferences
    )

    fun getRepository(name: String): Repository? {
        return serviceMap[name]
    }

}
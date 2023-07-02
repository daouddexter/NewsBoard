package com.dexter.newsboard.data.repository

import com.dexter.newsboard.app.NewsApp
import com.dexter.newsboard.data.converters.NewsDataPreferences
import com.dexter.newsboard.data.converters.toArticleList
import com.dexter.newsboard.data.converters.toHeadlineEntities
import com.dexter.newsboard.data.local.datasource.NewsLocalDataSource
import com.dexter.newsboard.data.remote.datasource.NewsRemoteDataSource
import com.dexter.newsboard.data.remote.datasource.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsDataPreferences: NewsDataPreferences,
    private val externalScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : Repository {

    companion object {
        private const val MAX_SYNC_DIFFERENCE = 2 * 60 * 60 * 1000
    }

    private suspend fun refreshHeadlines() {
        newsRemoteDataSource.fetchHeadlines("in")?.let { headlineAPIData ->
            newsLocalDataSource.deleteAllHeadlines()
            newsLocalDataSource.insertAllHeadlines(headlineAPIData.toHeadlineEntities())
            newsDataPreferences.setLastSyncTime(NewsApp.appContext, System.currentTimeMillis())
        }
    }

    suspend fun getLatestHeadlines(refresh: Boolean): Flow<List<Article>> {
        var shouldRefresh = false
        if (refresh || newsDataPreferences.getLastSyncTime(NewsApp.appContext) < (System.currentTimeMillis() - MAX_SYNC_DIFFERENCE)) {
            shouldRefresh = true
        }
        if (shouldRefresh) {
            return externalScope.async {
                refreshHeadlines()
                return@async newsLocalDataSource.getAllHeadlines().map {
                    it.toArticleList()
                }
            }.await()
        } else {
            return newsLocalDataSource.getAllHeadlines().map {
                it.toArticleList()
            }
        }
    }

}
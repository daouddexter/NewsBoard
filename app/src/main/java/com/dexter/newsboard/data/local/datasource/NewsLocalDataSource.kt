package com.dexter.newsboard.data.local.datasource

import com.dexter.newsboard.data.local.dao.HeadlinesDao
import com.dexter.newsboard.data.local.entity.HeadlineEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NewsLocalDataSource(
    private val headlinesDao: HeadlinesDao, private val ioDispatcher: CoroutineDispatcher
) {

    fun getAllHeadlines(): Flow<List<HeadlineEntity>> = headlinesDao.getAllHeadlines()

    suspend fun insertAllHeadlines(headlines: List<HeadlineEntity>) {
        withContext(ioDispatcher) {
            headlinesDao.insertAll(headlines)
        }
    }

    suspend fun deleteAllHeadlines() {
        withContext(ioDispatcher) {
            headlinesDao.deleteAll()
        }
    }


}
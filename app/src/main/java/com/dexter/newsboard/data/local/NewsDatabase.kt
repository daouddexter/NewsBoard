package com.dexter.newsboard.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dexter.newsboard.app.NewsApp
import com.dexter.newsboard.data.local.dao.HeadlinesDao
import com.dexter.newsboard.data.local.entity.HeadlineEntity

@Database(
    entities = [HeadlineEntity::class],
    version = DatabaseConstants.NEWS_DB_VERSION,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun headlinesDao(): HeadlinesDao


    companion object {
        val newsDB : NewsDatabase by lazy {
            Room.databaseBuilder(NewsApp.appContext,NewsDatabase::class.java,
                DatabaseConstants.NEWS_DB_NAME)
                .build()
        }
    }
}
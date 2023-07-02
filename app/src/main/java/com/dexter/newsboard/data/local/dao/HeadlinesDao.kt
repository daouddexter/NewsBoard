package com.dexter.newsboard.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dexter.newsboard.data.local.DatabaseConstants
import com.dexter.newsboard.data.local.entity.HeadlineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeadlinesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(headlineEntity: HeadlineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(headlineEntity: List<HeadlineEntity>)

    @Query("SELECT * FROM ${DatabaseConstants.HEADLINES_TABLE_NAME} ORDER BY publishedAt DESC")
    fun getAllHeadlines(): Flow<List<HeadlineEntity>>

    @Query("DELETE FROM ${DatabaseConstants.HEADLINES_TABLE_NAME}")
    suspend fun deleteAll()
}
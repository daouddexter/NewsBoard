package com.dexter.newsboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dexter.newsboard.data.local.DatabaseConstants

@Entity(tableName = DatabaseConstants.HEADLINES_TABLE_NAME)
data class HeadlineEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val source: String?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

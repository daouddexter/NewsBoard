package com.dexter.newsboard.data.remote.api.model

data class HeadlineAPIData(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleAPIData>
)

data class ArticleAPIData(
    val source: SourceAPIData,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)

data class SourceAPIData(val id: String, val name: String)

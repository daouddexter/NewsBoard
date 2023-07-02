package com.dexter.newsboard.data.remote.datasource.model

data class Article( val id: Int,
                    val source: String?,
                    val author: String?,
                    val title: String,
                    val description: String?,
                    val url: String?,
                    val urlToImage: String?,
                    val timePassed: String,
                    val content: String?)
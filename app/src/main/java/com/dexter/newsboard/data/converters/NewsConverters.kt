package com.dexter.newsboard.data.converters

import android.icu.text.SimpleDateFormat
import com.dexter.newsboard.data.local.entity.HeadlineEntity
import com.dexter.newsboard.data.remote.api.model.HeadlineAPIData
import com.dexter.newsboard.data.remote.datasource.model.Article
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun HeadlineAPIData.toHeadlineEntities(): List<HeadlineEntity> =
    this.articles.map { article ->
        HeadlineEntity(
            source = article.source.name,
            author = article.author,
            title = article.title,
            description = article.description,
            url = article.url,
            urlToImage = article.urlToImage,
            publishedAt = dateToEpoch(article.publishedAt),
            content = article.content
        )
    }


private fun dateToEpoch(dateString: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    val date = dateFormat.parse(dateString) ?: Date()
    return date.time.toString()
}

fun List<HeadlineEntity>.toArticleList(): List<Article> =
    this.filter {
        it.id != null && it.title != null && it.publishedAt != null
    }.map {
        Article(
            source = it.source,
            author = it.author,
            title = it.title!!,
            description = it.description,
            url = it.url,
            urlToImage = it.urlToImage,
            timePassed = it.publishedAt!!.getTimePassed(),
            content = it.content,
            id = it.id!!
        )
    }


fun String.getTimePassed(): String {

    val timeDiffInMillis = System.currentTimeMillis() - this.toLong()
    return when {
        timeDiffInMillis >= TimeUnit.HOURS.toMillis(1) -> {
            val hours = TimeUnit.MILLISECONDS.toHours(timeDiffInMillis)
            "$hours h"
        }

        timeDiffInMillis >= TimeUnit.MINUTES.toMillis(1) -> {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiffInMillis)
            "$minutes m"
        }

        else -> "now"
    }
}

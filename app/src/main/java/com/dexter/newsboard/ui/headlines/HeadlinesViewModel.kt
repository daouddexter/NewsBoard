package com.dexter.newsboard.ui.headlines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dexter.newsboard.data.RepositoryProvider
import com.dexter.newsboard.data.remote.datasource.model.Article
import com.dexter.newsboard.data.repository.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class HeadlinesViewModel : ViewModel() {
    private val newsRepository: NewsRepository =
        RepositoryProvider.getRepository(RepositoryProvider.NEWS_REPOSITORY) as NewsRepository

    private val _headlineState = MutableStateFlow(HeadlinesUIState())
    val headlinesState = _headlineState as SharedFlow<HeadlinesUIState>

    fun getHeadlines(refresh: Boolean = false) {
        viewModelScope.launch {
            _headlineState.update {
                it.copy(isLoading = true)
            }
            newsRepository.getLatestHeadlines(refresh)
                .catch { error ->
                    _headlineState.update {
                        it.copy(isLoading = false)
                    }
                    Log.e("HeadlinesViewModel", "Failed to get headlines", error)
                }
                .collect { articles ->
                    _headlineState.update {
                        it.copy(articles = articles, isLoading = false)
                    }
                }
        }

    }

    fun refreshHeadlines() {
        getHeadlines(true)
    }
}

data class HeadlinesUIState(val articles: List<Article> = listOf(), val isLoading: Boolean = false)
package com.example.firebasetestingapp.core.news

import com.example.firebasetestingapp.core.news.data_sources.NewsLocalDataSource
import com.example.firebasetestingapp.core.news.data_sources.NewsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val remote: NewsRemoteDataSource,
    private val local: NewsLocalDataSource
) {

    companion object {
        const val newsTable = "News"
    }

    fun saveToUserArticles(article: News) {
        remote.saveToUserArticles(article)
    }

    fun getDetailedArticle(articleId: String): Flow<News?> {
        return local.getDetailedArticle(articleId)
    }

    fun observeUserNews(): Flow<List<News>> {
        return local.getUserNews()
    }
}
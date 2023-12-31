package com.example.firebasetestingapp.ui.news

import com.example.firebasetestingapp.ui.news.data_sources.NewsLocalDataSource
import com.example.firebasetestingapp.ui.news.data_sources.NewsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Singleton
class NewsRepository() {

    private val remote: NewsRemoteDataSource = NewsRemoteDataSource()
    private val local: NewsLocalDataSource = NewsLocalDataSource()

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
        return flow { local.getUserNews() }
    }
}
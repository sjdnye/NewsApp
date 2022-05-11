package com.example.newsappmvvm.ui.repository

import com.example.newsappmvvm.ui.api.RetrofitInstance
import com.example.newsappmvvm.ui.database.ArticleDatabase
import com.example.newsappmvvm.ui.models.Article

class NewsRepository(private val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchForNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsertArticle(article)

    fun getSaveNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}

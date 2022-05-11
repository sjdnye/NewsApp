package com.example.newsappmvvm.ui.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsappmvvm.ui.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticle(article: Article) : Long

    @Query("SELECT * FROM articles")
    fun getAllArticles() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}
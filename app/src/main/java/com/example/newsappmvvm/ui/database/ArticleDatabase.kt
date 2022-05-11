package com.example.newsappmvvm.ui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsappmvvm.ui.models.Article

@Database(
    entities = [Article::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase(){

    abstract fun getArticleDao() : ArticleDao

    companion object{
        @Volatile
        private var INSTANCE : ArticleDatabase? = null

        fun getInstance(context: Context): ArticleDatabase{
            kotlin.synchronized(this){
                return INSTANCE ?:  Room.databaseBuilder(
                    context.applicationContext,
                    ArticleDatabase::class.java,
                    "article_db.db"
                ).build().also {
                    INSTANCE = it
                }

            }

        }

    }
}
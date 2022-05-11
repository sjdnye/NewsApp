package com.example.newsappmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsappmvvm.R
import com.example.newsappmvvm.ui.database.ArticleDatabase
import com.example.newsappmvvm.ui.repository.NewsRepository
import com.example.newsappmvvm.ui.uI.NewsViewModel
import com.example.newsappmvvm.ui.uI.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

//        val newsRepository = NewsRepository(ArticleDatabase.getInstance(this))
//        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
//        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]
        supportActionBar?.title = "News App"
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}
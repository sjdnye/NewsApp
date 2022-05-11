package com.example.newsappmvvm.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.newsappmvvm.R
import com.example.newsappmvvm.ui.NewsActivity
import com.example.newsappmvvm.ui.database.ArticleDatabase
import com.example.newsappmvvm.ui.models.Article
import com.example.newsappmvvm.ui.repository.NewsRepository
import com.example.newsappmvvm.ui.uI.NewsViewModel
import com.example.newsappmvvm.ui.uI.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var viewModel: NewsViewModel
    private val args by navArgs<ArticleFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val newsRepository = NewsRepository(ArticleDatabase.getInstance(requireContext()))
        val viewModelProviderFactory = NewsViewModelProviderFactory(requireActivity().application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        val article = args.article
        article.let {
            webView.apply {
                webViewClient = WebViewClient()
                loadUrl(it.url)
            }
        }

        fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article added successfully!",Snackbar.LENGTH_SHORT).show()
        }


    }
}
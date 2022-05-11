package com.example.newsappmvvm.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappmvvm.R
import com.example.newsappmvvm.ui.adapters.NewsAdapter
import com.example.newsappmvvm.ui.database.ArticleDatabase
import com.example.newsappmvvm.ui.repository.NewsRepository
import com.example.newsappmvvm.ui.uI.NewsViewModel
import com.example.newsappmvvm.ui.uI.NewsViewModelProviderFactory
import com.example.newsappmvvm.ui.utils.Constants
import com.example.newsappmvvm.ui.utils.Resource
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.newsappmvvm.ui.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel = (activity as NewsActivity).viewModel
        val newsRepository = NewsRepository(ArticleDatabase.getInstance(requireContext()))
        val viewModelProviderFactory = NewsViewModelProviderFactory(requireActivity().application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        setUpRecyclerView()
        newsAdapter.setOnItemClickListener {
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it)
            Navigation.findNavController(view).navigate(action)
        }

        var job : Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(this, Observer{ response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPage = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPage
                        if(isLastPage){
                            rvSearchNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), "An error occurred: $message", Toast.LENGTH_LONG ).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }

            }

        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.getSearchNews(etSearch.text.toString())
                isScrolling = false
            }
        }
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }
}
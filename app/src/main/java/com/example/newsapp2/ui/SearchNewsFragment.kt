package com.example.newsapp2.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp2.R
import com.example.newsapp2.adapters.NewsAdapter
import com.example.newsapp2.api.NewsResponse
import com.example.newsapp2.mvvm.NewsViewModel
import com.example.newsapp2.databinding.FragmentSearchNewsBinding
import com.example.newsapp2.util.AppConstants.QUERY_PAGE_SIZE
import com.example.newsapp2.util.OnItemClickListener
import com.example.newsapp2.util.Resource

class SearchNewsFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentSearchNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private lateinit var handler: Handler

    var isLoading = false
    var isLastPage = false
    var isScrolling = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRV()

        handler = Handler(Looper.getMainLooper())

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                handler.postDelayed(
                    {
                        viewModel.searchNews(p0.toString())
                    }, 500
                )
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        viewModel.searchNews.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    hideProgressBar()
                    Log.e("SearchNewsFragment", "An error occured: ${it.message}")
                }

                is Resource.Loading -> showProgressBar()

                is Resource.Success -> {
                    hideProgressBar()
                    newsAdapter.differ.submitList(it.data?.articles)
                    val totalPages = it.data!!.totalResults / QUERY_PAGE_SIZE + 2
                    isLastPage = viewModel.searchNewsPage == totalPages
                    if (isLastPage) {
                        binding.rvSearchNews.setPadding(0, 0, 0, 0)
                    }
                }
            }
        }
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.searchNews(binding.etSearch.text.toString())
                isScrolling = false
            } else {
                binding.rvSearchNews.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }


    private fun setupRV() {
        newsAdapter = NewsAdapter(this)
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }

    override fun onItemClick(position: Int, data: Any) {
        when (data) {
            is NewsResponse.Article -> {

                val bundle = Bundle().apply {
                    putString("article_url", data.url)
                }
                findNavController().navigate(
                    R.id.action_searchNewsFragment_to_articleFragment,
                    bundle
                )
            }
        }
    }

}
package com.example.newsapp2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp2.R
import com.example.newsapp2.mvvm.NewsViewModel
import com.example.newsapp2.adapters.NewsAdapter
import com.example.newsapp2.api.NewsResponse
import com.example.newsapp2.databinding.FragmentSavedNewsBinding
import com.example.newsapp2.util.OnItemClickListener

class SavedNewsFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentSavedNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setUpRV()

        viewModel.getAllSavedArticles().observe(viewLifecycleOwner) {
            newsAdapter.differ.submitList(it)
        }
    }

    private fun setUpRV() {
        newsAdapter = NewsAdapter(this)
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onItemClick(position: Int, data: Any) {
        when (data) {
            is NewsResponse.Article -> {
                val bundle = Bundle().apply {
                    data.id?.let { putInt("article_id", it) }
                    putString("article_url", data.url)
                    putString("article_urlToImage", data.urlToImage)
                    putString("article_author", data.author)
                    putString("article_content", data.content)
                    putString("article_description", data.description)
                    putString("article_publishedAt", data.publishedAt)
                    putString("article_title", data.title)
                    putString("article_source_id", data.source!!.id)
                    putString("article_source_name", data.source.name)
                }
                findNavController().navigate(
                    R.id.action_savedNewsFragment_to_articleFragment, bundle
                )
            }
        }


    }

}
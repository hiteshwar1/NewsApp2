package com.example.newsapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.newsapp2.api.NewsResponse
import com.example.newsapp2.mvvm.NewsViewModel
import com.example.newsapp2.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        val id = arguments?.getInt("article_id")
        val url = arguments?.getString("article_url")
        val urlToImage = arguments?.getString("article_urlToImage")
        val author = arguments?.getString("article_author")
        val content = arguments?.getString("article_content")
        val description = arguments?.getString("article_description")
        val title = arguments?.getString("article_title")
        val source_id = arguments?.getString("article_source_id")
        val source_name = arguments?.getString("article_source_name")
        val publishedAt = arguments?.getString("article_publishedAt")

        binding.webView.apply {
            webViewClient = WebViewClient()
            url?.let { loadUrl(it) }
        }

        binding.fab.setOnClickListener {
            viewModel.insertArticle(
                NewsResponse.Article(
                    id = id,
                    author = author,
                    content = content,
                    description = description,
                    publishedAt = publishedAt,
                    source = source_id?.let { it1 -> source_name?.let { it2 -> NewsResponse.Article.Source(id = it1, name = it2) } },
                    title = title,
                    url = url,
                    urlToImage = urlToImage
                )
            )
            Toast.makeText(requireContext(), "Article Saved", Toast.LENGTH_SHORT).show()
        }

    }


}
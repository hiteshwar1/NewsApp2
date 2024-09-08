package com.example.newsapp2.mvvm

import com.example.newsapp2.api.NewsResponse
import com.example.newsapp2.api.RetrofitService
import com.example.newsapp2.room.ArticleDataBase
import javax.inject.Inject

class NewsRepository (private val retrofitService: RetrofitService, val articleDataBase: ArticleDataBase) {

    suspend fun getBreakingNews(query: String) = retrofitService.getNews(query)

    suspend fun searchNews(searchQuery:String) = retrofitService.searchNews(searchQuery)

    suspend fun insertArticle(article: NewsResponse.Article) = articleDataBase.articleDao().insertArticle(article)

    suspend fun deleteArticle(article: NewsResponse.Article) = articleDataBase.articleDao().deleteArticle(article)

    fun getAllArticles() = articleDataBase.articleDao().getAllArticles()
}
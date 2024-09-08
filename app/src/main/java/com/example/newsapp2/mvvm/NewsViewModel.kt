package com.example.newsapp2.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp2.api.NewsResponse
import com.example.newsapp2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

//@HiltViewModel
class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    val breakingNews = MutableLiveData<Resource<NewsResponse>>()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews = MutableLiveData<Resource<NewsResponse>>()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(query: String) = viewModelScope.launch(Dispatchers.IO) {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(query)
//        if (response.isSuccessful) {
//            breakingNews.postValue(Resource.Success(response.body()!!))
//        }
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            breakingNewsPage++
            if (breakingNewsResponse == null) {
                breakingNewsResponse = response.body()
            } else {
                val oldArticles = breakingNewsResponse?.articles as MutableList
                val newArticles = response.body()?.articles
                if (newArticles != null) {
                    oldArticles.addAll(newArticles)
                }
            }
            return Resource.Success(breakingNewsResponse?: response.body()!!)
        }
        return Resource.Error(response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            searchNewsPage++
            if (searchNewsResponse == null) {
                searchNewsResponse = response.body()
            } else {
                val oldArticles = searchNewsResponse?.articles as MutableList
                val newArticles = response.body()?.articles
                if (newArticles != null) {
                    oldArticles.addAll(newArticles)
                }
            }
            return Resource.Success(searchNewsResponse?: response.body()!!)
        }
        return Resource.Error(response.message())
    }

    fun insertArticle(article: NewsResponse.Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.insertArticle(article)
    }

    fun deleteArticle(article: NewsResponse.Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.deleteArticle(article)
    }

    fun getAllSavedArticles() = newsRepository.getAllArticles()


}
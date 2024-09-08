package com.example.newsapp2.api

import com.example.newsapp2.util.AppConstants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country") country: String, @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String, @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>

}
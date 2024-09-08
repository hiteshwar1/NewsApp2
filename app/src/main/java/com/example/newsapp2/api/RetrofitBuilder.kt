package com.example.newsapp2.api

import com.example.newsapp2.util.AppConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    companion object {
        private var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                retrofitService =
                    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL).build().create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }

}
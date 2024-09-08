package com.example.newsapp2.api

import androidx.room.Entity
import androidx.room.PrimaryKey

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
) {
    @Entity(tableName = "articles")
    data class Article(
        @PrimaryKey(autoGenerate = true) val id: Int? = null,
        val author: String?,
        val content: String?,
        val description: String?,
        val publishedAt: String?,
        val source: Source?,
        val title: String?,
        val url: String?,
        val urlToImage: String? = null
    ) {
        data class Source(
            val id: String,
            val name: String
        )
    }
}
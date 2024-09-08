package com.example.newsapp2.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp2.api.NewsResponse


@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: NewsResponse.Article):Long

    @Query("Select * from articles")
    fun getAllArticles(): LiveData<List<NewsResponse.Article>>

    @Delete
    suspend fun deleteArticle(article: NewsResponse.Article)
}
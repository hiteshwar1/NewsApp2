package com.example.newsapp2.room

import androidx.room.TypeConverter
import com.example.newsapp2.api.NewsResponse

class Converters {

    @TypeConverter
    fun fromSource(source: NewsResponse.Article.Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): NewsResponse.Article.Source {
        return NewsResponse.Article.Source(name, name)
    }
}
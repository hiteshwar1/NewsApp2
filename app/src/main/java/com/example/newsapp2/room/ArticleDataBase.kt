package com.example.newsapp2.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp2.api.NewsResponse

@Database(entities = [NewsResponse.Article::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArticleDataBase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile // Other threads can immediately see changes
        private var instance: ArticleDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): ArticleDataBase =
            instance ?: synchronized(LOCK) {
                instance ?: createDataBase(context).also { instance = it }
            }

        private fun createDataBase(context: Context): ArticleDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                ArticleDataBase::class.java,
                "article_db.db"
            ).build()
        }
    }
}

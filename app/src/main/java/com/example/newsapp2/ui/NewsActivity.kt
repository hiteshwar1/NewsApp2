package com.example.newsapp2.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp2.mvvm.NewsRepository
import com.example.newsapp2.mvvm.NewsViewModel
import com.example.newsapp2.mvvm.NewsViewModelFactory
import com.example.newsapp2.util.OnItemClickListener
import com.example.newsapp2.R
import com.example.newsapp2.api.RetrofitBuilder
import com.example.newsapp2.databinding.ActivityNewsBinding
import com.example.newsapp2.room.ArticleDataBase
import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var viewModelFactory: NewsViewModelFactory
    private lateinit var repository: NewsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as? NavHostFragment
        val navController = navHostFragment?.navController
        if (navController != null) {
            binding.bottomNavigationView.setupWithNavController(navController)
        } else {
            Log.i("NewsActivity", "NavController is null")
        }
    }

    private fun init() {
        repository = NewsRepository(RetrofitBuilder.getInstance(), ArticleDataBase(this))
        viewModelFactory = NewsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
package com.example.newsapp2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.util.Consumer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp2.util.OnItemClickListener
import com.example.newsapp2.R
import com.example.newsapp2.api.NewsResponse
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso

class NewsAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivArticleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)
        val tvSource: TextView = itemView.findViewById(R.id.tvSource)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
        val clArticle: ConstraintLayout = itemView.findViewById(R.id.clArticlePreview)
    }

    val diffCallback = object : DiffUtil.ItemCallback<NewsResponse.Article>() {
        override fun areItemsTheSame(
            oldItem: NewsResponse.Article,
            newItem: NewsResponse.Article
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: NewsResponse.Article,
            newItem: NewsResponse.Article
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article_preview, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.apply {
            if(!item.urlToImage.isNullOrEmpty()){
                Picasso.get().load(item.urlToImage).into(ivArticleImage)
            }
            tvSource.text = item.source!!.name
            tvTitle.text = item.title
            tvDescription.text = item.description
            tvPublishedAt.text = item.publishedAt
            clArticle.setOnClickListener {
                listener.onItemClick(position, item)
            }
        }
    }


}
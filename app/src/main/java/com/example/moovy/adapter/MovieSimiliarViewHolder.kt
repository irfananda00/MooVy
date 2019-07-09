package com.example.moovy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moovy.R
import com.example.moovy.model.ListMovie
import com.example.moovy.model.SimiliarMovies
import com.squareup.picasso.Picasso

class MovieSimiliarViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_movie_similiar, parent, false)){

    private var mImage: ImageView? = null
    private var mTitle: TextView? = null

    init {
        mImage = itemView.findViewById(R.id.cardPoster)
        mTitle = itemView.findViewById(R.id.txtTitle)
    }

    fun bind(model: SimiliarMovies.Result, clickListener: (SimiliarMovies.Result) -> Unit) {
        mTitle?.text = model.title
        Picasso.get().load("https://image.tmdb.org/t/p/w300_and_h450_bestv2"+model.poster_path).into(mImage)
        itemView.setOnClickListener{
            clickListener(model)
        }
    }
}
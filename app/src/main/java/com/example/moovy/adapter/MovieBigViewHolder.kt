package com.example.moovy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moovy.R
import com.example.moovy.model.ListMovie
import com.squareup.picasso.Picasso

class MovieBigViewHolder(inflater: LayoutInflater, parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_movie_big, parent, false)){

    private var mImage: ImageView? = null
    private var mTitle: TextView? = null
    private var mRating: TextView? = null

    init {
        mImage = itemView.findViewById(R.id.cardPoster)
        mTitle = itemView.findViewById(R.id.txtTitle)
        mRating = itemView.findViewById(R.id.txtRating)
    }

    fun bind(model: ListMovie.Result, clickListener: (ListMovie.Result) -> Unit) {
        mTitle?.text = model.title
        mRating?.text = model.vote_average.toString()
        Picasso.get().load("https://image.tmdb.org/t/p/w600_and_h900_bestv2"+model.poster_path).into(mImage)
        itemView.setOnClickListener{
            clickListener(model)
        }
    }
}
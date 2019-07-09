package com.example.moovy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moovy.model.SimiliarMovies

class ListSimiliarAdapter(
    val list: List<SimiliarMovies.Result>,
    val clickListener: (SimiliarMovies.Result) -> Unit): RecyclerView.Adapter<MovieSimiliarViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSimiliarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieSimiliarViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewHolder: MovieSimiliarViewHolder, position: Int) {
        val movie: SimiliarMovies.Result = list[position]
        viewHolder.bind(movie,clickListener)
    }

}
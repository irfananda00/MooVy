package com.example.moovy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moovy.model.ListMovie

class ListSmallVerticalAdapter(
    val list: List<ListMovie.Result>,
    val clickListener: (ListMovie.Result) -> Unit): RecyclerView.Adapter<MovieSmallViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSmallViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieSmallViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MovieSmallViewHolder, position: Int) {
        val movie: ListMovie.Result = list[position]
        holder.bind(movie,clickListener)
    }

}
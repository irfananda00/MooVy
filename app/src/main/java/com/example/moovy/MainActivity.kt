package com.example.moovy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import androidx.transition.Visibility
import com.example.moovy.adapter.ListBigVerticalAdapter
import com.example.moovy.adapter.ListSmallVerticalAdapter
import com.example.moovy.model.ListMovie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import com.example.moovy.adapter.EndlessRecyclerViewScrollListener



class MainActivity : AppCompatActivity() {

    private lateinit var listNowPlaying: ArrayList<ListMovie.Result>
    private lateinit var listUpcoming: ArrayList<ListMovie.Result>
    private lateinit var listSearch: ArrayList<ListMovie.Result>

    private lateinit var adapterNowPlaying: ListBigVerticalAdapter
    private lateinit var adapterUpcoming: ListSmallVerticalAdapter
    private lateinit var adapterSearch: ListSmallVerticalAdapter

    private lateinit var layoutManagerSearch: GridLayoutManager

    private val mContext = this
    private var isSearch = false
    private var pageSearch = 1
    private var maxPage = 1

    private lateinit var constraint1: ConstraintSet
    private lateinit var constraint2: ConstraintSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        constraint1 = ConstraintSet()
        constraint1.clone(root)
        constraint2 = ConstraintSet()
        constraint2.clone(this, R.layout.activity_main_search)

        btnCloseSearch.visibility = View.GONE

        rvNowPlaying.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
            // set the custom adapter to the RecyclerView
            listNowPlaying = ArrayList()
            adapterNowPlaying = ListBigVerticalAdapter(listNowPlaying, { movie : ListMovie.Result -> movieClicked(movie) })
            adapter = adapterNowPlaying
            val helper = LinearSnapHelper()
            helper.attachToRecyclerView(rvNowPlaying)
        }
        rvComingSoon.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
            // set the custom adapter to the RecyclerView
            listUpcoming = ArrayList()
            adapterUpcoming = ListSmallVerticalAdapter(listUpcoming, { movie : ListMovie.Result -> movieClicked(movie) })
            adapter = adapterUpcoming
            val helper = LinearSnapHelper()
            helper.attachToRecyclerView(rvComingSoon)
        }
        rvSearchMovie.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManagerSearch = GridLayoutManager(mContext, 2)
            layoutManager = layoutManagerSearch
            // set the custom adapter to the RecyclerView
            listSearch = ArrayList()
            adapterSearch = ListSmallVerticalAdapter(listSearch, { movie : ListMovie.Result -> movieClicked(movie) })
            adapter = adapterSearch
        }
        rvSearchMovie.addOnScrollListener(object: EndlessRecyclerViewScrollListener(layoutManagerSearch) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                pageSearch++
                if (page <= maxPage) {
                    Log.i("irfananda","load page: "+page)
                    searchMovies("136451254291f50e7661446b9450ede6", pageSearch, edtSearch.text.toString())
                }
            }
        })

        btnSearch.setOnClickListener {
            animateSearch()
            searchMovies("136451254291f50e7661446b9450ede6",pageSearch, edtSearch.text.toString())
        }
        btnCloseSearch.setOnClickListener {
            reanimateSearch()
        }

        loadNowPlaying("136451254291f50e7661446b9450ede6",1)
        loadUpcoming("136451254291f50e7661446b9450ede6",1)
    }

    private fun animateSearch() {
        if(!isSearch) {
            TransitionManager.beginDelayedTransition(root)
            val constraint = constraint2
            constraint.applyTo(root)
            isSearch = !isSearch
            btnCloseSearch.visibility = View.VISIBLE
        }
    }

    private fun reanimateSearch() {
        TransitionManager.beginDelayedTransition(root)
        val constraint = constraint1
        constraint.applyTo(root)
        isSearch = !isSearch
        btnCloseSearch.visibility = View.GONE
    }

    private fun searchMovies(apiKey: String, page: Int, query: String) {
        Log.i("irfananda","search movies query: "+query)
        APIService.disposable = APIService.APIserve.getSearchMovie(apiKey,page,query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result -> showResultSearch(result.results, result.total_pages)},
                {error -> Log.e("irfananda",error.message)}
            )
    }

    private fun showResultSearch(results: ArrayList<ListMovie.Result>, totalPages: Int) {
        Log.i("irfananda","total page search: "+totalPages)
        listSearch.addAll(results)
        adapterSearch.notifyDataSetChanged()
        maxPage = totalPages
    }

    private fun loadNowPlaying(apiKey: String, page: Int) {
        Log.i("irfananda","search now playing")
        APIService.disposable = APIService.APIserve.getNowPlaying(apiKey,page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result -> showResultNowPlaying(result.results, result.total_pages)},
                {error -> Log.e("irfananda",error.message)}
            )
    }

    private fun showResultNowPlaying(results: ArrayList<ListMovie.Result>, totalPages: Int) {
        Log.i("irfananda","total page nowplaying: "+totalPages)
        listNowPlaying.addAll(results)
        adapterNowPlaying.notifyDataSetChanged()
    }

    private fun loadUpcoming(apiKey: String, page: Int) {
        Log.i("irfananda","search upcoming")
        APIService.disposable = APIService.APIserve.getUpcoming(apiKey,page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result -> showResultUpcoming(result.results, result.total_pages)},
                {error -> Log.e("irfananda",error.message)}
            )
    }

    private fun showResultUpcoming(results: ArrayList<ListMovie.Result>, totalPages: Int) {
        Log.i("irfananda","total page upcoming: "+totalPages)
        listUpcoming.addAll(results)
        adapterUpcoming.notifyDataSetChanged()
    }

    private fun movieClicked(movie: ListMovie.Result) {
        val intent = Intent(this, DetailMovieActivity::class.java)
        Log.i("irfananda","movie id: "+movie.id)
        intent.putExtra("movie_id",movie.id.toString())
        startActivity(intent)
    }
}

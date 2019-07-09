package com.example.moovy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.moovy.adapter.ListSimiliarAdapter
import com.example.moovy.model.MovieDetail
import com.example.moovy.model.MovieTrailer
import com.example.moovy.model.SimiliarMovies
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var listSimiliar: ArrayList<SimiliarMovies.Result>
    private val mContext = this
    private lateinit var adapterSimiliar: ListSimiliarAdapter
    var trailerURL = ""
    var trailerKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        btnBack.setOnClickListener {
            finish()
        }

        btnTrailer.setOnClickListener {
            //open in chrome
//            val builder = CustomTabsIntent.Builder()
//            builder.setToolbarColor(resources.getColor(R.color.colorPrimary))
//            val customTabsIntent = builder.build()
//            customTabsIntent.launchUrl(this, Uri.parse(trailerURL))
            //open in youtube
            val intent = Intent(this, WatchTrailerActivity::class.java)
            Log.i("irfananda","trailer id: "+trailerKey)
            intent.putExtra("trailer_id",trailerKey)
            startActivity(intent)
        }

        rvSimiliar.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
            // set the custom adapter to the RecyclerView
            listSimiliar = ArrayList()
            adapterSimiliar = ListSimiliarAdapter(listSimiliar, { movie : SimiliarMovies.Result -> movieClicked(movie) })
            adapter = adapterSimiliar
            val helper = LinearSnapHelper()
            helper.attachToRecyclerView(rvSimiliar)
        }

        loadMovieDetail("136451254291f50e7661446b9450ede6",intent.getStringExtra("movie_id"))
        getTrailerURL("136451254291f50e7661446b9450ede6",intent.getStringExtra("movie_id"))
        loadSimiliar("136451254291f50e7661446b9450ede6",1, intent.getStringExtra("movie_id"))
    }

    private fun loadSimiliar(apiKey: String, page: Int, movieID: String) {
        Log.i("irfananda","search similiar id: "+movieID)
        APIService.disposable = APIService.APIserve.getSimiliar(movieID,apiKey,page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result -> showResultSimiliar(result.results, result.total_pages)},
                {error -> Log.e("irfananda",error.message)}
            )
    }

    private fun showResultSimiliar(results: List<SimiliarMovies.Result>, totalPages: Int) {
        Log.i("irfananda","total page similiar: "+totalPages)
        listSimiliar.addAll(results)
        adapterSimiliar.notifyDataSetChanged()
    }

    private fun loadMovieDetail(api: String, id: String) {
        Log.i("irfananda","load detail movie")
        APIService.disposable = APIService.APIserve.getMovieDetail(id,api)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result -> showResult(result)},
                {error -> Log.e("irfananda",error.message)}
            )
    }

    private fun getTrailerURL(api: String, id: String) {
        Log.i("irfananda","load trailer movie")
        APIService.disposable = APIService.APIserve.getMovieTrailerURL(id,api)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {result -> saveTrailerURL(result)},
                {error -> Log.e("irfananda",error.message)}
            )
    }

    private fun saveTrailerURL(result: MovieTrailer) {
        trailerURL = "https://www.themoviedb.org/video/play?key="+result.results.get(result.results.size-1).key
        trailerKey = result.results.get(result.results.size-1).key
    }

    private fun showResult(result: MovieDetail) {
        txtTitle.text = result.title
        txtRating.text = result.vote_average.toString()
        txtDesc.text = result.overview
        txtDurationLanguage.text = (result.runtime/60).toString()+"h "+(result.runtime%60).toString()+"min"+" | "+result.spoken_languages.get(0).name
        var genres = ""
        for (genre in result.genres){
            genres = genres+genre.name+", "
        }
        genres = genres.removeRange(genres.length-2, genres.length-1)
        txtGenre.text = genres
        Picasso.get().load("https://image.tmdb.org/t/p/w300_and_h450_bestv2"+result.poster_path).into(imgPoster)
        Picasso.get().load("https://image.tmdb.org/t/p/w533_and_h300_bestv2"+result.backdrop_path).into(imgBackdrop)
    }

    private fun movieClicked(movie: SimiliarMovies.Result) {
        val intent = Intent(this, DetailMovieActivity::class.java)
        Log.i("irfananda","movie id: "+movie.id)
        intent.putExtra("movie_id",movie.id.toString())
        finish()
        startActivity(intent)
    }
}

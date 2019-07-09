package com.example.moovy

import com.example.moovy.model.MovieDetail
import com.example.moovy.model.MovieTrailer
import com.example.moovy.model.ListMovie
import com.example.moovy.model.SimiliarMovies
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService{
    @GET("movie/now_playing")
    fun getNowPlaying(
        @Query("api_key")apikey: String,
        @Query("page")page: Int
    ): Observable<ListMovie>

    @GET("movie/upcoming")
    fun getUpcoming(
        @Query("api_key")apikey: String,
        @Query("page")page: Int
    ): Observable<ListMovie>

    @GET("search/movie")
    fun getSearchMovie(
        @Query("api_key")apikey: String,
        @Query("page")page: Int,
        @Query("query")query: String
    ): Observable<ListMovie>

    @GET("movie/{movieid}/similar?")
    fun getSimiliar(
        @Path("movieid")movieId: String,
        @Query("api_key")apikey: String,
        @Query("page")page: Int
    ): Observable<SimiliarMovies>

    @GET("movie/{movieid}")
    fun getMovieDetail(
        @Path("movieid")movieId: String,
        @Query("api_key")apikey: String
    ): Observable<MovieDetail>

    @GET("movie/{movieid}/videos")
    fun getMovieTrailerURL(
        @Path("movieid")movieId: String,
        @Query("api_key")apikey: String
    ): Observable<MovieTrailer>

    companion object{
        fun create(): APIService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.themoviedb.org/3/")
                .build()

            return retrofit.create(APIService::class.java)
        }

        val APIserve by lazy {
            create()
        }

        var disposable: Disposable? = null

    }
}
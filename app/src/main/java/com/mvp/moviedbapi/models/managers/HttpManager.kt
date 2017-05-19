package com.mvp.moviedbapi.models.managers

import com.mvp.moviedbapi.constants.Urls
import com.mvp.moviedbapi.network.MovieSearchService

import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import com.mvp.moviedbapi.constants.Urls.MOVIEDB_BASE_URL

/**
 * Created by olivier.goutay on 4/28/17.
 */

class HttpManager
/**
 * Constructs the different services we use
 */
private constructor() {

    /**
     * The [MovieSearchService] instance
     */
    var movieSearchService: MovieSearchService

    init {
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Urls.MOVIEDB_BASE_URL)
                .build()
        movieSearchService = retrofit.create(MovieSearchService::class.java)
    }

    companion object {

        /**
         * The volatile static singleton of [HttpManager]
         */
        @Volatile private var sHttpManager: HttpManager? = null

        /**
         * Gets the singleton of the HttpManager, to avoid re-constructing retrofit etc...
         * @return The instance of [HttpManager]
         */
        val instance: HttpManager
            get() {
                synchronized(HttpManager::class.java) {
                    if (sHttpManager == null) {
                        sHttpManager = HttpManager()
                    }
                }
                return sHttpManager!!
            }
    }
}

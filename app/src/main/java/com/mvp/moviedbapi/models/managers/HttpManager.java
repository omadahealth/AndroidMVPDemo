package com.mvp.moviedbapi.models.managers;

import com.mvp.moviedbapi.network.MovieSearchService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mvp.moviedbapi.constants.Urls.MOVIEDB_BASE_URL;

/**
 * Created by olivier.goutay on 4/28/17.
 */

public final class HttpManager {

    /**
     * The volatile static singleton of {@link HttpManager}
     */
    private static volatile HttpManager sHttpManager;

    /**
     * The {@link MovieSearchService} instance
     */
    private MovieSearchService mMovieSearchService;

    /**
     * Constructs the different services we use
     */
    private HttpManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MOVIEDB_BASE_URL)
                .build();
        mMovieSearchService = retrofit.create(MovieSearchService.class);
    }

    /**
     * Gets the singleton of the HttpManager, to avoid re-constructing retrofit etc...
     * @return The instance of {@link HttpManager}
     */
    public static HttpManager getInstance() {
        synchronized (HttpManager.class) {
            if (sHttpManager == null) {
                sHttpManager = new HttpManager();
            }
        }
        return sHttpManager;
    }

    public MovieSearchService getMovieSearchService() {
        return mMovieSearchService;
    }

    public void setMovieSearchService(MovieSearchService movieSearchService) {
        this.mMovieSearchService = movieSearchService;
    }
}

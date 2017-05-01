package com.mvp.moviedbapi.network;

import com.mvp.moviedbapi.models.apis.SearchResults;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.mvp.moviedbapi.constants.Urls.MOVIEDB_API_KEY_QUERY;
import static com.mvp.moviedbapi.constants.Urls.MOVIEDB_MOVIE_TITLE_QUERY;
import static com.mvp.moviedbapi.constants.Urls.MOVIEDB_PAGE_QUERY;

/**
 * Created by olivier.goutay on 4/18/17.
 * Declaration of the {@link MovieSearchService} allowing to query {@link com.mvp.moviedbapi.constants.Urls#MOVIEDB_BASE_URL}
 */
public interface MovieSearchService {

    @GET("movie?")
    Observable<SearchResults> getMovies(@Query(MOVIEDB_API_KEY_QUERY) String apiKey, @Query(MOVIEDB_MOVIE_TITLE_QUERY) String movieTitle, @Query(MOVIEDB_PAGE_QUERY) int page);

}

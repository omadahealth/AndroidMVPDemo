package com.mvp.moviedbapi.interfaces;

import android.support.annotation.StringRes;

import com.mvp.moviedbapi.models.apis.SearchResults;

/**
 * Created by olivier.goutay on 4/28/17.
 */

public interface MainActivityContract {
    /**
     * Defines the callbacks that {@link com.mvp.moviedbapi.activities.MainActivity} is gonna implement
     */
    interface MainActivityView {
        /**
         * Shows a toast on the {@link com.mvp.moviedbapi.activities.MainActivity}
         *
         * @param idString The id of the string we want to show
         */
        void showToast(@StringRes int idString);

        /**
         * Following the {@link Presenter#searchMovie(String, int)}, calls back the {@link com.mvp.moviedbapi.activities.MainActivity}
         * to update the list adapter
         *
         * @param searchResults The list of {@link SearchResults}
         */
        void updateMovieAdapter(SearchResults searchResults);

        /**
         * Set up the next button {@link android.view.View} visibility, and the click listener
         */
        void setUpOnNextPageButton(String text, int visibility, int page);
    }

    /**
     * Defines the methods that the {@link com.mvp.moviedbapi.activities.MainActivity} presenter is gonna implement
     */
    interface Presenter extends BasePresenter<MainActivityView> {
        /**
         * Allows to search a movie
         *
         * @param text The text inputed in the edittext
         * @param page The page number we want to show
         */
        void searchMovie(String text, int page);
    }
}

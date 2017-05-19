package com.mvp.moviedbapi.interfaces

import android.support.annotation.StringRes

import com.mvp.moviedbapi.models.apis.SearchResults

/**
 * Created by olivier.goutay on 4/28/17.
 */

interface MainActivityContract {
    /**
     * Defines the callbacks that [com.mvp.moviedbapi.activities.MainActivity] is gonna implement
     */
    interface MainActivityView {
        /**
         * Shows a toast on the [com.mvp.moviedbapi.activities.MainActivity]

         * @param idString The id of the string we want to show
         */
        fun showToast(@StringRes idString: Int)

        /**
         * Following the [Presenter.searchMovie], calls back the [com.mvp.moviedbapi.activities.MainActivity]
         * to update the list adapter

         * @param searchResults The list of [SearchResults]
         */
        fun updateMovieAdapter(searchResults: SearchResults)

        /**
         * Set up the next button [android.view.View] visibility, and the click listener
         */
        fun setUpOnNextPageButton(text: String, visibility: Int, page: Int)
    }

    /**
     * Defines the methods that the [com.mvp.moviedbapi.activities.MainActivity] presenter is gonna implement
     */
    interface Presenter : BasePresenter<MainActivityView> {
        /**
         * Allows to search a movie

         * @param text The text inputed in the edittext
         * *
         * @param page The page number we want to show
         */
        fun searchMovie(text: String, page: Int)
    }
}

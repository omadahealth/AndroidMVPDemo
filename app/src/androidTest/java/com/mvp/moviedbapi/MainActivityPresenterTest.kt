package com.mvp.moviedbapi

import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.mvp.moviedbapi.base.AbstractTest
import com.mvp.moviedbapi.constants.Urls
import com.mvp.moviedbapi.interfaces.MainActivityContract
import com.mvp.moviedbapi.models.apis.MovieResult
import com.mvp.moviedbapi.models.apis.SearchResults
import com.mvp.moviedbapi.models.managers.HttpManager
import com.mvp.moviedbapi.network.MovieSearchService
import com.mvp.moviedbapi.presenters.MainActivityPresenter
import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import rx.Observable
import java.io.IOException
import java.util.*

/**
 * Created by olivier.goutay on 4/28/17.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityPresenterTest : AbstractTest() {

    val SEARCH = "star wars"

    @Test
    fun testAttach() {
        val mainActivityPresenter = MainActivityPresenter()
        assertNull(mainActivityPresenter.mView)

        mainActivityPresenter.attach(mock())
        assertNotNull(mainActivityPresenter.mView)
    }

    @Test
    fun testDetach() {
        val mainActivityPresenter = MainActivityPresenter()
        mainActivityPresenter.attach(mock())
        assertNotNull(mainActivityPresenter.mView)

        mainActivityPresenter.detach()
        assertNull(mainActivityPresenter.mView)
    }

    @Test
    fun testSearchMovie() {
        val mainActivityPresenter = MainActivityPresenter()
        val mainActivityView = mock<MainActivityContract.MainActivityView>()

        //Test null view is not crashing at least
        mainActivityPresenter.searchMovie("", 1)

        //Test null text
        mainActivityPresenter.attach(mainActivityView)
        mainActivityPresenter.searchMovie("", 1)
        verify(mainActivityView).showToast(R.string.search_error_no_text)

        //Test error response
        var movieSearchService = mock<MovieSearchService> {
            on { getMovies(anyString(), anyString(), anyInt()) } doReturn Observable.error<SearchResults>(IOException())
        }
        HttpManager.instance.movieSearchService = movieSearchService
        mainActivityPresenter.searchMovie(SEARCH, 1)
        waitFor(50)
        verify(mainActivityView, atLeastOnce()).showToast(R.string.search_error_text)

        //Test ok response
        val searchResults = fakeSearchResults
        movieSearchService = mock<MovieSearchService> {
            on { getMovies(anyString(), anyString(), anyInt()) } doReturn Observable.just(searchResults)
        }
        HttpManager.instance.movieSearchService = movieSearchService
        mainActivityPresenter.searchMovie(SEARCH, 1)
        waitFor(100)

        //Verify movieSearchService called with right params
        verify(movieSearchService, atLeastOnce()).getMovies(eq(Urls.MOVIEDB_API_KEY_VALUE), eq(SEARCH), eq(1))

        //Verify updateMovieAdapter with response
        verify(mainActivityView, atLeastOnce()).updateMovieAdapter(eq(searchResults))

        //Verify next button
        verify(mainActivityView, atLeastOnce()).setUpOnNextPageButton(eq(SEARCH), eq(View.VISIBLE), eq(2))
    }

    /**
     * Returns a fake initialiazed [SearchResults]
     */
    private val fakeSearchResults: SearchResults
        get() {
            val searchResults = SearchResults()
            searchResults.page = 1
            searchResults.totalPages = 2
            val movieResults = Arrays.asList(MovieResult(), MovieResult())
            searchResults.results = movieResults
            return searchResults
        }
}

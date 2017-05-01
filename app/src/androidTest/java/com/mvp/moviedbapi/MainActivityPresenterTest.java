package com.mvp.moviedbapi;

import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.mvp.moviedbapi.base.AbstractTest;
import com.mvp.moviedbapi.constants.Urls;
import com.mvp.moviedbapi.interfaces.MainActivityContract;
import com.mvp.moviedbapi.models.apis.MovieResult;
import com.mvp.moviedbapi.models.apis.SearchResults;
import com.mvp.moviedbapi.models.managers.HttpManager;
import com.mvp.moviedbapi.network.MovieSearchService;
import com.mvp.moviedbapi.presenters.MainActivityPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by olivier.goutay on 4/28/17.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityPresenterTest extends AbstractTest {

    private static final String SEARCH = "star wars";

    @Test
    public void testAttach() {
        MainActivityPresenter mainActivityPresenter = new MainActivityPresenter();
        assertNull(mainActivityPresenter.mView);

        mainActivityPresenter.attach(mock(MainActivityContract.MainActivityView.class));
        assertNotNull(mainActivityPresenter.mView);
    }

    @Test
    public void testDetach() {
        MainActivityPresenter mainActivityPresenter = new MainActivityPresenter();
        mainActivityPresenter.attach(mock(MainActivityContract.MainActivityView.class));
        assertNotNull(mainActivityPresenter.mView);

        mainActivityPresenter.detach();
        assertNull(mainActivityPresenter.mView);
    }

    @Test
    public void testSearchMovie() {
        MainActivityPresenter mainActivityPresenter = new MainActivityPresenter();
        MainActivityContract.MainActivityView mainActivityView = mock(MainActivityContract.MainActivityView.class);

        //Test null view is not crashing at least
        mainActivityPresenter.searchMovie("", 1);

        //Test null text
        mainActivityPresenter.attach(mainActivityView);
        mainActivityPresenter.searchMovie(null, 1);
        verify(mainActivityView).showToast(R.string.search_error_no_text);
        mainActivityPresenter.searchMovie("", 1);
        verify(mainActivityView, times(2)).showToast(R.string.search_error_no_text);

        //Test error response
        MovieSearchService movieSearchService = Mockito.mock(MovieSearchService.class);
        when(movieSearchService.getMovies(anyString(), anyString(), anyInt())).thenReturn(Observable.error(new IOException()));
        HttpManager.getInstance().setMovieSearchService(movieSearchService);
        mainActivityPresenter.searchMovie(SEARCH, 1);
        waitFor(50);
        verify(mainActivityView, atLeastOnce()).showToast(R.string.search_error_text);

        //Test ok response
        SearchResults searchResults = getFakeSearchResults();
        when(movieSearchService.getMovies(anyString(), anyString(), anyInt())).thenReturn(Observable.just(searchResults));
        mainActivityPresenter.searchMovie(SEARCH, 1);
        waitFor(50);

        //Verify movieSearchService called with right params
        verify(movieSearchService, atLeastOnce()).getMovies(eq(Urls.MOVIEDB_API_KEY_VALUE), eq(SEARCH), eq(1));

        //Verify updateMovieAdapter with response
        ArgumentCaptor<SearchResults> argumentCaptorResults = ArgumentCaptor.forClass(SearchResults.class);
        verify(mainActivityView, atLeastOnce()).updateMovieAdapter(argumentCaptorResults.capture());
        assertEquals(searchResults, argumentCaptorResults.getValue());

        //Verify next button
        verify(mainActivityView, atLeastOnce()).setUpOnNextPageButton(eq(SEARCH), eq(View.VISIBLE), eq(2));
    }

    /**
     * Returns a fake initialiazed {@link SearchResults}
     */
    private SearchResults getFakeSearchResults() {
        SearchResults searchResults = new SearchResults();
        searchResults.setPage(1);
        searchResults.setTotalPages(2);
        List<MovieResult> movieResults = Arrays.asList(new MovieResult(), new MovieResult());
        searchResults.setResults(movieResults);
        return searchResults;
    }
}

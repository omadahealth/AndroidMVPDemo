package com.mvp.moviedbapi.presenters;

import android.util.Log;
import android.view.View;

import com.mvp.moviedbapi.R;
import com.mvp.moviedbapi.activities.MainActivity;
import com.mvp.moviedbapi.interfaces.MainActivityContract.MainActivityView;
import com.mvp.moviedbapi.interfaces.MainActivityContract.Presenter;
import com.mvp.moviedbapi.models.apis.SearchResults;
import com.mvp.moviedbapi.models.managers.HttpManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mvp.moviedbapi.constants.Urls.MOVIEDB_API_KEY_VALUE;

/**
 * Created by olivier.goutay on 4/28/17.
 */

public class MainActivityPresenter implements Presenter {

    /**
     * {@link Log} tag of this {@link MainActivity}
     */
    private static final String TAG = "MainActivityPresenter";

    /**
     * The reference to {@link MainActivityView}
     * Created in {@link #attach(MainActivityView)} and destroyed in {@link #detach()}
     */
    public MainActivityView mView;

    @Override
    public void attach(MainActivityView view) {
        this.mView = view;
    }

    @Override
    public void detach() {
        this.mView = null;
    }

    @Override
    public void searchMovie(String text, int page) {
        if (text == null || text.length() == 0 && mView != null) {
            mView.showToast(R.string.search_error_no_text);
        }

        Observable<SearchResults> results = HttpManager.getInstance().getMovieSearchService().getMovies(MOVIEDB_API_KEY_VALUE, text, page);
        results.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResults>() {
                    @Override
                    public final void onCompleted() {
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public final void onError(Throwable e) {
                        Log.e(TAG, "OnError" + e.getMessage());
                        if (mView != null) {
                            mView.showToast(R.string.search_error_text);
                        }
                    }

                    @Override
                    public final void onNext(SearchResults searchResults) {
                        Log.e(TAG, "onNext" + searchResults.getResults()
                                .get(0)
                                .getOriginalTitle());

                        if (mView != null) {
                            mView.updateMovieAdapter(searchResults);

                            boolean nextButtonGone = searchResults.getTotalPages() < 2 || searchResults.getPage().equals(searchResults.getTotalPages());
                            mView.setUpOnNextPageButton(text, nextButtonGone ? View.GONE : View.VISIBLE, searchResults.getPage() + 1);
                        }
                    }
                });
    }
}

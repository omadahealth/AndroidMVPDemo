package com.mvp.moviedbapi.presenters

import android.util.Log
import android.view.View
import com.mvp.moviedbapi.R
import com.mvp.moviedbapi.constants.Urls
import com.mvp.moviedbapi.interfaces.MainActivityContract.MainActivityView
import com.mvp.moviedbapi.interfaces.MainActivityContract.Presenter
import com.mvp.moviedbapi.models.apis.SearchResults
import com.mvp.moviedbapi.models.managers.HttpManager
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by olivier.goutay on 4/28/17.
 */

class MainActivityPresenter : Presenter {

    val TAG = "MainActivityPresenter"

    /**
     * The reference to [MainActivityView]
     * Created in [.attach] and destroyed in [.detach]
     */
    var mView: MainActivityView? = null

    override fun attach(view: MainActivityView) {
        this.mView = view
    }

    override fun detach() {
        this.mView = null
    }

    override fun searchMovie(text: String, page: Int) {
        if (text.isEmpty() && mView != null) {
            mView?.showToast(R.string.search_error_no_text)
        }

        val results = HttpManager.instance.movieSearchService.getMovies(Urls.MOVIEDB_API_KEY_VALUE, text, page)
        results.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<SearchResults>() {
                    override fun onCompleted() {
                        Log.e(TAG, "onCompleted")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "OnError" + e.message)
                        mView?.showToast(R.string.search_error_text)
                    }

                    override fun onNext(searchResults: SearchResults) {
                        Log.e(TAG, "onNext" + searchResults.results?.get(0)?.originalTitle)
                        mView?.updateMovieAdapter(searchResults)
                        val nextButtonGone = searchResults.totalPages!! < 2 || searchResults.page == searchResults.totalPages
                        mView?.setUpOnNextPageButton(text, if (nextButtonGone) View.GONE else View.VISIBLE, searchResults.page!! + 1)
                    }
                })
    }
}

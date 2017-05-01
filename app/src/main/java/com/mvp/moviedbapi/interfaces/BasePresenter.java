package com.mvp.moviedbapi.interfaces;

/**
 * Created by olivier.goutay on 4/28/17.
 */
public interface BasePresenter<T> {

    /**
     * Called when the view is created and wants to attach its presenter
     */
    void attach(T view);

    /**
     * Called when the view is destroyed to get rid of its presenter
     */
    void detach();
}

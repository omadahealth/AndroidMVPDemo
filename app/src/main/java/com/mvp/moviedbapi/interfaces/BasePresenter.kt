package com.mvp.moviedbapi.interfaces

/**
 * Created by olivier.goutay on 4/28/17.
 */
interface BasePresenter<in T> {

    /**
     * Called when the view is created and wants to attach its presenter
     */
    fun attach(view: T)

    /**
     * Called when the view is destroyed to get rid of its presenter
     */
    fun detach()
}

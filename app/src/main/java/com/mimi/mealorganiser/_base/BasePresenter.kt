package com.mimi.mealorganiser._base

/**
 * Created by Mimi on 02/11/2017.
 *
 */
interface BasePresenter<T>{

    var view: T
    fun start()
}
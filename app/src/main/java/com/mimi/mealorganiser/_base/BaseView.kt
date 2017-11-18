package com.mimi.mealorganiser._base

/**
 * Created by Mimi on 02/11/2017.
 * The base view
 */
interface BaseView<out T : BasePresenter<*>> {
    val presenter: T
    fun showLoadingDialog()
    fun hideLoadingDialog()
    fun init()
}

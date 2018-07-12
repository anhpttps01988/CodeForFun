package com.example.anhptt.codeforfun.activities.mvp


interface BaseView<T> {


    fun setPresenter(presenter: T): T


}
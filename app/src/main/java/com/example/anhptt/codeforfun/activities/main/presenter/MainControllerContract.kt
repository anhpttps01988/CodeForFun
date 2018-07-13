package com.example.anhptt.codeforfun.activities.main.presenter

import com.example.anhptt.codeforfun.activities.data.response.UserDetailResponse
import com.example.anhptt.codeforfun.activities.data.response.UserListResponse
import com.example.anhptt.codeforfun.activities.mvp.BasePresenter
import com.example.anhptt.codeforfun.activities.mvp.BaseView


interface MainControllerContract {

    interface View : BaseView<Presenter> {
        fun showUserListOnSuccess(listItem:  MutableList<UserDetailResponse>, page: Int, finalPage: Boolean)
        fun showUserListOnError(throwable: Throwable)
        fun hideLoading()
        fun showLoading()
        fun emptyList()
    }

    interface Presenter : BasePresenter {
        fun getUserList(page: Int)
    }

}
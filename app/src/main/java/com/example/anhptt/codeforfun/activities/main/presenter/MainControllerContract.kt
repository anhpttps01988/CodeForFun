package com.example.anhptt.codeforfun.activities.main.presenter

import com.example.anhptt.codeforfun.activities.data.response.UserListResponse
import com.example.anhptt.codeforfun.activities.mvp.BasePresenter
import com.example.anhptt.codeforfun.activities.mvp.BaseView


interface MainControllerContract {

    interface View : BaseView<Presenter> {
        fun showUserListOnSuccess(listItem: MutableList<UserListResponse.User>, page: Int, finalPage: Boolean)
        fun showUserListOnError(throwable: Throwable)
        fun hideLoading()
        fun showLoading()
    }

    interface Presenter : BasePresenter {
        fun getUserList(page: Int, finalPage: Boolean)
    }

}
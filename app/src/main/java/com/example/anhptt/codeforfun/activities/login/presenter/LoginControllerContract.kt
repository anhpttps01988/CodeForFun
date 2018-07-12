package com.example.anhptt.codeforfun.activities.login.presenter

import com.example.anhptt.codeforfun.activities.data.request.UserRequest
import com.example.anhptt.codeforfun.activities.data.response.UserResponse
import com.example.anhptt.codeforfun.activities.mvp.BasePresenter
import com.example.anhptt.codeforfun.activities.mvp.BaseView


interface LoginControllerContract {

    interface View : BaseView<Presenter> {
        fun showLoading()
        fun hideLoading()
        fun showLoginResponseSuccess(userResponse: UserResponse)
        fun showLoginResponseError(throwable: Throwable)
    }

    interface Presenter : BasePresenter {
        fun login(userRequest: UserRequest)
    }

}
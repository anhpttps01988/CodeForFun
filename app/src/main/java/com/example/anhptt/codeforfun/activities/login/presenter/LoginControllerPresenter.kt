package com.example.anhptt.codeforfun.activities.login.presenter

import android.content.Context
import com.example.anhptt.codeforfun.activities.data.local.LocalData
import com.example.anhptt.codeforfun.activities.data.request.UserRequest
import com.example.anhptt.codeforfun.activities.data.response.UserResponse
import com.example.anhptt.codeforfun.activities.data.service.APIClient
import com.example.anhptt.codeforfun.activities.data.service.ServiceAPI
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class LoginControllerPresenter constructor(context: Context, mView: LoginControllerContract.View) : LoginControllerContract.Presenter {

    private var context = context
    private var apiService: ServiceAPI? = null
    private var compositeDisposable: CompositeDisposable? = null
    private var view = mView

    init {
        compositeDisposable = CompositeDisposable()
        apiService = APIClient().getClient(context).create(ServiceAPI::class.java)
    }


    override fun start() {
    }

    override fun stop() {
        compositeDisposable?.clear()
    }

    override fun login(userRequest: UserRequest) {
        view.showLoading()
        compositeDisposable?.add(apiService?.login(userRequest)!!
                .flatMap { it: Response<UserResponse> ->
                    (
                            if (it.isSuccessful) {
                                Observable.just(it.body())
                            } else {
                                Observable.error(Exception(it.errorBody()!!.string()))
                            }
                            )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onLoginResponseSuccess, this::onLoginResponseError))
    }

    private fun onLoginResponseSuccess(response: UserResponse?) {
        view.hideLoading()
        if (response != null) {
            view.showLoginResponseSuccess(response)
            val json = Gson().toJson(response)
            LocalData.saveLoginResponse(context, json)
            LocalData.saveRefreshTokenResponse(context, response.refreshToken!!)
        }
    }

    private fun onLoginResponseError(throwable: Throwable) {
        view.hideLoading()
        view.showLoginResponseError(throwable)
    }
}
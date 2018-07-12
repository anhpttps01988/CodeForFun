package com.example.anhptt.codeforfun.activities.main.presenter

import android.content.Context
import com.example.anhptt.codeforfun.activities.data.response.UserListResponse
import com.example.anhptt.codeforfun.activities.data.service.APIClient
import com.example.anhptt.codeforfun.activities.data.service.ServiceAPI
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class MainControllerPresenter constructor(context: Context, view: MainControllerContract.View) : MainControllerContract.Presenter {

    private var context = context
    private var view = view
    private var apiService: ServiceAPI? = null
    private var compositeDisposable: CompositeDisposable
    private var isFinalPage = false
    private var page: Int = 0

    init {
        apiService = APIClient().getClient(context).create(ServiceAPI::class.java)
        compositeDisposable = CompositeDisposable()
    }

    override fun start() {
    }

    override fun stop() {
        compositeDisposable.clear()
    }

    override fun getUserList(page: Int, finalPage: Boolean) {
        this.page = page
        this.isFinalPage = finalPage
        compositeDisposable.add(apiService?.getUserList(this.page)!!.flatMap { it: Response<UserListResponse> ->
            (
                    if (it.isSuccessful) {
                        Observable.just(it.body())
                    } else {
                        Observable.error(Exception(it.errorBody()!!.string()))
                    })
        }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onUserListResponseSuccess, this::onUserListResponseError))
    }

    private fun onUserListResponseSuccess(userListResponse: UserListResponse?) {
        var list: MutableList<UserListResponse.User> = mutableListOf()
        view.hideLoading()
        if (userListResponse?.users != null) {
            this.page++
            list = userListResponse.users!!
            isFinalPage = false
        } else {
            isFinalPage = true
        }
        view.showUserListOnSuccess(list, this.page, this.isFinalPage)
    }

    private fun onUserListResponseError(throwable: Throwable) {
        view.hideLoading()
        view.showUserListOnError(throwable)
    }
}
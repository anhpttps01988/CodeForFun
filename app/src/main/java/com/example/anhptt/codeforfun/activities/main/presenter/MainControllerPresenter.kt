package com.example.anhptt.codeforfun.activities.main.presenter

import android.content.Context
import android.util.Log
import com.example.anhptt.codeforfun.activities.data.response.UserDetailResponse
import com.example.anhptt.codeforfun.activities.data.response.UserListResponse
import com.example.anhptt.codeforfun.activities.data.service.APIClient
import com.example.anhptt.codeforfun.activities.data.service.CustomFlapMap
import com.example.anhptt.codeforfun.activities.data.service.ServiceAPI
import com.google.gson.JsonArray
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import retrofit2.Response
import rx.internal.operators.OperatorToObservableList
import java.util.*


class MainControllerPresenter constructor(context: Context, view: MainControllerContract.View) : MainControllerContract.Presenter {

    private var context = context
    private var view = view
    private var apiService: ServiceAPI? = null
    private var compositeDisposable: CompositeDisposable
    private var isFinalPage = false
    private var page: Int = 1
    private var count = 0
    private var listObservable: MutableList<Observable<UserDetailResponse>> = mutableListOf()

    init {
        apiService = APIClient().getClient(context).create(ServiceAPI::class.java)
        compositeDisposable = CompositeDisposable()
    }

    override fun start() {
    }

    override fun stop() {
        compositeDisposable.clear()
    }

    override fun getUserList(page: Int) {
        this.page = page
        compositeDisposable.add(apiService?.getUserList(this.page)!!
                .flatMap { it -> (CustomFlapMap<UserListResponse>().customFlatmapHandler(it)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onUserListResponseSuccess, this::onUserListResponseError))
    }

    private fun onUserListResponseSuccess(userListResponse: UserListResponse?) {
        if (userListResponse?.users != null && userListResponse.users!!.size > 0) {
            listObservable.clear()
            count = userListResponse.users!!.size
            isFinalPage = false
            val idList = userListResponse.users!!.map { it.userId }
            Observable.fromArray(*idList.toTypedArray())
                    .subscribe { getUserId(it!!) }
        }else{
            view.emptyList()
        }
    }

    private fun getUserId(id: String) {
        val observable: Observable<UserDetailResponse> = apiService?.getUserDetail(id)!!
                .flatMap { it -> (CustomFlapMap<UserDetailResponse>().customFlatmapHandler(it)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        listObservable.add(observable)
        if (count == listObservable.size) {
            Observable.combineLatest(listObservable) { t: Array<out Any> ->
                val listUserDetail = t.toMutableList() as MutableList<UserDetailResponse>
                listUserDetail
            }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe { t: MutableList<UserDetailResponse>? -> onUserDetailListSuccess(t) }
        }
    }

    private fun onUserDetailListSuccess(response: List<UserDetailResponse>?) {
        view.hideLoading()
        if (response != null) {
            view.showUserListOnSuccess(response.toMutableList(), this.page, this.isFinalPage)
        }
    }

    private fun onUserDetailListError(throwable: Throwable) {

    }

    private fun onUserListResponseError(throwable: Throwable) {
        view.hideLoading()
        view.showUserListOnError(throwable)
    }
}
package com.example.anhptt.codeforfun.activities.data.service

import com.example.anhptt.codeforfun.activities.data.response.BaseResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

class CustomFlapMap<T : BaseResponse> {

    fun customFlatmapHandler(response: Response<T>): Observable<T> {
        return if (response.isSuccessful) {
            Observable.just(response.body())
        } else {
            try {
                Observable.error<T>(Exception(response.errorBody()!!.string()))
            } catch (e: Exception) {
                Observable.error<T>(e)
            }
        }
    }

    fun customSynchronousFlatmapHandler(response: Response<T>): Single<T> {
        return if (response.isSuccessful) {
            Single.just(response.body())
        } else {
            try {
                Single.error<T>(Exception(response.errorBody()!!.string()))
            } catch (e: Exception) {
                Single.error<T>(e)
            }
        }
    }

}
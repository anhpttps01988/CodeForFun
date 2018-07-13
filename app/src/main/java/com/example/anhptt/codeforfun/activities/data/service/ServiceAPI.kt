package com.example.anhptt.codeforfun.activities.data.service

import com.example.anhptt.codeforfun.activities.data.request.UserRequest
import com.example.anhptt.codeforfun.activities.data.response.UserDetailResponse
import com.example.anhptt.codeforfun.activities.data.response.UserListResponse
import com.example.anhptt.codeforfun.activities.data.response.UserResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface ServiceAPI {

    @POST("/user/token")
    fun login(@Body userRequest: UserRequest): Observable<Response<UserResponse>>

    @POST("/user/token")
    fun refreshToken(@Body userRequest: UserRequest): Single<Response<UserResponse>>

    @GET("/user")
    fun getUserList(@Query("page") page: Int): Observable<Response<UserListResponse>>

    @GET("/user/{user_id}")
    fun getUserDetail(@Path("user_id") userId: String): Observable<Response<UserDetailResponse>>
}
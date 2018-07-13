package com.example.anhptt.codeforfun.activities.data.service

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.example.anhptt.codeforfun.activities.data.local.LocalData
import com.example.anhptt.codeforfun.activities.data.request.UserRequest
import com.example.anhptt.codeforfun.activities.data.response.UserResponse
import com.example.anhptt.codeforfun.activities.utils.Constants
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class APIClient {

    private var retrofit: Retrofit? = null
    private val REQUEST_TIMEOUT = 60
    private var okHttpClient: OkHttpClient? = null

    fun getClient(context: Context): Retrofit {

        if (okHttpClient == null)
            initOkHttp(context)

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(Constants.URL)
                    .client(okHttpClient!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!
    }

    private fun initOkHttp(context: Context) {
        val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("user-Agent", "android")
                    .addHeader("Content-Type", "application/json")

            if (!TextUtils.isEmpty(LocalData.getApiKey(context))) {
                val loginResponse = Gson().fromJson<UserResponse>(LocalData.getApiKey(context), UserResponse::class.java)
                requestBuilder.addHeader("Authorization", loginResponse?.tokenType!!.trim() + " " + loginResponse.accessToken!!.trim())
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        httpClient.addInterceptor(RefreshingOAuthToken(context))
        okHttpClient = httpClient.build()
    }

    inner class RefreshingOAuthToken constructor(private var context: Context) : Interceptor {

        private var loginResponse: UserResponse? = null
        private var isRefresh = false
        private var refreshToken: String? = null


        override fun intercept(chain: Interceptor.Chain?): Response {

            var request = chain?.request()
            refreshToken = LocalData.getRefreshKey(context)
            loginResponse = Gson().fromJson<UserResponse>(LocalData.getApiKey(context), UserResponse::class.java)

            val builder = request?.newBuilder()
            builder?.header("Authorization", "Bearer ")
            val token = loginResponse?.accessToken
            setAuthHeader(builder!!, token)

            request = builder.build()
            val response = chain?.proceed(request!!)

            if (response?.code() == 401) {
                synchronized(this) {
                    val currentToken = loginResponse?.accessToken
                    if (currentToken != null) {
                        fetchToken()
                    }
                    if (loginResponse?.accessToken != null) {
                        setAuthHeader(builder, loginResponse?.accessToken)
                        request = builder.build()
                        return chain.proceed(request!!)
                    }
                }
            }
            return response!!
        }

        private fun setAuthHeader(builder: Request.Builder, token: String?) {
            if (token != null)
                builder.header("Authorization", "${loginResponse?.tokenType} $token")
        }

        private fun fetchToken() {
            if (!isRefresh) {
                isRefresh = true
                if (refreshToken != null) {
                    val request = UserRequest()
                    request.clientId = Constants.CLIENT_ID
                    request.grantType = Constants.REFRESH_TOKEN
                    request.refreshToken = refreshToken
                    getClient(context).create(ServiceAPI::class.java).refreshToken(request)
                            .flatMap { it -> (CustomFlapMap<UserResponse>().customSynchronousFlatmapHandler(it)) }
                            .subscribe(this::onUserResponseSuccess, this::onUserResponseError)
                    isRefresh = false
                }
            }
        }

        private fun onUserResponseSuccess(response: UserResponse) {
            val json = Gson().toJson(response)
            loginResponse = response
            LocalData.saveLoginResponse(context, json)
        }

        private fun onUserResponseError(throwable: Throwable) {
            Log.d("RefreshTokenError", throwable.message)
        }
    }
}
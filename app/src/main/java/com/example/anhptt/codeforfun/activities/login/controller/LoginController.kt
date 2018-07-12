package com.example.anhptt.codeforfun.activities.login.controller

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bluelinelabs.conductor.Controller
import com.example.anhptt.codeforfun.R
import com.example.anhptt.codeforfun.activities.data.request.UserRequest
import com.example.anhptt.codeforfun.activities.data.response.UserResponse
import com.example.anhptt.codeforfun.activities.login.presenter.LoginControllerContract
import com.example.anhptt.codeforfun.activities.login.presenter.LoginControllerPresenter
import com.example.anhptt.codeforfun.activities.main.MainActivity
import com.example.anhptt.codeforfun.activities.utils.Constants

class LoginController : Controller(), LoginControllerContract.View {

    @BindView(R.id.edtUserName)
    lateinit var edtUser: EditText
    @BindView(R.id.edtPassword)
    lateinit var edtPassword: EditText
    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar
    private var presenter: LoginControllerContract.Presenter? = null
    lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_login, container, false)
        ButterKnife.bind(this, view)
        mContext = inflater.context
        presenter = LoginControllerPresenter(mContext, this)
        return view
    }


    @OnClick(R.id.btnLogin)
    fun login() {
        val userRequest = UserRequest()
        userRequest.clientId = Constants.CLIENT_ID
        userRequest.username = edtUser?.text.toString()
        userRequest.password = edtPassword?.text.toString()
        userRequest.grantType = edtPassword?.text.toString()
        presenter?.login(userRequest = userRequest)
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.INVISIBLE
    }

    override fun showLoginResponseSuccess(userResponse: UserResponse) {
        val intent = Intent(mContext, MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(mContext, "Login successfully", Toast.LENGTH_SHORT).show()
    }

    override fun showLoginResponseError(throwable: Throwable) {
        Toast.makeText(mContext, throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun setPresenter(presenter: LoginControllerContract.Presenter): LoginControllerContract.Presenter {
        return this.presenter!!
    }
}
package com.example.anhptt.codeforfun.activities.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.example.anhptt.codeforfun.R
import com.example.anhptt.codeforfun.activities.data.local.LocalData
import com.example.anhptt.codeforfun.activities.login.controller.LoginController
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        router = Conductor.attachRouter(this@LoginActivity, controller_container, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(LoginController()))
        }
    }

    override fun onStart() {
        super.onStart()
        LocalData.clearLoginData(this)
    }
}
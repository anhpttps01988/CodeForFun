package com.example.anhptt.codeforfun.activities.main.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.bluelinelabs.conductor.Controller
import com.example.anhptt.codeforfun.R
import android.os.Bundle



class MainDetailController : Controller() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_main_detail, container, false)
        ButterKnife.bind(this, view)
        return view
    }
}
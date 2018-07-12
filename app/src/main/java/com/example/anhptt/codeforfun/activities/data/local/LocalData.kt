package com.example.anhptt.codeforfun.activities.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


object LocalData {


    @JvmStatic
    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE)
    }

    @JvmStatic
    @SuppressLint("ApplySharedPref")
    fun saveLoginResponse(context: Context, json: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString("LOGIN_PREF", json)
        editor.commit()
    }

    @JvmStatic
    @SuppressLint("ApplySharedPref")
    fun saveRefreshTokenResponse(context: Context, json: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString("REFRESH_PREF", json)
        editor.commit()
    }

    @JvmStatic
    fun getRefreshKey(context: Context): String? {
        return getSharedPreferences(context).getString("REFRESH_PREF", null)
    }

    @JvmStatic
    fun getApiKey(context: Context): String? {
        return getSharedPreferences(context).getString("LOGIN_PREF", null)
    }

    @SuppressLint("ApplySharedPref")
    @JvmStatic
    fun clearLoginData(context: Context) {
        val preferences: SharedPreferences = context.getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE)
        preferences.edit().clear().commit()
    }


}
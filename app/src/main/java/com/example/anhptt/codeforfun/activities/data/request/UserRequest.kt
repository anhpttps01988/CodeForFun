package com.example.anhptt.codeforfun.activities.data.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class UserRequest : Serializable {


    @SerializedName("client_id")
    var clientId: String? = null
    @SerializedName("username")
    var username: String? = null
    @SerializedName("password")
    var password: String? = null
    @SerializedName("grant_type")
    var grantType: String? = null
    @SerializedName("refresh_token")
    var refreshToken: String? = null

}
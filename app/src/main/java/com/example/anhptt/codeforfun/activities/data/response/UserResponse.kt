package com.example.anhptt.codeforfun.activities.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserResponse : Serializable, BaseResponse() {

    @SerializedName("access_token")
    var accessToken: String? = null
    @SerializedName("expires_in")
    var expiresIn: String? = null
    @SerializedName("token_type")
    var tokenType: String? = null
    @SerializedName("scope")
    var scope: String? = null
    @SerializedName("refresh_token")
    var refreshToken: String? = null

}
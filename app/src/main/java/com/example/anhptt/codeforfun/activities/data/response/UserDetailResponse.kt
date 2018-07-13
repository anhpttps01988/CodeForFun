package com.example.anhptt.codeforfun.activities.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserDetailResponse : Serializable, BaseResponse() {
    @SerializedName("user_id")
    var userId: String? = null
    @SerializedName("business_name")
    var businessName: String? = null
    @SerializedName("last_name")
    var lastName: String? = null
    @SerializedName("mobile")
    var mobile: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("description")
    var description: String? = null
}
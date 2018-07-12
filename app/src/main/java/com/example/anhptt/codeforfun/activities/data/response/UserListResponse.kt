package com.example.anhptt.codeforfun.activities.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserListResponse : Serializable {

    @SerializedName("rowCount")
    var rowCount: Int? = -1
    @SerializedName("totalRowCount")
    var totalRowCount: Int? = -1
    @SerializedName("users")
    var users: MutableList<User>? = null

    inner class User constructor(): Serializable {
        @SerializedName("user_id")
        var userId: String? = null
    }

}
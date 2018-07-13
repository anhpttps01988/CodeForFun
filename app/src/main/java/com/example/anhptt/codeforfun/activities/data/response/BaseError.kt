package com.example.anhptt.codeforfun.activities.data.response

import com.google.gson.annotations.SerializedName

open class BaseError {
    @SerializedName("error")
    var error: Error? = null

    inner class Error {
        var code: Int? = 0
        var message: String? = null
    }
}
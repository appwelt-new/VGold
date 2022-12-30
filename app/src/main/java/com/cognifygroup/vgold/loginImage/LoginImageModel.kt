package com.cognifygroup.vgold.loginImage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginImageModel {
    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    @Expose
    @SerializedName("image")
    var image: String? = null
}

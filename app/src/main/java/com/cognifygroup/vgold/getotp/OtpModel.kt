package com.cognifygroup.vgold.getotp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OtpModel {
    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null
}

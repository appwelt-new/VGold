package com.cognifygroup.vgold.payMoneyOtp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PayMoneyOtpModel {
    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    @Expose
    @SerializedName("otp")
    var otp: String? = null
}

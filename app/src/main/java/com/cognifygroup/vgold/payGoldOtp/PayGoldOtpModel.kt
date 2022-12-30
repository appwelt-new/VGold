package com.cognifygroup.vgold.payGoldOtp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PayGoldOtpModel {

    @Expose
    @SerializedName("Message")
    private var Message: String? = null

    @Expose
    @SerializedName("status")
    private var status: String? = null

    @Expose
    @SerializedName("otp")
    private var otp: String? = null

    fun getOtp(): String? {
        return otp
    }

    fun setOtp(otp: String?) {
        this.otp = otp
    }

    fun getMessage(): String? {
        return Message
    }

    fun setMessage(Message: String?) {
        this.Message = Message
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }
}

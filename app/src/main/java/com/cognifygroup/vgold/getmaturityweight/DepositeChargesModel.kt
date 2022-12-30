package com.cognifygroup.vgold.getmaturityweight

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DepositeChargesModel {

    @Expose
    @SerializedName("Message")
    private var Message: String? = null

    @Expose
    @SerializedName("status")
    private var status: String? = null

    @Expose
    @SerializedName("charges")
    private var Data: String? = null

    fun getData(): String? {
        return Data
    }

    fun setData(data: String?) {
        Data = data
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

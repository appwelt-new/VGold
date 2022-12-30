package com.cognifygroup.vgold.getrefercode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReferModel {

    @Expose
    @SerializedName("Message")
    private var Message: String? = null

    @Expose
    @SerializedName("status")
    private var status: String? = null

    @Expose
    @SerializedName("data")
    private var data: String? = null

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

    fun getData(): String? {
        return data
    }

    fun setData(data: String?) {
        this.data = data
    }
}

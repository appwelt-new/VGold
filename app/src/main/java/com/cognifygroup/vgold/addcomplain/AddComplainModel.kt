package com.cognifygroup.vgold.addcomplain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddComplainModel {
    @Expose
    @SerializedName("Message")
    private var Message: String? = null

    @Expose
    @SerializedName("status")
    private var status: String? = null

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

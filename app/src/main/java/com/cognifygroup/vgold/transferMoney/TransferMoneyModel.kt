package com.cognifygroup.vgold.transferMoney

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransferMoneyModel {
    @Expose
    @SerializedName("Message")
    private var Message: String? = null

    @Expose
    @SerializedName("status")
    private var status: String? = null

    @Expose
    @SerializedName("Name")
    private var Name: String? = null

    fun getName(): String? {
        return Name
    }

    fun setName(name: String?) {
        Name = name
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

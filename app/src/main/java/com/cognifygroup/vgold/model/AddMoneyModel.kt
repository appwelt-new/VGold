package com.cognifygroup.vgold.model

class AddMoneyModel {
    private var Message: String? = null

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
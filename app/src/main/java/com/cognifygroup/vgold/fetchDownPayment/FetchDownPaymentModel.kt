package com.cognifygroup.vgold.fetchDownPayment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FetchDownPaymentModel {

    @Expose
    @SerializedName("Message")
    private var Message: String? = null

    @Expose
    @SerializedName("status")
    private var status: String? = null

    @Expose
    @SerializedName("monthly_installment")
    private var monthly_installment: String? = null

    fun getMonthly_installment(): String? {
        return monthly_installment
    }

    fun setMonthly_installment(monthly_installment: String?) {
        this.monthly_installment = monthly_installment
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

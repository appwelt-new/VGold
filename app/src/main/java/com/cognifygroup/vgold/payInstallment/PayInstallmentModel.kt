package com.cognifygroup.vgold.payInstallment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PayInstallmentModel {

    @Expose
    @SerializedName("Message")
    private var Message: String? = null

    @Expose
    @SerializedName("status")
    private var status: String? = null

    @Expose
    @SerializedName("data")
    private var data: Data? = null

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


    fun getData(): Data? {
        return data
    }

    fun setData(data: Data?) {
        this.data = data
    }

    class Data {
        @Expose
        @SerializedName("today_sale_rate")
        var today_sale_rate: String? = null

        @Expose
        @SerializedName("amount_to_pay")
        var amount_to_pay: String? = null

        @Expose
        @SerializedName("amount_to_pay_gst")
        var amount_to_pay_gst: String? = null

        @Expose
        @SerializedName("gst_per")
        var gst_per: String? = null

        @Expose
        @SerializedName("gold_in_wallet")
        var gold_in_wallet: String? = null

        @Expose
        @SerializedName("gold_reduce")
        var gold_reduce: String? = null

        @Expose
        @SerializedName("reduced_gold_in_wallet")
        var reduced_gold_in_wallet: String? = null
    }
}

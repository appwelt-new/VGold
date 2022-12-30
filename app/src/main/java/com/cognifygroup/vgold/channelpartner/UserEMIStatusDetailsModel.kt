package com.cognifygroup.vgold.channelpartner

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserEMIStatusDetailsModel : Serializable {
    @Expose
    @SerializedName("Data")
    var data: ArrayList<Data>? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    class Data : Serializable {
        @Expose
        @SerializedName("id")
        var id: String? = null

        @Expose
        @SerializedName("period")
        var period: String? = null

        @Expose
        @SerializedName("installment")
        var installment: String? = null

        @Expose
        @SerializedName("entry_status")
        var entry_status: String? = null

        @Expose
        @SerializedName("remaining_amount")
        var remaining_amount: String? = null

        @Expose
        @SerializedName("transaction_id")
        var transaction_id: String? = null

        @Expose
        @SerializedName("payment_method")
        var payment_method: String? = null

        @Expose
        @SerializedName("bank_details")
        var bank_details: String? = null

        @Expose
        @SerializedName("cheque_no")
        var cheque_no: String? = null

        @Expose
        @SerializedName("status")
        var status: String? = null

        @Expose
        @SerializedName("admin_status")
        var admin_status: String? = null

        @Expose
        @SerializedName("transaction_date")
        var transaction_date: String? = null

        @Expose
        @SerializedName("is_installment")
        var is_installment: String? = null

        @Expose
        @SerializedName("is_paid")
        var is_paid: String? = null

        @Expose
        @SerializedName("next_due_date")
        var next_due_date: String? = null
    }
}

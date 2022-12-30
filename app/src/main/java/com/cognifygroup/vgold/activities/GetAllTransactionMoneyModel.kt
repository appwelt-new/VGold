package com.cognifygroup.vgold.activities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAllTransactionMoneyModel {
    @Expose
    @SerializedName("Data")
    var data: ArrayList<Data>? = null

    @Expose
    @SerializedName("Wallet_Balance")
    var wallet_Balance: String? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    class Data {
        @Expose
        @SerializedName("status")
        var status: String? = null

        @Expose
        @SerializedName("transaction_date")
        var transaction_date: String? = null

        @Expose
        @SerializedName("admin_status")
        var admin_status: String? = null

        @Expose
        @SerializedName("cheque_no")
        var cheque_no: String? = null

        @Expose
        @SerializedName("bank_details")
        var bank_details: String? = null

        @Expose
        @SerializedName("online_transaction_id")
        var online_transaction_id: String? = null

        @Expose
        @SerializedName("payment_method")
        var payment_method: String? = null

        @Expose
        @SerializedName("transafer_to")
        var transafer_to: String? = null

        @Expose
        @SerializedName("received_from")
        var received_from: String? = null

        @Expose
        @SerializedName("amount")
        var amount: String? = null

        @Expose
        @SerializedName("transaction_id")
        var transaction_id: String? = null
    }
}


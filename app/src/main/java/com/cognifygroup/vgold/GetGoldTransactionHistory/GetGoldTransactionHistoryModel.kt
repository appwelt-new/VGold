package com.cognifygroup.vgold.GetGoldTransactionHistory

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetGoldTransactionHistoryModel {
    @Expose
    @SerializedName("Data")
    var data: ArrayList<Data>? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    class Data(

        val id: String,
        val period: String,
        val installment: String,
        val entry_status: String,
        val remaining_amount: String,
        val transaction_id: String,
        val payment_method: String,
        val bank_details: String,
        val cheque_no: String,
        val status: String,
        val admin_status: String,
        val transaction_date: String,
        val next_due_date: String

    )

}

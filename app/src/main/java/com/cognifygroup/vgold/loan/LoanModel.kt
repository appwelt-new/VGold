package com.cognifygroup.vgold.loan

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoanModel {
    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    @Expose
    @SerializedName("data")
    var data: Data? = null

    inner class Data {
        @Expose
        @SerializedName("amount_in_account")
        var amount_in_account: String? = null

        @Expose
        @SerializedName("loan_amount")
        var loan_amount: String? = null

        @Expose
        @SerializedName("is_eligible")
        var is_eligible: String? = null
    }
}

package com.cognifygroup.vgold.getbankdetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetBankModel {
    @Expose
    @SerializedName("Data")
    var data: ArrayList<Data>? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    class Data {
        @Expose
        @SerializedName("bank_name")
        var bank_name: String? = null

        @Expose
        @SerializedName("bank_id")
        var bank_id: String? = null

        @Expose
        @SerializedName("acc_no")
        var acc_no: String? = null

        override fun toString(): String {
            return "$bank_name( $acc_no )"
        }
    }
}

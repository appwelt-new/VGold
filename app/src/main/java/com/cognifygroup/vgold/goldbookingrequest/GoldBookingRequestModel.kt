package com.cognifygroup.vgold.goldbookingrequest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GoldBookingRequestModel {
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

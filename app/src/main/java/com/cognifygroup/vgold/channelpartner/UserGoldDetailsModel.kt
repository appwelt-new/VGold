package com.cognifygroup.vgold.channelpartner

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserGoldDetailsModel : Serializable {
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
        @SerializedName("gold_booking_id")
        var gold_booking_id: String? = null

        @Expose
        @SerializedName("rate")
        var rate: String? = null

        @Expose
        @SerializedName("gold")
        var gold: String? = null

        @Expose
        @SerializedName("tennure")
        var tennure: String? = null

        @Expose
        @SerializedName("booking_charge")
        var booking_charge: String? = null

        @Expose
        @SerializedName("promo_code")
        var promo_code: String? = null

        @Expose
        @SerializedName("booking_amount")
        var booking_amount: String? = null

        @Expose
        @SerializedName("down_payment")
        var down_payment: String? = null

        @Expose
        @SerializedName("monthly_installment")
        var monthly_installment: String? = null

        @Expose
        @SerializedName("admin_status")
        var admin_status: String? = null

        @Expose
        @SerializedName("added_date")
        var added_date: String? = null

        @Expose
        @SerializedName("account_status")
        var account_status: String? = null

        @Expose
        @SerializedName("commission")
        var commission: String? = null

        @Expose
        @SerializedName("totalPaidInstallment")
        var totalPaidInstallment: String? = null
    }
}

package com.cognifygroup.vgold.channelpartner

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserCommissionDetailsModel : Serializable {
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
        @SerializedName("booking_id")
        var booking_id: String? = null

        @Expose
        @SerializedName("commission_amount")
        var commission_amount: String? = null

        @Expose
        @SerializedName("gold")
        var gold: String? = null

        @Expose
        @SerializedName("created_date")
        var created_date: String? = null
    }
}

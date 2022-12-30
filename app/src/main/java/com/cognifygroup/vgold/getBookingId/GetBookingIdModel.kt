package com.cognifygroup.vgold.getBookingId

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetBookingIdModel {
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
        @SerializedName("id")
        var id: String? = null

        @Expose
        @SerializedName("is_paid")
        var is_paid: Int? = null

        override fun toString(): String {
            return id!!
        }
    }
}

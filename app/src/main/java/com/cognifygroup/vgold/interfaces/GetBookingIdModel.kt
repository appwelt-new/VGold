package com.cognifygroup.vgold.interfaces

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

    class Data(var id:String,var is_paid:Int ) {


    }
}


package com.cognifygroup.vgold.channelpartner

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserDetailsModel_ {
    @Expose
    @SerializedName("data")
    var data: ArrayList<Data>? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    class Data {
        @Expose
        @SerializedName("uid")
        var uid: String? = null

        @Expose
        @SerializedName("uname")
        var uname: String? = null

        @Expose
        @SerializedName("umobile")
        var umobile: String? = null

        @Expose
        @SerializedName("uemail")
        var uemail: String? = null

        @Expose
        @SerializedName("urole")
        var urole: String? = null

        @Expose
        @SerializedName("total_gold_in_account")
        var total_gold_in_account: String? = null

        @Expose
        @SerializedName("upic")
        var upic: String? = null

        @Expose
        @SerializedName("total_commission_created")
        var total_commission_created: String? = null


    }
}
package com.cognifygroup.vgold.channelpartner

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class UserDetailsModel : Serializable {
    @Expose
    @SerializedName("data")
    var data: ArrayList<Data>? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    class Data : Serializable {
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

        override fun toString(): String {
            return "Data{" +
                    "uid='" + uid + '\'' +
                    ", uname='" + uname + '\'' +
                    ", umobile='" + umobile + '\'' +
                    ", uemail='" + uemail + '\'' +
                    ", urole='" + urole + '\'' +
                    ", total_gold_in_account='" + total_gold_in_account + '\'' +
                    ", upic='" + upic + '\'' +
                    ", total_commission_created='" + total_commission_created + '\'' +
                    '}'
        }
    }

    override fun toString(): String {
        return "UserDetailsModel{" +
                "Data=" + data +
                ", Message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}'
    }
}

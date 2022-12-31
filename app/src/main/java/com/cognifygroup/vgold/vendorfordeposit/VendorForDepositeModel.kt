package com.cognifygroup.vgold.vendorfordeposit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VendorForDepositeModel {
    @Expose
    @SerializedName("Data")
    var data: ArrayList<Data>? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    class Data() {
        @Expose
        @SerializedName("firm_name")
        var firm_name: String? = null

        @Expose
        @SerializedName("vendor_id")
        var vendor_id: String? = null

        override fun toString(): String {
            return firm_name!!
        }
    }
}

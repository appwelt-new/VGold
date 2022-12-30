package com.cognifygroup.vgold.getVendorOffer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class VendorOfferModel {
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
        @SerializedName("vendor_id")
        var vendor_id: String? = null

        @Expose
        @SerializedName("logo_path")
        var logo_path: String? = null

        @Expose
        @SerializedName("letter_path")
        var letter_path: String? = null

        @Expose
        @SerializedName("advertisement_path")
        var advertisement_path: String? = null
    }
}

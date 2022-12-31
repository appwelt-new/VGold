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

    class Data(
        val vendor_id: String,
        val logo_path: String,
        val letter_path: String,
        val advertisement_path: String
    )


}

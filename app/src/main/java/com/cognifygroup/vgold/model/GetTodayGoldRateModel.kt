package com.cognifygroup.vgold.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetTodayGoldRateModel {
    @Expose
    @SerializedName("Gold_purchase_rate")
    var gold_purchase_rate: String? = null

    @Expose
    @SerializedName("Gold_purchase_rate_with_gst")
    var gold_purchase_rate_with_gst: String? = null

    @Expose
    @SerializedName("Gold_sale_rate")
    var gold_sale_rate: String? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null
}

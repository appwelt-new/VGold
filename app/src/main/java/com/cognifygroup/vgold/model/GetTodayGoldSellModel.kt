package com.cognifygroup.vgold.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetTodayGoldSellModel {
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

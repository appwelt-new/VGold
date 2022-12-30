package com.cognifygroup.vgold.interfaces

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetTotalGoldGainModel {
    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    @Expose
    @SerializedName("Data")
    var data: Data? = null

    inner class Data {
        @Expose
        @SerializedName("gain")
        var gain: String? = null
    }
}


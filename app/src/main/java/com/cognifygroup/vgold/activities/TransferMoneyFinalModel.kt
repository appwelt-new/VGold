package com.cognifygroup.vgold.activities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TransferMoneyFinalModel {
    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null
}

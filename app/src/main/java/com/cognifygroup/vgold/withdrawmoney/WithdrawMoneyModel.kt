package com.cognifygroup.vgold.withdrawmoney

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class WithdrawMoneyModel {
    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null
}

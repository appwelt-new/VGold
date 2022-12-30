package com.cognifygroup.vgold.model

import com.google.gson.annotations.SerializedName

class BaseServiceResponseModel {
    @SerializedName("status")
    var status = 0

    @SerializedName("Message")
    var message: String? = null
}
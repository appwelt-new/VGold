package com.cognifygroup.vgold.transferMoney

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TransferMoneyService {

    @POST("fetch_name.php?")
    @FormUrlEncoded
    fun addGold(@Field("no") no: String?): Call<TransferMoneyModel?>?
}

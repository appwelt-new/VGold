package com.cognifygroup.vgold.activities

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface TransferMoneyFinalService {
    @POST("money_transfer.php?")
    @FormUrlEncoded
    fun transferMoney(
        @Field("user_id") user_id: String?,
        @Field("no") no: String?,
        @Field("amount") amount: String?
    ): Call<TransferMoneyFinalModel?>?
}

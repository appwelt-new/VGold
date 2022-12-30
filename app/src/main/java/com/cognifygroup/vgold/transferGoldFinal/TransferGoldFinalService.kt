package com.cognifygroup.vgold.transferGoldFinal

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TransferGoldFinalService {

    @POST("gold_transfer.php?")
    @FormUrlEncoded
    fun transferGold(
        @Field("user_id") user_id: String?,
        @Field("no") no: String?,
        @Field("gold") gold: String?
    ): Call<TransferGoldFinalModel?>?
}

package com.cognifygroup.vgold.addgold

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AddGoldService {

    @POST("add_gold.php?")
    @FormUrlEncoded
    fun addGold(
        @Field("user_id") user_id: String?,
        @Field("gold") gold: String?,
        @Field("amount") amount: String?,
        @Field("payment_option") payment_option: String?,
        @Field("bank_details") bank_details: String?,
        @Field("tr_id") tr_id: String?,
        @Field("cheque_no") cheque_no: String?
    ): Call<AddGoldModel?>?
}

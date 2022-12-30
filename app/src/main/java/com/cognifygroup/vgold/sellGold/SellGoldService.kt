package com.cognifygroup.vgold.sellGold

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SellGoldService {

    @POST("sale_gold.php?")
    @FormUrlEncoded
    fun addGold(
        @Field("user_id") user_id: String?,
        @Field("gold") gold: String?,
        @Field("amount") amount: String?
    ): Call<SellGoldModel?>?

    @POST("sale_gold_verifyotp.php?")
    @FormUrlEncoded
    fun getOTP(
        @Field("user_id") user_id: String?,
        @Field("action") gold: String?
    ): Call<SellGoldModel?>?

    @POST("sale_gold_verifyotp.php?")
    @FormUrlEncoded
    fun verifyOTP(
        @Field("user_id") user_id: String?,
        @Field("action") action: String?,
        @Field("otp") otp: String?
    ): Call<SellGoldModel?>?
}

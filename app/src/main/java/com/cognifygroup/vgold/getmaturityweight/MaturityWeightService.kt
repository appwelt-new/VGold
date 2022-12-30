package com.cognifygroup.vgold.getmaturityweight

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MaturityWeightService {
    @POST("calculate_gold_mature_weight.php?")
    @FormUrlEncoded
    fun getMaturityWeight(
        @Field("gold_weight") gold_weight: String?,
        @Field("tennure") tennure: String?,
        @Field("guarantee") guarantee: String?
    ): Call<MaturityWeightModel?>?

    @POST("gold_deposite_charges.php?")
    @FormUrlEncoded
    fun getDepositeCHarges(@Field("gw") gold_weight: String?): Call<DepositeChargesModel?>?
}
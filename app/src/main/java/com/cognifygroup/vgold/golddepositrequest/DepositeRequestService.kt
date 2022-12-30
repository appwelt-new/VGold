package com.cognifygroup.vgold.golddepositrequest

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DepositeRequestService {
    @POST("gold_deposite.php?")
    @FormUrlEncoded
    fun addGold(
        @Field("user_id") user_id: String?,
        @Field("gw") gw: String?,
        @Field("tennure") tennure: String?,
        @Field("cmw") cmw: String?,
        @Field("deposite_charges") dCharges: String?,
        @Field("vendor_id") vendor_id: String?,
        @Field("addpurity") add_purity: String?,
        @Field("remark") remark: String?,
        @Field("guarantee") guarantee: String?
    ): Call<DepositeRequestModel?>?
}
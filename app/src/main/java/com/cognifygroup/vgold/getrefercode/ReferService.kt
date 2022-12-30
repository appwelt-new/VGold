package com.cognifygroup.vgold.getrefercode

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ReferService {
    @POST("send_refer_link.php?")
    @FormUrlEncoded
    fun getBankAddDetails(
        @Field("user_id") user_id: String?,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("mobile_no") mobile_no: String?,
        @Field("refer_link") referLink: String?
    ): Call<ReferModel?>?

    @POST("get_referal_code.php?")
    @FormUrlEncoded
    fun getReferenceCode(@Field("user_id") user_id: String?): Call<ReferModel?>?

    @POST("membership_upgrade.php?")
    @FormUrlEncoded
    fun getWalletPoint(
        @Field("user_id") user_id: String?,
        @Field("data") data: String?
    ): Call<ReferModel?>?
}

package com.cognifygroup.vgold.payInstallment

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PayInstallmentService {

    @POST("installment.php?")
    @FormUrlEncoded
    fun addGold(
        @Field("user_id") user_id: String?,
        @Field("gbid") gbid: String?,
        @Field("amountr") amountr: String?,
        @Field("payment_option") payment_option: String?,
        @Field("bank_details") bank_details: String?,
        @Field("tr_id") tr_id: String?,
        @Field("amount_other") amount_other: String?,
        @Field("cheque_no") cheque_no: String?,
        @Field("confirmed") status: String?
    ): Call<PayInstallmentModel?>?
}

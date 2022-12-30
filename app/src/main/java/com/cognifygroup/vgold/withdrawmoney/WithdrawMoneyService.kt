package com.cognifygroup.vgold.withdrawmoney

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface WithdrawMoneyService {
    @POST("add_withdraw_request.php?")
    @FormUrlEncoded
    fun withdrawMoney(
        @Field("user_id") user_id: String?,
        @Field("bank_id") bank_id: String?,
        @Field("amount") amount: String?,
        @Field("comment") comment: String?
    ): Call<WithdrawMoneyModel?>?
}
package com.cognifygroup.vgold.activities

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface GetAllTransactionMoneyService {
    @POST("money_wallet_transactions.php?")
    @FormUrlEncoded
    fun getAllTransactionForMoney(@Field("user_id") user_id: String?): Call<GetAllTransactionMoneyModel?>?
}

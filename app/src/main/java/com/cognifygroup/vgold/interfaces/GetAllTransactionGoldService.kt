package com.cognifygroup.vgold.interfaces

import com.cognifygroup.vgold.model.GetAllTransactionGoldModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GetAllTransactionGoldService {

    @POST("gold_wallet_transactions.php?")
    @FormUrlEncoded
    fun getAllTransactionForGold(@Field("user_id") user_id: String?): Call<GetAllTransactionGoldModel?>?

}
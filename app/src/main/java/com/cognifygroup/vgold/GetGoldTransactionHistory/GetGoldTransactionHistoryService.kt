package com.cognifygroup.vgold.GetGoldTransactionHistory

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GetGoldTransactionHistoryService {
    @POST("gold_booking_transactions.php?")
    @FormUrlEncoded
    fun getGoldTransactionHistory(@Field("gold_booking_id") gold_booking_id: String?): Call<GetGoldTransactionHistoryModel?>?
}

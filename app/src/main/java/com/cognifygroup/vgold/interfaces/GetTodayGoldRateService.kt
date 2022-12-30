package com.cognifygroup.vgold.interfaces

import com.cognifygroup.vgold.model.GetTodayGoldRateModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface GetTodayGoldRateService {
   // @get:GET("get_purchase_rate.php?")
    val todayGoldRate: Call<Any?>?

    @POST("total_gold_booking_gain.php?")
    @FormUrlEncoded
    fun getTotalGoldGain(@Field("user_id") userId: String?): Call<GetTotalGoldGainModel?>?

    @GET("get_purchase_rate.php?")
    fun getTodayGoldRateForCalculation(): Call<GetTodayGoldRateModel?>?

}
package com.cognifygroup.vgold.interfaces

import com.cognifygroup.vgold.model.GetTodayGoldSellModel
import retrofit2.Call
import retrofit2.http.GET

interface GetTodayGoldSellService {
    @GET("get_sale_rate.php?")
    fun getTodayGoldRate(): Call<GetTodayGoldSellModel?>?
}
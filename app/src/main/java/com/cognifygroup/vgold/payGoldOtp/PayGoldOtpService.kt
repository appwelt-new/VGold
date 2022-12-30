package com.cognifygroup.vgold.payGoldOtp

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PayGoldOtpService {
    @POST("gold_send_otp.php?")
    @FormUrlEncoded
    fun addGold(@Field("user_id") user_id: String?): Call<PayGoldOtpModel?>?
}

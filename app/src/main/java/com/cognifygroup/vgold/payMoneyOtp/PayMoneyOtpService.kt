package com.cognifygroup.vgold.payMoneyOtp

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PayMoneyOtpService {
    @POST("money_send_otp.php?")
    @FormUrlEncoded
    fun addGold(@Field("user_id") user_id: String?): Call<PayMoneyOtpModel?>?
}
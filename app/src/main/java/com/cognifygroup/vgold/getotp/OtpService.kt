package com.cognifygroup.vgold.getotp

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OtpService {

    @POST("forgot_password_otp.php?")
    @FormUrlEncoded
    fun getOtp(@Field("id") id: String?): Call<OtpModel?>?
}
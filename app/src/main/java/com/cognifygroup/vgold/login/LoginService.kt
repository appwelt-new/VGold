package com.cognifygroup.vgold.login

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginService {
    @POST("login.php?")
    @FormUrlEncoded
    fun getLogin(
        @Field("email") contact_no: String?,
        @Field("otp_app_token") otp_app_token: String?
    ): Call<LoginModel?>?

    @POST("login.php?")
    @FormUrlEncoded
    fun verifyLogin(
        @Field("email") contact_no: String?,
        @Field("otp") otp: String?,
        @Field("token") token: String?
    ): Call<LoginModel?>?
}

package com.cognifygroup.vgold.model

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CheckUserSession {
    @POST("login_check.php?")
    @FormUrlEncoded
    fun checkSession(@Field("user_id") user_id: String?): Call<LoginSessionModel?>?
}

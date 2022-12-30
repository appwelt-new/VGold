package com.cognifygroup.vgold.fetchDownPayment

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FetchDownPaymentService {
    @POST("fetch_down_payment.php?")
    @FormUrlEncoded
    fun fetchDownPayment(@Field("gbid") gbid: String?): Call<FetchDownPaymentModel?>?
}

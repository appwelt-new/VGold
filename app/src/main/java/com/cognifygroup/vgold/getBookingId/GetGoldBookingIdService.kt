package com.cognifygroup.vgold.getBookingId

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GetGoldBookingIdService {

    @POST("installment_booking_id.php?")
    @FormUrlEncoded
    fun getGoldBookingId(@Field("user_id") user_id: String?): Call<GetBookingIdModel?>?
}

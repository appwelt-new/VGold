package com.cognifygroup.vgold.goldbooking

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface GoldBookingService {
    @POST("booking_details.php?")
    @FormUrlEncoded
    fun getGoldBooking(
        @Field("quantity") quantity: String?,
        @Field("tennure") tennure: String?,
        @Field("pc") pc: String?,
        @Field("user_id") userId: String?
    ): Call<GoldBookingModel?>?
}

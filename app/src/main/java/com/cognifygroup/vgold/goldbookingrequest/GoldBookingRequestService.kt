package com.cognifygroup.vgold.goldbookingrequest

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface GoldBookingRequestService {
    @POST("gold_booking.php?")
    @FormUrlEncoded
    fun addGold(
        @Field("user_id") user_id: String?,
        @Field("booking_value") booking_value: String?,
        @Field("down_payment") down_payment: String?,
        @Field("monthly") monthly: String?,
        @Field("rate") rate: String?,
        @Field("gold_weight") gold_weight: String?,
        @Field("tennure") tennure: String?,
        @Field("pc") pc: String?,
        @Field("payment_option") payment_option: String?,
        @Field("bank_details") bank_details: String?,
        @Field("tr_id") tr_id: String?,
        @Field("cheque_no") cheque_no: String?,
        @Field("initial_booking_charges") initBookCharges: String?,
        @Field("booking_charges_discount") bookChargeDisc: String?,
        @Field("booking_charges") bookingCharge: String?,
        @Field("confirmed") confirmedVal: String?
    ): Call<GoldBookingRequestModel?>?
}

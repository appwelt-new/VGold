package com.cognifygroup.vgold.goldbooking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GoldBookingModel {
    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    @Expose
    @SerializedName("Booking_value")
    var booking_value: String? = null

    @Expose
    @SerializedName("Gold_rate")
    var gold_rate: String? = null

    @Expose
    @SerializedName("Down_payment")
    var down_payment: String? = null

    @Expose
    @SerializedName("Monthly")
    var monthly: String? = null

    @Expose
    @SerializedName("Booking_charges")
    var bookingCharge: String? = null

    @Expose
    @SerializedName("Initial_Booking_charges")
    var initial_Booking_charges: String? = null

    @Expose
    @SerializedName("Booking_charges_discount")
    var booking_charges_discount: String? = null
}

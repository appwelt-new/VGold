package com.cognifygroup.vgold.CPModule

import com.cognifygroup.vgold.channelpartner.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CPService {
    @POST("cp_user_list.php?")
    @FormUrlEncoded
    fun getCPUserDetails(@Field("user_id") id: String?): Call<UserDetailsModel?>?

    @POST("cp_gold_booking_history.php?")
    @FormUrlEncoded
    fun getCPUserGoldDetails(@Field("user_id") id: String?): Call<UserGoldDetailsModel?>?

    @POST("cp_commission_list.php?")
    @FormUrlEncoded
    fun getCPUserCommissionDetails(@Field("user_id") id: String?): Call<UserCommissionDetailsModel?>?

    @POST("cp_gold_booking_emi.php?")
    @FormUrlEncoded
    fun getCPUserEMIDetails(@Field("user_id") id: String?): Call<UserEMIDetailsModel?>?

    @POST("cp_gold_booking_transactions.php?")
    @FormUrlEncoded
    fun getCPUserEMIStatusDetails(@Field("gold_booking_id") id: String?): Call<UserEMIStatusDetailsModel?>?

    @POST("save_become_cp_request.php?")
    @FormUrlEncoded
    fun beChannelPartner(@Field("user_id") id: String?): Call<UserEMIStatusDetailsModel?>?
}

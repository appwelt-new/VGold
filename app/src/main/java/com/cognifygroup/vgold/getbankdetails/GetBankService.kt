package com.cognifygroup.vgold.getbankdetails

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GetBankService {
    @POST("get_bank_details.php?")
    @FormUrlEncoded
    fun addGold(@Field("user_id") user_id: String?): Call<GetBankModel?>?
}

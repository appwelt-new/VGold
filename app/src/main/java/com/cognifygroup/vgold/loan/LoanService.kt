package com.cognifygroup.vgold.loan

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoanService {


    @POST("get_user_loan_eligiblity.php?")
    @FormUrlEncoded
    fun getEligibility(@Field("user_id") user_id: String?): Call<LoanModel?>?


    @POST("save_loan_request.php?")
    @FormUrlEncoded
    fun applyLoan(
        @Field("user_id") user_id: String?,
        @Field("amount") amt: String?,
        @Field("comment") comment: String?
    ): Call<LoanModel?>?
}
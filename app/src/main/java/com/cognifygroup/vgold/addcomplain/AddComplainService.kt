package com.cognifygroup.vgold.addcomplain

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AddComplainService {
    @POST("process_complaints.php?")
    @FormUrlEncoded
    fun AddComplain(
        @Field("user_id") user_id: String?,
        @Field("complaint") complaint: String?
    ): Call<AddComplainModel?>?

    @POST("user_notes.php?")
    @FormUrlEncoded
    fun AddReview(
        @Field("user_id") user_id: String?,
        @Field("notes") complaint: String?
    ): Call<AddComplainModel?>?

    @POST("user_feedback.php?")
    @FormUrlEncoded
    fun AddFeedback(
        @Field("user_id") user_id: String?,
        @Field("description") desc: String?,
        @Field("points") rating: String?
    ): Call<AddComplainModel?>?
}

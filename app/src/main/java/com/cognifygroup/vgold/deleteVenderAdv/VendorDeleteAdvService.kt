package com.cognifygroup.vgold.deleteVenderAdv

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface VendorDeleteAdvService {
    @POST("vendor_list.php?")
    @FormUrlEncoded
    fun vendorDelete(
        @Field("operation") operation: String?,
        @Field("vendor_id") venderId: String?
    ): Call<VendorAdvModel?>?

    @POST("vendor_list.php?")
    @FormUrlEncoded
    fun vendorAdd(
        @Field("operation") operation: String?,
        @Field("vendor_id") venderId: String?,
        @Field("advertisement_path") imagePath: String?
    ): Call<VendorAdvModel?>? /* @POST("vendor_list.php?")
    @FormUrlEncoded
    Call<VendorAdvModel> vendorUpdate(@Field("operation") String operation, @Field("vendor_id") String venderId, @Field("advertisement_path") String imagePath);*/
}

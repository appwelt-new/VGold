package com.cognifygroup.vgold.getVendorOffer

import retrofit2.Call
import retrofit2.http.POST

interface VendorOfferService {
    @POST("vendor_upload.php?")
    fun vendorOffer(): Call<VendorOfferModel?>?
}

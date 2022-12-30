package com.cognifygroup.vgold.vendorfordeposit

import retrofit2.Call
import retrofit2.http.POST

interface VendorForDepositeService {
    @POST("vendor_for_deposite.php?")
    fun addGold(): Call<VendorForDepositeModel?>?
}

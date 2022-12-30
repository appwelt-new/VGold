package com.cognifygroup.vgold.getVendorOffer

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VendorOfferServiceProvider(context: Context?) {
    private val vendorOfferService: VendorOfferService

    init {
        vendorOfferService =
            APIServiceFactory.createService(VendorOfferService::class.java, context)
    }

    fun getGoldBookingHistory(apiCallback: APICallback) {
        var call: Call<VendorOfferModel?>? = null
        call = vendorOfferService.vendorOffer()
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<VendorOfferModel?> {
            override fun onResponse(
                call: Call<VendorOfferModel?>,
                response: Response<VendorOfferModel?>
            ) {
                if (response.isSuccessful && response.body() != null && response.body()!!.status
                        .equals("200")
                ) {
                    apiCallback.onSuccess(response.body())
                } else if (response.isSuccessful && response.body() != null && response.body()!!
                        .status.equals("400")
                ) {
                    apiCallback.onSuccess(response.body())
                } else {
                    val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                    apiCallback.onFailure(model, response.errorBody())
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            override fun onFailure(call: Call<VendorOfferModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}


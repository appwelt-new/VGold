package com.cognifygroup.vgold.vendorfordeposit

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorForDepositeServiceProvider(context: Context?) {
    private var vendorForDepositeService: VendorForDepositeService? = null

    init {
        vendorForDepositeService =
            APIServiceFactory.createService(VendorForDepositeService::class.java, context)
    }
//    fun VendorForDepositeServiceProvider(context: Context?) {
//        vendorForDepositeService =
//            APIServiceFactory.createService(VendorForDepositeService::class.java, context)
//    }

    fun getVendorForDeposite(apiCallback: APICallback) {
        var call: Call<VendorForDepositeModel?>? = null
        call = vendorForDepositeService?.addGold()
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<VendorForDepositeModel?> {
                override fun onResponse(
                    call: Call<VendorForDepositeModel?>,
                    response: Response<VendorForDepositeModel?>
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("400")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<VendorForDepositeModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}

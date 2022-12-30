package com.cognifygroup.vgold.goldbooking

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GoldBookingServiceProvider(context: Context?) {
    private val goldBookingService: GoldBookingService

    init {
        goldBookingService =
            APIServiceFactory.createService(GoldBookingService::class.java, context)
    }

    fun getAddBankDetails(
        quantity: String?,
        tennure: String?,
        pc: String?,
        userId: String?,
        apiCallback: APICallback
    ) {
        var call: Call<GoldBookingModel?>? = null
        call = goldBookingService.getGoldBooking(quantity, tennure, pc, userId)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<GoldBookingModel?> {
                override fun onResponse(
                    call: Call<GoldBookingModel?>,
                    response: Response<GoldBookingModel?>
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

                override fun onFailure(call: Call<GoldBookingModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}

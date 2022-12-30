package com.cognifygroup.vgold.getBookingId

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetGoldBookingIdServiceProvider {
    private var getGoldBookingIdService: GetGoldBookingIdService? = null

    fun GetGoldBookingIdServiceProvider(context: Context?) {
        getGoldBookingIdService =
            APIServiceFactory.createService(GetGoldBookingIdService::class.java, context)
    }

    fun getGoldBookingId(user_id: String?, apiCallback: APICallback) {
        var call: Call<GetBookingIdModel?>? = null
        call = getGoldBookingIdService!!.getGoldBookingId(user_id)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<GetBookingIdModel?> {
            override fun onResponse(
                call: Call<GetBookingIdModel?>,
                response: Response<GetBookingIdModel?>
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

            override fun onFailure(call: Call<GetBookingIdModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

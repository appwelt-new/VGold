package com.cognifygroup.vgold.getotp

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OtpServiceProvider(context: Context?) {
    private val otpService: OtpService

    init {
        otpService = APIServiceFactory.createService(OtpService::class.java, context)
    }

    fun getReg(id: String?, apiCallback: APICallback) {
        var call: Call<OtpModel?>? = null
        call = otpService.getOtp(id)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<OtpModel?> {
                override fun onResponse(call: Call<OtpModel?>, response: Response<OtpModel?>) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("300")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<OtpModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}

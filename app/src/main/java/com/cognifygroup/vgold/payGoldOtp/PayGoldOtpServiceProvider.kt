package com.cognifygroup.vgold.payGoldOtp

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayGoldOtpServiceProvider(context: Context?) {
    private var payGoldOtpService: PayGoldOtpService? = null

    init {
        payGoldOtpService = APIServiceFactory.createService(PayGoldOtpService::class.java, context)
    }
//    fun PayGoldOtpServiceProvider(context: Context?) {
//        payGoldOtpService = APIServiceFactory.createService(PayGoldOtpService::class.java, context)
//    }

    fun getAddBankDetails(user_id: String?, apiCallback: APICallback) {
        var call: Call<PayGoldOtpModel?>? = null
        call = payGoldOtpService!!.addGold(user_id)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<PayGoldOtpModel?> {
            override fun onResponse(
                call: Call<PayGoldOtpModel?>,
                response: Response<PayGoldOtpModel?>
            ) {
                if (response.isSuccessful && response.body() != null && response.body()!!
                        .getStatus().equals("200")
                ) {
                    apiCallback.onSuccess(response.body())
                } else if (response.isSuccessful && response.body() != null && response.body()!!
                        .getStatus().equals("400")
                ) {
                    apiCallback.onSuccess(response.body())
                } else {
                    val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                    apiCallback.onFailure(model, response.errorBody())
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            override fun onFailure(call: Call<PayGoldOtpModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

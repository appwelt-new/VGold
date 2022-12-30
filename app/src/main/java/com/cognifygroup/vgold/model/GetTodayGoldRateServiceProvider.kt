package com.cognifygroup.vgold.model

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.GetTodayGoldRateService
import com.cognifygroup.vgold.interfaces.GetTotalGoldGainModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetTodayGoldRateServiceProvider(context: Context?) {
     private val getTodayGoldRateService: GetTodayGoldRateService

    init {
        getTodayGoldRateService =
            APIServiceFactory.createService(GetTodayGoldRateService::class.java, context)
    }

    fun getTodayGoldRate(userId: String?, apiCallback: APICallback) {
        var call: Call<GetTodayGoldRateModel?>? = null
        call = getTodayGoldRateService.getTodayGoldRateForCalculation()
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<GetTodayGoldRateModel?> {
            override fun onResponse(
                call: Call<GetTodayGoldRateModel?>,
                response: Response<GetTodayGoldRateModel?>
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
                    val model: BaseServiceResponseModel? = ErrorUtils().parseError(response)
                    apiCallback.onFailure(model, response.errorBody())
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            override fun onFailure(call: Call<GetTodayGoldRateModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }

    fun getTotalGain(userId: String?, apiCallback: APICallback) {
        var call: Call<GetTotalGoldGainModel?>? = null
        call = getTodayGoldRateService.getTotalGoldGain(userId)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<GetTotalGoldGainModel?> {
            override fun onResponse(
                call: Call<GetTotalGoldGainModel?>,
                response: Response<GetTotalGoldGainModel?>
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
                    val model: BaseServiceResponseModel? = ErrorUtils().parseError(response)
                    apiCallback.onFailure(model, response.errorBody())
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            override fun onFailure(call: Call<GetTotalGoldGainModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}



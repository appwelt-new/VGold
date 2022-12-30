package com.cognifygroup.vgold.model

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.GetTodayGoldSellService
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetTodayGoldSellRateServiceProvider(context: Context?) {
    private val getTodayGoldSellService: GetTodayGoldSellService

    init {
        getTodayGoldSellService =
            APIServiceFactory.createService(GetTodayGoldSellService::class.java, context)
    }

    fun getTodayGoldRate(apiCallback: APICallback) {
        var call: Call<GetTodayGoldSellModel?>? = null
        call = getTodayGoldSellService.getTodayGoldRate()
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<GetTodayGoldSellModel?> {
            override fun onResponse(
                call: Call<GetTodayGoldSellModel?>,
                response: Response<GetTodayGoldSellModel?>
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

            override fun onFailure(call: Call<GetTodayGoldSellModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

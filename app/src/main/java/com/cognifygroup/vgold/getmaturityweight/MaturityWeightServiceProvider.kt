package com.cognifygroup.vgold.getmaturityweight

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MaturityWeightServiceProvider {
    private var maturityWeightService: MaturityWeightService? = null

    fun MaturityWeightServiceProvider(context: Context?) {
        maturityWeightService =
            APIServiceFactory.createService(MaturityWeightService::class.java, context)
    }

    fun getMaturityWeight(
        gold_weight: String?,
        tennure: String?,
        guarantee: String?,
        apiCallback: APICallback
    ) {
        var call: Call<MaturityWeightModel?>? = null
        call = maturityWeightService?.getMaturityWeight(gold_weight, tennure, guarantee)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<MaturityWeightModel?> {
                override fun onResponse(
                    call: Call<MaturityWeightModel?>,
                    response: Response<MaturityWeightModel?>
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .getStatus().equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .getStatus().equals("400")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<MaturityWeightModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }

    fun getDepositeCharges(gold_weight: String?, apiCallback: APICallback) {
        var call: Call<DepositeChargesModel?>? = null
        call = maturityWeightService?.getDepositeCHarges(gold_weight)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<DepositeChargesModel?> {
                override fun onResponse(
                    call: Call<DepositeChargesModel?>,
                    response: Response<DepositeChargesModel?>
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .getStatus().equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .getStatus().equals("400")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<DepositeChargesModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}

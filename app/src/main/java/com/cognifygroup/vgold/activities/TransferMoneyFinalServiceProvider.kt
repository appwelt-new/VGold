package com.cognifygroup.vgold.activities

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransferMoneyFinalServiceProvider(context: Context?) {
    private val transferMoneyFinalService: TransferMoneyFinalService

    init {
        transferMoneyFinalService =
            APIServiceFactory.createService(TransferMoneyFinalService::class.java, context)
    }

    fun transferMoney(user_id: String?, no: String?, amount: String?, apiCallback: APICallback) {
        var call: Call<TransferMoneyFinalModel?>? = null
        call = transferMoneyFinalService.transferMoney(user_id, no, amount)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<TransferMoneyFinalModel?> {
            override fun onResponse(
                call: Call<TransferMoneyFinalModel?>,
                response: Response<TransferMoneyFinalModel?>
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

            override fun onFailure(call: Call<TransferMoneyFinalModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

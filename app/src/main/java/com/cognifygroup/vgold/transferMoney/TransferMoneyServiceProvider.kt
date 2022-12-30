package com.cognifygroup.vgold.transferMoney

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransferMoneyServiceProvider(context: Context?) {
    private val transferMoneyService: TransferMoneyService

    init {
        transferMoneyService =
            APIServiceFactory.createService(TransferMoneyService::class.java, context)
    }

    fun getAddBankDetails(no: String?, apiCallback: APICallback) {
        var call: Call<TransferMoneyModel?>? = null
        call = transferMoneyService.addGold(no)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<TransferMoneyModel?> {
                override fun onResponse(
                    call: Call<TransferMoneyModel?>,
                    response: Response<TransferMoneyModel?>
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

                override fun onFailure(call: Call<TransferMoneyModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}

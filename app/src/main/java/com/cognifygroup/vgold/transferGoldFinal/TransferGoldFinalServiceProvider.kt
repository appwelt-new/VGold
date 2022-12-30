package com.cognifygroup.vgold.transferGoldFinal

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransferGoldFinalServiceProvider(context: Context?) {

    private var transferGoldFinalService: TransferGoldFinalService? = null
init {
    transferGoldFinalService =
        APIServiceFactory.createService(TransferGoldFinalService::class.java, context)
}
//    fun TransferGoldFinalServiceProvider(context: Context?) {
//        transferGoldFinalService =
//            APIServiceFactory.createService(TransferGoldFinalService::class.java, context)
//    }

    fun transferMoney(user_id: String?, no: String?, gold: String?, apiCallback: APICallback) {
        var call: Call<TransferGoldFinalModel?>? = null
        call = transferGoldFinalService!!.transferGold(user_id, no, gold)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<TransferGoldFinalModel?> {
            override fun onResponse(
                call: Call<TransferGoldFinalModel?>,
                response: Response<TransferGoldFinalModel?>
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

            override fun onFailure(call: Call<TransferGoldFinalModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

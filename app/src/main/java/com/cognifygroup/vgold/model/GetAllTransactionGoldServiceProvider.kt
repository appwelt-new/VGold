package com.cognifygroup.vgold.model

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.GetAllTransactionGoldService
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetAllTransactionGoldServiceProvider(context: Context?) {
    private val getAllTransactionMoneyService: GetAllTransactionGoldService

    init {
        getAllTransactionMoneyService =
            APIServiceFactory.createService(GetAllTransactionGoldService::class.java, context)
    }

    fun getAllTransactionGoldHistory(user_id: String?, apiCallback: APICallback) {
        var call: Call<GetAllTransactionGoldModel?>? = null
        call = getAllTransactionMoneyService.getAllTransactionForGold(user_id)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<GetAllTransactionGoldModel?> {
            override fun onResponse(
                call: Call<GetAllTransactionGoldModel?>,
                response: Response<GetAllTransactionGoldModel?>
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

            override fun onFailure(call: Call<GetAllTransactionGoldModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}
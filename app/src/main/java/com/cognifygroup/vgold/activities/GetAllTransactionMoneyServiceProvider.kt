package com.cognifygroup.vgold.activities

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetAllTransactionMoneyServiceProvider(context: Context?) {
    private val getAllTransactionMoneyService: GetAllTransactionMoneyService

    init {
        getAllTransactionMoneyService = APIServiceFactory.createService(
            GetAllTransactionMoneyService::class.java, context
        )
    }

    fun getAllTransactionMoneyHistory(user_id: String?, apiCallback: APICallback) {
        var call: Call<GetAllTransactionMoneyModel?>? = null
        call = getAllTransactionMoneyService.getAllTransactionForMoney(user_id)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<GetAllTransactionMoneyModel?> {
            override fun onResponse(
                call: Call<GetAllTransactionMoneyModel?>,
                response: Response<GetAllTransactionMoneyModel?>
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

            override fun onFailure(call: Call<GetAllTransactionMoneyModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}


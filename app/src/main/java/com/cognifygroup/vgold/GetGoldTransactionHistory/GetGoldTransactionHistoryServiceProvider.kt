package com.cognifygroup.vgold.GetGoldTransactionHistory

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetGoldTransactionHistoryServiceProvider {
    private var getGoldTransactionHistoryService: GetGoldTransactionHistoryService? = null

    fun GetGoldTransactionHistoryServiceProvider(context: Context?) {
        getGoldTransactionHistoryService = APIServiceFactory.createService(
            GetGoldTransactionHistoryService::class.java, context
        )
    }

    fun getGoldTransactionHistory(gold_booking_id: String?, apiCallback: APICallback) {
        var call: Call<GetGoldTransactionHistoryModel?>? = null
        call = getGoldTransactionHistoryService?.getGoldTransactionHistory(gold_booking_id)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<GetGoldTransactionHistoryModel?> {
                override fun onResponse(
                    call: Call<GetGoldTransactionHistoryModel?>,
                    response: Response<GetGoldTransactionHistoryModel?>
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

                override fun onFailure(call: Call<GetGoldTransactionHistoryModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}



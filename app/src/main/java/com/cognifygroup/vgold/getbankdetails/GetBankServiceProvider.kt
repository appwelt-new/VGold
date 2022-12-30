package com.cognifygroup.vgold.getbankdetails

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetBankServiceProvider {

    private var getBankService: GetBankService? = null

    fun GetBankServiceProvider(context: Context?) {
        getBankService = APIServiceFactory.createService(GetBankService::class.java, context)
    }

    fun getAddBankDetails(user_id: String?, apiCallback: APICallback) {
        var call: Call<GetBankModel?>? = null
        call = getBankService?.addGold(user_id)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<GetBankModel?> {
                override fun onResponse(call: Call<GetBankModel?>, response: Response<GetBankModel?>) {
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

                override fun onFailure(call: Call<GetBankModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}
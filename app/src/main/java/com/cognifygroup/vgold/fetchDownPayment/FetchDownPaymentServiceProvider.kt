package com.cognifygroup.vgold.fetchDownPayment

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchDownPaymentServiceProvider(context: Context?) {
    private var fetchDownPaymentService: FetchDownPaymentService? = null

    init {
        fetchDownPaymentService =
            APIServiceFactory.createService(FetchDownPaymentService::class.java, context)

    }
//    fun FetchDownPaymentServiceProvider(context: Context?) {
//        fetchDownPaymentService =
//            APIServiceFactory.createService(FetchDownPaymentService::class.java, context)
//    }

    fun getAddBankDetails(gbid: String?, apiCallback: APICallback) {
        var call: Call<FetchDownPaymentModel?>? = null
        call = fetchDownPaymentService?.fetchDownPayment(gbid)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<FetchDownPaymentModel?> {
                override fun onResponse(
                    call: Call<FetchDownPaymentModel?>,
                    response: Response<FetchDownPaymentModel?>
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

                override fun onFailure(call: Call<FetchDownPaymentModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}

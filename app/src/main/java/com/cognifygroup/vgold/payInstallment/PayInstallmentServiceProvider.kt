package com.cognifygroup.vgold.payInstallment

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayInstallmentServiceProvider(context: Context?) {
    private var payInstallmentService: PayInstallmentService? = null

    init {

        payInstallmentService =
            APIServiceFactory.createService(PayInstallmentService::class.java, context)
    }
//    fun PayInstallmentServiceProvider(context: Context?) {
//        payInstallmentService =
//            APIServiceFactory.createService(PayInstallmentService::class.java, context)
//    }

    fun payInstallment(
        user_id: String?, gbid: String?, amountr: String?, payment_option: String?,
        bank_details: String?, tr_id: String?, otherAmount: String?,
        cheque_no: String?, status: String?, apiCallback: APICallback
    ) {
        var call: Call<PayInstallmentModel?>? = null
        call = payInstallmentService!!.addGold(
            user_id, gbid, amountr, payment_option, bank_details,
            tr_id, otherAmount,
            cheque_no, status
        )
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<PayInstallmentModel?> {
            override fun onResponse(
                call: Call<PayInstallmentModel?>,
                response: Response<PayInstallmentModel?>
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

            override fun onFailure(call: Call<PayInstallmentModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

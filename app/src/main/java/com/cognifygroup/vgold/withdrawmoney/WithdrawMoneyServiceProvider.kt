package com.cognifygroup.vgold.withdrawmoney

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithdrawMoneyServiceProvider(context: Context?) {
    private val withdrawMoneyService: WithdrawMoneyService

    init {
        withdrawMoneyService =
            APIServiceFactory.createService(WithdrawMoneyService::class.java, context)
    }

    fun getAddBankDetails(
        user_id: String?,
        bank_id: String?,
        amount: String?,
        comment: String?,
        apiCallback: APICallback
    ) {
        var call: Call<WithdrawMoneyModel?>? = null
        call = withdrawMoneyService.withdrawMoney(user_id, bank_id, amount, comment)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<WithdrawMoneyModel?> {
                override fun onResponse(
                    call: Call<WithdrawMoneyModel?>,
                    response: Response<WithdrawMoneyModel?>
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

                override fun onFailure(call: Call<WithdrawMoneyModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}

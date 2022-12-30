package com.cognifygroup.vgold.activities.addmoney

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMoneyServiceProvider (context: Context?){

    private var addMoneyService: AddMoneyService? = null

    init {

        addMoneyService = APIServiceFactory.createService(AddMoneyService::class.java, context)
    }
//    fun AddMoneyServiceProvider(context: Context?) {
//        addMoneyService = APIServiceFactory.createService(AddMoneyService::class.java, context)
//    }

    fun getAddBankDetails(
        user_id: String?,
        amount: String?,
        payment_option: String?,
        bank_details: String?,
        tr_id: String?,
        cheque_no: String?,
        apiCallback: APICallback
    ) {
        var call: Call<AddMoneyModel?>? = null
        call = addMoneyService?.addMoney(
            user_id,
            amount,
            payment_option,
            bank_details,
            tr_id,
            cheque_no
        )
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<AddMoneyModel?> {
                override fun onResponse(
                    call: Call<AddMoneyModel?>,
                    response: Response<AddMoneyModel?>
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

                override fun onFailure(call: Call<AddMoneyModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}


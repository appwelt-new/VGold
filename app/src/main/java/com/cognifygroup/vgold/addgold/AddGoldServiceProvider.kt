package com.cognifygroup.vgold.addgold

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGoldServiceProvider(context: Context?) {
    private var addGoldService: AddGoldService? = null

    init {
        addGoldService = APIServiceFactory.createService(AddGoldService::class.java, context)
    }
//    fun AddGoldServiceProvider(context: Context?) {
//        addGoldService = APIServiceFactory.createService(AddGoldService::class.java, context)
//    }

    fun getAddBankDetails(
        user_id: String?,
        gold: String?,
        amount: String?,
        payment_option: String?,
        bank_details: String?,
        tr_id: String?,
        cheque_no: String?,
        apiCallback: APICallback
    ) {
        var call: Call<AddGoldModel?>? = null
        call = addGoldService!!.addGold(
            user_id!!,
            gold,
            amount,
            payment_option,
            bank_details,
            tr_id,
            cheque_no
        )
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<AddGoldModel?> {
            override fun onResponse(call: Call<AddGoldModel?>, response: Response<AddGoldModel?>) {
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

            override fun onFailure(call: Call<AddGoldModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

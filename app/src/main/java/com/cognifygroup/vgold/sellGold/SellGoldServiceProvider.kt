package com.cognifygroup.vgold.sellGold

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellGoldServiceProvider (context: Context?){
    private var sellGoldService: SellGoldService? = null
init {
    sellGoldService = APIServiceFactory.createService(SellGoldService::class.java, context)
}
//    fun SellGoldServiceProvider(context: Context?) {
//        sellGoldService = APIServiceFactory.createService(SellGoldService::class.java, context)
//    }

    fun getAddBankDetails(
        user_id: String?,
        gold: String?,
        amount: String?,
        apiCallback: APICallback
    ) {
        var call: Call<SellGoldModel?>? = null
        call = sellGoldService!!.addGold(user_id, gold, amount)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<SellGoldModel?> {
            override fun onResponse(
                call: Call<SellGoldModel?>,
                response: Response<SellGoldModel?>
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

            override fun onFailure(call: Call<SellGoldModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }

    fun getOTP(user_id: String?, action: String?, apiCallback: APICallback) {
        var call: Call<SellGoldModel?>? = null
        call = sellGoldService!!.getOTP(user_id, action)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<SellGoldModel?> {
            override fun onResponse(
                call: Call<SellGoldModel?>,
                response: Response<SellGoldModel?>
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

            override fun onFailure(call: Call<SellGoldModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }

    fun verifyOTP(user_id: String?, action: String?, otp: String?, apiCallback: APICallback) {
        var call: Call<SellGoldModel?>? = null
        call = sellGoldService!!.verifyOTP(user_id, action, otp)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<SellGoldModel?> {
            override fun onResponse(
                call: Call<SellGoldModel?>,
                response: Response<SellGoldModel?>
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

            override fun onFailure(call: Call<SellGoldModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

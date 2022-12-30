package com.cognifygroup.vgold.getrefercode

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferServiceProvider(context: Context?) {


    private var referService: ReferService? = null

    init {

        referService = APIServiceFactory.createService(ReferService::class.java, context)
    }
//    fun ReferServiceProvider(context: Context?) {
//        referService = APIServiceFactory.createService(ReferService::class.java, context)
//    }

    fun getAddBankDetails(
        user_id: String?,
        name: String?,
        email: String?,
        mobile_no: String?,
        refLink: String?,
        apiCallback: APICallback
    ) {
        var call: Call<ReferModel?>? = null
        call = referService!!.getBankAddDetails(user_id, name, email, mobile_no, refLink)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<ReferModel?> {
            override fun onResponse(call: Call<ReferModel?>, response: Response<ReferModel?>) {
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

            override fun onFailure(call: Call<ReferModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }

    fun getReferenceCode(user_id: String?, apiCallback: APICallback) {
        var call: Call<ReferModel?>? = null
        call = referService!!.getReferenceCode(user_id)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<ReferModel?> {
            override fun onResponse(call: Call<ReferModel?>, response: Response<ReferModel?>) {
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

            override fun onFailure(call: Call<ReferModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }

    fun getCheckWallet(user_id: String?, data: String?, apiCallback: APICallback) {
        var call: Call<ReferModel?>? = null
        call = referService!!.getWalletPoint(user_id, data)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<ReferModel?> {
            override fun onResponse(call: Call<ReferModel?>, response: Response<ReferModel?>) {
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

            override fun onFailure(call: Call<ReferModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }


}

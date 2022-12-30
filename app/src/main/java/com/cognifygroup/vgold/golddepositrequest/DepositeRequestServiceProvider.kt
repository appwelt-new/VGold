package com.cognifygroup.vgold.golddepositrequest

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepositeRequestServiceProvider(context: Context?) {
    private var depositeRequestService: DepositeRequestService? = null

    init {
        depositeRequestService =
            APIServiceFactory.createService(DepositeRequestService::class.java, context)
    }
//    fun DepositeRequestServiceProvider(context: Context?) {
//        depositeRequestService =
//            APIServiceFactory.createService(DepositeRequestService::class.java, context)
//    }

    fun getDepositeRequest(
        user_id: String?,
        gw: String?,
        tennure: String?,
        cmw: String?,
        dCharges: String?,
        vendor_id: String?,
        add_purity: String?,
        remark: String?,
        guarantee: String?,
        apiCallback: APICallback
    ) {
        var call: Call<DepositeRequestModel?>? = null
        call = depositeRequestService!!.addGold(
            user_id,
            gw,
            tennure,
            cmw,
            dCharges,
            vendor_id,
            add_purity,
            remark,
            guarantee
        )
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<DepositeRequestModel?> {
            override fun onResponse(
                call: Call<DepositeRequestModel?>,
                response: Response<DepositeRequestModel?>
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

            override fun onFailure(call: Call<DepositeRequestModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

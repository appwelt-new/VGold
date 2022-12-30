package com.cognifygroup.vgold.goldbookingrequest

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GoldBookingRequestServiceProvider(context: Context?) {
    private val goldBookingRequestService: GoldBookingRequestService

    init {
        goldBookingRequestService =
            APIServiceFactory.createService(GoldBookingRequestService::class.java, context)
    }

    fun getGoldBookingRequest(
        user_id: String?, booking_value: String?, down_payment: String?,
        monthly: String?, rate: String?, gold_weight: String?,
        tennure: String?, pc: String?, payment_option: String?,
        bank_details: String?, tr_id: String?, cheque_no: String?,
        initBookingCharge: String?,
        disc: String?, booking_charge: String?, confirmedVal: String?,
        apiCallback: APICallback
    ) {
        var call: Call<GoldBookingRequestModel?>? = null
        call = goldBookingRequestService.addGold(
            user_id,
            booking_value,
            down_payment,
            monthly,
            rate,
            gold_weight,
            tennure,
            pc,
            payment_option,
            bank_details,
            tr_id,
            cheque_no,
            initBookingCharge,
            disc,
            booking_charge,
            confirmedVal
        )
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<GoldBookingRequestModel?> {
                override fun onResponse(
                    call: Call<GoldBookingRequestModel?>,
                    response: Response<GoldBookingRequestModel?>
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

                override fun onFailure(call: Call<GoldBookingRequestModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}

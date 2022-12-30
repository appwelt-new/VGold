package com.cognifygroup.vgold.loan
import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoanServiceProvider(context: Context?) {

    private var loanService: LoanService? = null

    init {

        loanService = APIServiceFactory.createService(LoanService::class.java, context)
    }
//    fun LoanServiceProvider(context: Context?) {
//        loanService = APIServiceFactory.createService(LoanService::class.java, context)
//    }

    fun getLoanEligibility(user_id: String?, apiCallback: APICallback) {
        var call: Call<LoanModel?>? = null
        call = loanService!!.getEligibility(user_id)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<LoanModel?> {
            override fun onResponse(call: Call<LoanModel?>, response: Response<LoanModel?>) {
                if (response.isSuccessful && response.body() != null && response.body()!!.status
                        .equals("200")
                ) {
                    apiCallback.onSuccess(response.body())
                } else if (response.isSuccessful && response.body() != null && response.body()!!
                        .status.equals("400")
                ) {
                    apiCallback.onSuccess(response.body())
                } else {
                    val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                    apiCallback.onFailure(model, response.errorBody())
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            override fun onFailure(call: Call<LoanModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }

    fun applyForLoan(user_id: String?, amt: String?, comment: String?, apiCallback: APICallback) {
        var call: Call<LoanModel?>? = null
        call = loanService!!.applyLoan(user_id, amt, comment)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<LoanModel?> {
            override fun onResponse(call: Call<LoanModel?>, response: Response<LoanModel?>) {
                if (response.isSuccessful && response.body() != null && response.body()!!.status
                        .equals("200")
                ) {
                    apiCallback.onSuccess(response.body())
                } else if (response.isSuccessful && response.body() != null && response.body()!!
                        .status.equals("400")
                ) {
                    apiCallback.onSuccess(response.body())
                } else {
                    val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                    apiCallback.onFailure(model, response.errorBody())
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            override fun onFailure(call: Call<LoanModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}
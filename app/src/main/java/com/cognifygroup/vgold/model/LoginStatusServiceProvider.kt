package com.cognifygroup.vgold.model

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginStatusServiceProvider(context: Context?) {
    private val userSession: CheckUserSession

    init {
        userSession = APIServiceFactory.createService(CheckUserSession::class.java, context)
    }

    fun getLoginStatus(user_id: String?, apiCallback: APICallback) {
        var call: Call<LoginSessionModel?>? = null
        call = userSession.checkSession(user_id)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<LoginSessionModel?> {
            override fun onResponse(
                call: Call<LoginSessionModel?>,
                response: Response<LoginSessionModel?>
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

            override fun onFailure(call: Call<LoginSessionModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

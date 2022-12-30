package com.cognifygroup.vgold.login

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.utilities.APIServiceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginServiceProvider(context: Context?) {
    private val mLoginService: LoginService

    init {
        mLoginService = APIServiceFactory.createService(LoginService::class.java, context)
    }

    fun callUserLogin(email: String?, appSignature: String?, apiCallback: APICallback) {
        var call: Call<LoginModel?>? = null
        call = mLoginService.getLogin(email, appSignature)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                if (response.isSuccessful && response.body() != null && response.body()!!.status
                        .equals("200")
                ) {
                    apiCallback.onSuccess(response.body())
                } else if (response.isSuccessful && response.body() != null && response.body()!!
                        .status.equals("400")
                ) {
                    apiCallback.onSuccess(response.body())
                } else {
                    /* BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());*/
                    apiCallback.onFailure(response.body(), response.errorBody())
                }
            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }

    fun callVerifyUserLogin(
        email: String?,
        otp: String?,
        token: String?,
        apiCallback: APICallback
    ) {
        var call: Call<LoginModel?>? = null
        call = mLoginService.verifyLogin(email, otp, token)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                if (response.isSuccessful && response.body() != null && response.body()!!.status
                        .equals("200")
                ) {
                    apiCallback.onSuccess(response.body())
                } else if (response.isSuccessful && response.body() != null && response.body()!!
                        .status.equals("400")
                ) {
                    apiCallback.onSuccess(response.body())
                } else {
                    /* BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());*/
                    apiCallback.onFailure(response.body(), response.errorBody())
                }
            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

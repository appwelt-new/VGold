package com.cognifygroup.vgold.changepassword

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordServiceProvider(context: Context?) {
    private val changePasswordService: com.cognifygroup.vgold.changepassword.ChangePasswordService

    init {
        changePasswordService =
            APIServiceFactory.createService(com.cognifygroup.vgold.changepassword.ChangePasswordService::class.java, context)
    }

    fun changePassword(id: String?, otp: String?, pass: String?, apiCallback: APICallback) {
        var call: Call<com.cognifygroup.vgold.changepassword.ChangePasswordModel?>? = null
        call = changePasswordService.getChangePassword(id, otp, pass)
        val url = call.request().url().toString()
        call.enqueue(object : Callback<com.cognifygroup.vgold.changepassword.ChangePasswordModel?> {
            override fun onResponse(
                call: Call<com.cognifygroup.vgold.changepassword.ChangePasswordModel?>,
                response: Response<com.cognifygroup.vgold.changepassword.ChangePasswordModel?>
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

            override fun onFailure(call: Call<com.cognifygroup.vgold.changepassword.ChangePasswordModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    }
}

package com.cognifygroup.vgold.CPModule

import android.content.Context
import com.cognifygroup.vgold.channelpartner.*
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CPServiceProvider(context: Context?) {
    private val cpService: CPService

    init {
        cpService = APIServiceFactory.createService(CPService::class.java, context)
    }

    fun getUserDetails(id: String?, apiCallback: APICallback) {
        var call: Call<UserDetailsModel?>? = null
        call = cpService.getCPUserDetails(id)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<UserDetailsModel?> {
                override fun onResponse(
                    call: Call<UserDetailsModel?>,
                    response: Response<UserDetailsModel?>
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("300")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<UserDetailsModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }

    fun getUserGoldDetails(id: String?, apiCallback: APICallback) {
        var call: Call<UserGoldDetailsModel?>? = null
        call = cpService.getCPUserGoldDetails(id)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<UserGoldDetailsModel?> {
                override fun onResponse(
                    call: Call<UserGoldDetailsModel?>,
                    response: Response<UserGoldDetailsModel?>
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("300")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<UserGoldDetailsModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }

    fun getUserCommissionDetails(id: String?, apiCallback: APICallback) {
        var call: Call<UserCommissionDetailsModel?>? = null
        call = cpService.getCPUserCommissionDetails(id)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<UserCommissionDetailsModel?> {
                override fun onResponse(
                    call: Call<UserCommissionDetailsModel?>,
                    response: Response<UserCommissionDetailsModel?>
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("300")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<UserCommissionDetailsModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }

    fun getUserEMIDetails(id: String?, apiCallback: APICallback) {
        var call: Call<UserEMIDetailsModel?>? = null
        call = cpService.getCPUserEMIDetails(id)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<UserEMIDetailsModel?> {
                override fun onResponse(
                    call: Call<UserEMIDetailsModel?>,
                    response: Response<UserEMIDetailsModel?>
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("300")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<UserEMIDetailsModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }

    fun getUserEMIStatusDetails(id: String?, apiCallback: APICallback) {
        var call: Call<UserEMIStatusDetailsModel?>? = null
        call = cpService.getCPUserEMIStatusDetails(id)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<UserEMIStatusDetailsModel?> {
                override fun onResponse(
                    call: Call<UserEMIStatusDetailsModel?>,
                    response: Response<UserEMIStatusDetailsModel?>
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("300")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<UserEMIStatusDetailsModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }

    fun requetToBePartner(id: String?, apiCallback: APICallback) {
        var call: Call<UserEMIStatusDetailsModel?>? = null
        call = cpService.beChannelPartner(id)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<UserEMIStatusDetailsModel?> {
                override fun onResponse(
                    call: Call<UserEMIStatusDetailsModel?>,
                    response: Response<UserEMIStatusDetailsModel?>
                ) {
                    if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("200")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else if (response.isSuccessful() && response.body() != null && response.body()!!
                            .status.equals("300")
                    ) {
                        apiCallback.onSuccess(response.body())
                    } else {
                        val model: BaseServiceResponseModel = ErrorUtils().parseError(response)!!
                        apiCallback.onFailure(model, response.errorBody())
                        // apiCallback.onFailure(response.body(), response.errorBody());
                    }
                }

                override fun onFailure(call: Call<UserEMIStatusDetailsModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}


package com.cognifygroup.vgold.addcomplain

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddComplainServiceProvider(context: Context?) {

     var addComplainService: AddComplainService? = null
    init {
        addComplainService =
            APIServiceFactory.createService(AddComplainService::class.java, context)
    }

//    fun AddComplainServiceProvider(context: Context?) {
//        addComplainService =
//            APIServiceFactory.createService(AddComplainService::class.java, context)
//    }


    fun addComplain(user_id: String?, complain: String?, apiCallback: APICallback) {
        var call: Call<AddComplainModel?>? = null
        call = addComplainService?.AddComplain(user_id, complain)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<AddComplainModel?> {
                override fun onResponse(
                    call: Call<AddComplainModel?>,
                    response: Response<AddComplainModel?>
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

                override fun onFailure(call: Call<AddComplainModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }

    fun addReview(user_id: String?, comment: String?, apiCallback: APICallback) {
        var call: Call<AddComplainModel?>? = null
        call = addComplainService?.AddReview(user_id, comment)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<AddComplainModel?> {
                override fun onResponse(
                    call: Call<AddComplainModel?>,
                    response: Response<AddComplainModel?>
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

                override fun onFailure(call: Call<AddComplainModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }


    fun addFeedback(user_id: String?, comment: String?, rating: String?, apiCallback: APICallback) {
        var call: Call<AddComplainModel?>? = null
        call = addComplainService?.AddFeedback(user_id, comment, rating)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<AddComplainModel?> {
                override fun onResponse(
                    call: Call<AddComplainModel?>,
                    response: Response<AddComplainModel?>
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

                override fun onFailure(call: Call<AddComplainModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }
}

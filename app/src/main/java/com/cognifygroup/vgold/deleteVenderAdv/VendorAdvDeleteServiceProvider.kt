package com.cognifygroup.vgold.deleteVenderAdv

import android.content.Context
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.APIServiceFactory
import com.cognifygroup.vgold.utilities.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorAdvDeleteServiceProvider(context: Context?) {
    private val vendorAdvService: VendorDeleteAdvService

    init {
        vendorAdvService =
            APIServiceFactory.createService(VendorDeleteAdvService::class.java, context)
    }

    fun getDeleteVenderAdv(venderId: String?, apiCallback: APICallback) {
        var call: Call<VendorAdvModel?>? = null
        call = vendorAdvService.vendorDelete("delete", venderId)
        val url = call?.request()?.url().toString()
        if (call != null) {
            call.enqueue(object : Callback<VendorAdvModel?> {
                override fun onResponse(
                    call: Call<VendorAdvModel?>,
                    response: Response<VendorAdvModel?>
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

                override fun onFailure(call: Call<VendorAdvModel?>, t: Throwable) {
                    apiCallback.onFailure(null, null)
                }
            })
        }
    }

    fun addVenderAdv(
        operation: String?,
        venderId: String?,
        imagePath: String?,
        apiCallback: APICallback
    ) {
        var call: Call<VendorAdvModel?>? = null
        call = vendorAdvService.vendorAdd(operation, venderId, imagePath)
        val url = call!!.request().url().toString()
        call.enqueue(object : Callback<VendorAdvModel?> {
            override fun onResponse(
                call: Call<VendorAdvModel?>,
                response: Response<VendorAdvModel?>
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

            override fun onFailure(call: Call<VendorAdvModel?>, t: Throwable) {
                apiCallback.onFailure(null, null)
            }
        })
    } /*public void updateVenderAdv(String venderId, String imagePath, final APICallback apiCallback) {
        Call<VendorAdvModel> call = null;
        call = vendorAdvService.vendorUpdate("update", venderId,  imagePath);
        String url = call.request().url().toString();

        call.enqueue(new Callback<VendorAdvModel>() {
            @Override
            public void onResponse(Call<VendorAdvModel> call, Response<VendorAdvModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("200")) {
                    apiCallback.onSuccess(response.body());
                } else if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("400")) {
                    apiCallback.onSuccess(response.body());
                } else {
                    BaseServiceResponseModel model = ErrorUtils.parseError(response);
                    apiCallback.onFailure(model, response.errorBody());
                    // apiCallback.onFailure(response.body(), response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<VendorAdvModel> call, Throwable t) {
                apiCallback.onFailure(null, null);
            }
        });
    }*/
}


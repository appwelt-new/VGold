package com.cognifygroup.vgold.utilities

import com.cognifygroup.vgold.model.BaseServiceResponseModel
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

class ErrorUtils {

    fun parseError(response: Response<*>): BaseServiceResponseModel? {
        val error: BaseServiceResponseModel
        error = try {
            val converter: Converter<ResponseBody?, BaseServiceResponseModel> =
                APIServiceFactory.retrofit.responseBodyConverter(
                    BaseServiceResponseModel::class.java, arrayOfNulls(0)
                )
            converter.convert(response.errorBody())
        } catch (e: IOException) {
            return BaseServiceResponseModel()
        }
        return error
    }
}
package com.cognifygroup.vgold.utilities

import android.content.Context
import com.cognifygroup.vgold.utilities.Constants.Companion.SERVER_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit

object APIServiceFactory {
    private val httpClient = OkHttpClient.Builder()
        .readTimeout(120, TimeUnit.MINUTES)
        .connectTimeout(120, TimeUnit.MINUTES)
    private val builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(SERVER_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
    var retrofit = builder.build()
    fun <S> createService(serviceClass: Class<S>?, context: Context?): S {
        val client = httpClient.build()
        val retrofit = builder.client(client).addConverterFactory(
            GsonConverterFactory.create(

                GsonBuilder()
                    .serializeNulls()
                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    .create()
            )
        ).build()
        return retrofit.create(serviceClass)
    }
}
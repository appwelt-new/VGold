package com.cognifygroup.vgold.interfaces

interface APICallback {

    fun <T> onSuccess(serviceResponse: T)

    fun <T> onFailure(apiErrorModel: T, extras: T)
}
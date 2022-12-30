package com.cognifygroup.vgold.activities

import android.content.Context

object PrintUtil {
    fun showToast(context: Context?, msg: String?) {
        BaseActivity.showToast(context, msg)
    }

    fun showNetworkAvailableToast(context: Context?): String? {
        try {
            return if (context?.let { BaseActivity.isNetworkAvailable(it) } == true) {
                BaseActivity.showToast(context, "")
                ""
            } else {
                BaseActivity.showToast(context, "")
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

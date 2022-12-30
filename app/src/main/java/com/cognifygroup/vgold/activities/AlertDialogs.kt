package com.cognifygroup.vgold.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.location.Address
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.google.android.gms.common.api.GoogleApiClient


class AlertDialogs {
    private var mDialog: ProgressDialog? = null
    private val googleApiClient: GoogleApiClient? = null
    private val mAddress: String? = null
    var addresses: List<Address>? = null
    fun onShowProgressDialog(activity: Activity?, isShow: Boolean) {
        try {
            if (isShow) {
                mDialog = ProgressDialog.show(activity, "", "Loading...", true)
                mDialog!!.show()
            } else {
                if (mDialog!!.isShowing) mDialog!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }

    fun onShowToastNotification(activity: Activity?, msg: String?) {
        val ltoast = Toast.makeText(activity, msg, Toast.LENGTH_LONG)
        val group = ltoast.view as ViewGroup?
        val messageTextView = group!!.getChildAt(0) as TextView
        messageTextView.textSize = 16f
        ltoast.show()
    }

    fun onHideKeyBoard(mActivity: Activity) {
        val imm = mActivity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mActivity.currentFocus!!.windowToken, 0)
    }

    //Alert dialog for call admin
    fun BuyerAlert(activity: Activity?) {}


    fun getInstance(): AlertDialogs? {
       if (mInstance == null) {
            mInstance = AlertDialogs()
       }
        return mInstance
    }

    fun alertDialogOk(  context: Context?, title: String?, message: String?,
                        button: String?, resultCode: Int, setCancel: Boolean,
                        alertDialogOkListener: AlertDialogOkListener?
    ) {
        val alert = AlertDialog.Builder(
            context!!, R.style.customDialogue
        )
        alert.setCancelable(setCancel)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton(
            button
        ) { dialog, arg1 -> }
        val alertDialog = alert.create()
        alertDialog.window!!.attributes.windowAnimations = R.style.dialogTheme
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (alertDialogOkListener != null) {
                alertDialogOkListener.onDialogOk(resultCode)
                alertDialog.dismiss()
            }
        }
    }


    companion object {
        var mInstance: AlertDialogs? = null
        const val REQUEST_LOCATION = 199
        val instance: AlertDialogs?
            get() {
                if (mInstance == null) {
                    mInstance = AlertDialogs()
                }
                return mInstance
            }

//        fun alertDialogOk(
//            context: Context?, title: String?, message: String?,
//            button: String?, resultCode: Int, setCancel: Boolean,
//            alertDialogOkListener: AlertDialogOkListener?
//        ) {
//            val alert = AlertDialog.Builder(
//                context!!, R.style.customDialogue
//            )
//            alert.setCancelable(setCancel)
//            alert.setTitle(title)
//            alert.setMessage(message)
//            alert.setPositiveButton(
//                button
//            ) { dialog, arg1 -> }
//            val alertDialog = alert.create()
//            alertDialog.window!!.attributes.windowAnimations = R.style.dialogTheme
//            alertDialog.show()
//            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
//                if (alertDialogOkListener != null) {
//                    alertDialogOkListener.onDialogOk(resultCode)
//                    alertDialog.dismiss()
//                }
//            }
//        }
    }
}


//class AlertDialogs {
//
//    public var mDialog: ProgressDialog? = null
//    var mInstance: AlertDialogs? = null
//   // private val googleApiClient: GoogleApiClient? = null
//    val REQUEST_LOCATION = 199
//    private val mAddress: String? = null
//    var addresses: List<Address>? = null
//
//    fun getInstance(): AlertDialogs? {
//        if (mInstance == null) {
//            mInstance = AlertDialogs()
//        }
//        return mInstance
//    }
//
//    fun onShowProgressDialog(activity: Activity?, isShow: Boolean) {
//        try {
//            if (isShow) {
//                mDialog = ProgressDialog.show(activity, "", "Loading...", true)
//                mDialog!!.show()
//            } else {
//                if (mDialog!!.isShowing) mDialog!!.dismiss()
//            }
//        } catch (e: Exception) {
//        }
//    }
//
//    fun onShowToastNotification(activity: Activity?, msg: String?) {
//        val ltoast = Toast.makeText(activity, msg, Toast.LENGTH_LONG)
//        val group = ltoast.view as ViewGroup?
//        val messageTextView = group!!.getChildAt(0) as TextView
//        messageTextView.textSize = 16f
//        ltoast.show()
//    }
//
//    fun onHideKeyBoard(mActivity: Activity) {
//        val imm = mActivity
//            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(mActivity.currentFocus!!.windowToken, 0)
//    }
//
//    //Alert dialog for call admin
//    fun BuyerAlert(activity: Activity?) {}
//
//
    fun alertDialogOk(
        context: MoneyWalletActivity, title: String?, message: String?,
        button: String?, resultCode: Int, setCancel: Boolean,
        alertDialogOkListener: AlertDialogOkListener?
    ) {
        val alert = AlertDialog.Builder(
            context!!, R.style.customDialogue
        )
        alert.setCancelable(setCancel)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton(
            button
        ) { dialog, arg1 -> }
        val alertDialog = alert.create()
        alertDialog.window!!.attributes.windowAnimations = R.style.dialogTheme
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (alertDialogOkListener != null) {
                alertDialogOkListener.onDialogOk(resultCode)
                alertDialog.dismiss()
            }
        }
    }
//
//
//}

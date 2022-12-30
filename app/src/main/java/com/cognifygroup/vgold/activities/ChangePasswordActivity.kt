package com.cognifygroup.vgold.activities

//import com.cognifygroup.myappvgold.utilities.APICallback

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.changepassword.ChangePasswordModel
import com.cognifygroup.vgold.changepassword.ChangePasswordServiceProvider
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.TransparentProgressDialog


class ChangePasswordActivity : AppCompatActivity(),AlertDialogOkListener {

    var mAlert: AlertDialogs? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    var changePasswordServiceProvider: ChangePasswordServiceProvider? = null
    var edtOtp: EditText? = null

    var edtPass: EditText? = null
    var edtCpass: EditText? = null
    var btn_submit_otp: Button? = null

    private var otp =""
    private var pass =""


    lateinit var id: String
    private var progressDialog: TransparentProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        btn_submit_otp = findViewById(R.id.btn_submit_otp)
        edtOtp = findViewById(R.id.edtOtp)
        edtPass = findViewById(R.id.edtPass)
        edtCpass = findViewById(R.id.edtCpass)
        ButterKnife.inject(this)
        init()
        btn_submit_otp!!.setOnClickListener {
            onClickOfSubmit()
        }

    }
    fun init() {
            mAlert = AlertDialogs().getInstance()
        progressDialog = TransparentProgressDialog(this@ChangePasswordActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        changePasswordServiceProvider = ChangePasswordServiceProvider(this)
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id").toString()
        }
    }


    fun onClickOfSubmit() {
        otp = edtOtp!!.text.toString()
        pass = edtPass!!.text.toString()
        if (otp.length == 0) {
            AlertDialogs().alertDialogOk(
                this@ChangePasswordActivity, "Alert", "Enter Valid Otp",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
            //            mAlert.onShowToastNotification(ChangePasswordActivity.this, "Enter Valid Otp");
        } else if (pass.length == 0) {
            AlertDialogs().alertDialogOk(
                this@ChangePasswordActivity, "Alert", "Enter Valid password",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
            //            mAlert.onShowToastNotification(ChangePasswordActivity.this, "Enter Valid password");
        } else if (pass != edtCpass!!.text.toString()) {
            AlertDialogs().alertDialogOk(
                this@ChangePasswordActivity, "Alert", "password and confirm password not matched",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
            //            mAlert.onShowToastNotification(ChangePasswordActivity.this, "password and confirm password not matched");
        } else {
            ChangePasswordApi(id, otp, pass)
        }
    }

    private fun ChangePasswordApi(id: String, otp: String, pass: String) {
        progressDialog!!.show()
        changePasswordServiceProvider?.changePassword(id, otp, pass, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                val Status: String = (serviceResponse as com.cognifygroup.vgold.changepassword.ChangePasswordModel).getStatus()
                try {
                    val message: String = (serviceResponse as com.cognifygroup.vgold.changepassword.ChangePasswordModel).getMessage()
                    if (Status == "200") {

    //                        mAlert.onShowToastNotification(ChangePasswordActivity.this, message);
                        AlertDialogs().alertDialogOk(
                            this@ChangePasswordActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 1, false, alertDialogOkListener
                        )
                        //                        startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@ChangePasswordActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                        mAlert.onShowToastNotification(ChangePasswordActivity.this, message);
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    progressDialog!!.hide()
                }
            }

            override fun <T> onFailure(apiErrorModel: T, extras: T) {
                try {
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@ChangePasswordActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@ChangePasswordActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@ChangePasswordActivity)
                } finally {
                    progressDialog!!.hide()
                }

            }
        })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            1 -> startActivity(Intent(this@ChangePasswordActivity, LoginActivity::class.java))
        }
    }
}
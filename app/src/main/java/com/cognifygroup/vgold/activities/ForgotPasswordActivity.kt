package com.cognifygroup.vgold.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.getotp.OtpModel
import com.cognifygroup.vgold.getotp.OtpServiceProvider
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class ForgotPasswordActivity : AppCompatActivity(),AlertDialogOkListener {
    var edtEmail: EditText? = null

    var BtnSubmitEmail: Button? = null

    var mAlert: AlertDialogs? = null
    var otpServiceProvider: OtpServiceProvider? = null

    var email: String? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        edtEmail = findViewById(R.id.edtEmail)
        BtnSubmitEmail = findViewById(R.id.BtnSubmitEmail)

        ButterKnife.inject(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
        supportActionBar?.title = "forgot Password"
//        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
//        setSupportActionBar(mActionBarToolbar);
//        getSupportActionBar().setTitle("My title");
        BtnSubmitEmail!!.setOnClickListener {
            onClickofBtnSubmitEmail()
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        progressDialog = TransparentProgressDialog(this@ForgotPasswordActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        otpServiceProvider = OtpServiceProvider(this)
    }


    fun onClickofBtnSubmitEmail() {
        email = edtEmail!!.text.toString()
        if (edtEmail!!.text.toString().length == 0) {
            AlertDialogs().alertDialogOk(
                this@ForgotPasswordActivity, "Alert", "Enter Valid Email or Mobile No",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
            //            mAlert.onShowToastNotification(ForgotPasswordActivity.this, "Enter Valid Email or Mobile No");
        } else {
            OtpApi(email!!)
        }
    }


    private fun OtpApi(id: String) {
        progressDialog!!.show()
        otpServiceProvider?.getReg(id, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                val Status: String? = (serviceResponse as OtpModel).status
                try {
                    val message: String? = (serviceResponse as OtpModel).message
                    if (Status == "200") {
                        AlertDialogs().alertDialogOk(
                            this@ForgotPasswordActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 1, false, alertDialogOkListener
                        )

    //                        mAlert.onShowToastNotification(ForgotPasswordActivity.this, message);
    //                        startActivity(new Intent(ForgotPasswordActivity.this,ChangePasswordActivity.class).putExtra("id",email));
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@ForgotPasswordActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                        mAlert.onShowToastNotification(ForgotPasswordActivity.this, message);
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
                            this@ForgotPasswordActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@ForgotPasswordActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@ForgotPasswordActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            1 -> startActivity(
                Intent(
                    this@ForgotPasswordActivity,
                    ChangePasswordActivity::class.java
                ).putExtra("id", email)
            )
        }
    }
}
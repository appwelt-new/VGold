package com.cognifygroup.vgold.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import butterknife.OnClick
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class OtpActivity : AppCompatActivity(),AlertDialogOkListener {
    var edtOtp: EditText? = null
    var btnTransferMoney: Button? = null

    var otp = ""
    var amount:kotlin.String? = ""
    var no:kotlin.String? = ""

    private var transferMoneyFinalServiceProvider: TransferMoneyFinalServiceProvider? = null
    private var mAlert: AlertDialogs? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        edtOtp = findViewById(R.id.edtOtp)
        btnTransferMoney = findViewById(R.id.btnTransferMoney)

        supportActionBar!!.title ="Otp"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun init() {
        progressDialog = TransparentProgressDialog(this@OtpActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        transferMoneyFinalServiceProvider = TransferMoneyFinalServiceProvider(this)
        if (intent.hasExtra("OTP")) {
            otp = intent.getStringExtra("OTP")!!
            amount = intent.getStringExtra("AMOUNT")
            no = intent.getStringExtra("NO")
        }
        loginStatusServiceProvider = LoginStatusServiceProvider(this)
        checkLoginSession()

        btnTransferMoney!!.setOnClickListener {
            onClickOfBtnTransferMoney()
        }
    }

    private fun checkLoginSession() {
        loginStatusServiceProvider!!.getLoginStatus(VGoldApp.onGetUerId(), object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    progressDialog!!.hide()
                    val status: String? = (serviceResponse as LoginSessionModel).getStatus()
                    val message: String? = (serviceResponse as LoginSessionModel).getMessage()
                    val data: Boolean = (serviceResponse as LoginSessionModel).getData() == true
                    Log.i("TAG", "onSuccess: $status")
                    Log.i("TAG", "onSuccess: $message")
                    if (status == "200") {
                        if (!data) {
                            AlertDialogs().alertDialogOk(
                                this@OtpActivity,
                                "Alert",
                                "$message,  Please relogin to app",
                                resources.getString(R.string.btn_ok),
                                11,
                                false,
                                alertDialogOkListener
                            )
                        }
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@OtpActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                        mAlert.onShowToastNotification(AddGoldActivity.this, message);
                    }
                } catch (e: Exception) {
                    //  progressDialog.hide();
                    e.printStackTrace()
                } finally {
                    //  progressDialog.hide();
                }
            }

            override fun <T> onFailure(apiErrorModel: T, extras: T) {

                try {
                    progressDialog!!.hide()
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@OtpActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@OtpActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@OtpActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    @OnClick(R.id.btnTransferMoney)
    fun onClickOfBtnTransferMoney() {
        if (otp == edtOtp!!.text.toString()) {
            TransferMoney(VGoldApp.onGetUerId(), no!!, amount!!)
        } else {
            AlertDialogs().alertDialogOk(
                this@OtpActivity, "Alert", "Otp not Matched",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
            //            mAlert.onShowToastNotification(OtpActivity.this, "Otp not Matched");
        }
    }


    private fun TransferMoney(user_id: String?, no: String, amount: String) {
        progressDialog!!.show()
        transferMoneyFinalServiceProvider!!.transferMoney(
            user_id,
            no,
            amount,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    val Status: String? = (serviceResponse as TransferMoneyFinalModel).status
                    val message: String? = (serviceResponse as TransferMoneyFinalModel).message
                    try {
                        if (Status == "200") {
                            AlertDialogs().alertDialogOk(
                                this@OtpActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )


                            /* mAlert.onShowToastNotification(OtpActivity.this, message);
                        startActivity(new Intent(OtpActivity.this,MainActivity.class));*/
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@OtpActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )

//                        mAlert.onShowToastNotification(OtpActivity.this, message);
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
                                this@OtpActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@OtpActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@OtpActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            1 -> startActivity(Intent(this@OtpActivity, MainActivity::class.java))
            11 -> {
                val LogIntent = Intent(this@OtpActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

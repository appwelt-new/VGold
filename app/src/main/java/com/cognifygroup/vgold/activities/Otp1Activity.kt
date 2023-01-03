package com.cognifygroup.vgold.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.cognifygroup.vgold.sellGold.SellGoldModel
import com.cognifygroup.vgold.sellGold.SellGoldServiceProvider
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalModel
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class Otp1Activity : AppCompatActivity(), AlertDialogOkListener {
    var edtOtp: EditText? = null

    var btnTransferGold: Button? = null

    var otp = ""
    var amount: kotlin.String? = ""
    var no: kotlin.String? = ""
    var weight: kotlin.String? = ""
    var moveFrom: kotlin.String? = ""

    private var transferGoldFinalServiceProvider: TransferGoldFinalServiceProvider? = null
    private var sellGoldServiceProvider: SellGoldServiceProvider? = null
    private var mAlert: AlertDialogs? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp1)

        edtOtp = findViewById(R.id.edtOtp)
        btnTransferGold = findViewById(R.id.btnTransferGold)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
        btnTransferGold!!.setOnClickListener {
            onClickOfbtnTransferGold()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun init() {
        progressDialog = TransparentProgressDialog(this@Otp1Activity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        transferGoldFinalServiceProvider = TransferGoldFinalServiceProvider(this)
        sellGoldServiceProvider = SellGoldServiceProvider(this)

//        if (getIntent().hasExtra("OTP")) {
        otp = intent.getStringExtra("OTP")!!
        amount = intent.getStringExtra("AMOUNT")
        no = intent.getStringExtra("NO")
        weight = intent.getStringExtra("Weight")
        moveFrom = intent.getStringExtra("moveFrom")
        //        }
        if (moveFrom == "SellGold") {
            btnTransferGold!!.text = "Sell"
        } else {
            btnTransferGold!!.text = "Transfer"
        }
        loginStatusServiceProvider = LoginStatusServiceProvider(this)
        checkLoginSession()
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
                                this@Otp1Activity,
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
                            this@Otp1Activity, "Alert", message,
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
                            this@Otp1Activity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@Otp1Activity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@Otp1Activity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    fun onClickOfbtnTransferGold() {
        if (moveFrom == "SellGold") {
            val OTP = edtOtp!!.text.toString()
            if (!TextUtils.isEmpty(OTP)) {
                verifyOTP(VGoldApp.onGetUerId(), OTP)
            }
        } else {
            if (otp == edtOtp!!.text.toString()) {
                TransferGold(VGoldApp.onGetUerId(), no!!, weight!!)
            } else {
                AlertDialogs().alertDialogOk(
                    this@Otp1Activity, "Alert", "Otp not Matched",
                    resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                )
                //            mAlert.onShowToastNotification(Otp1Activity.this, "Otp not Matched");
            }
        }
    }

    private fun verifyOTP(user_id: String?, otp: String) {
        progressDialog!!.show()
        sellGoldServiceProvider!!.verifyOTP(user_id, "check_otp", otp, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                val Status: String? = (serviceResponse as SellGoldModel).getStatus()
                val message: String? = (serviceResponse as SellGoldModel).getMessage()
                try {
                    if (Status == "200") {
                        AttemptToSellGold(VGoldApp.onGetUerId(), weight!!, amount!!)
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@Otp1Activity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )

//                        mAlert.onShowToastNotification(PayActivity.this, message);
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
                            this@Otp1Activity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@Otp1Activity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@Otp1Activity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    private fun AttemptToSellGold(user_id: String?, gold: String, amount: String) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        sellGoldServiceProvider!!.getAddBankDetails(user_id, gold, amount, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    val status: String? = (serviceResponse as SellGoldModel).getStatus()
                    val message: String? = (serviceResponse as SellGoldModel).getMessage()
                    if (status == "200") {

                        //  mAlert.onShowToastNotification(SellGoldActivity.this, message);
                        val intent = Intent(this@Otp1Activity, SuccessActivity::class.java)
                        intent.putExtra("message", message)
                        startActivity(intent)
                        finish()
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@Otp1Activity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )

//                        mAlert.onShowToastNotification(SellGoldActivity.this, message);
//                        Intent intent = new Intent(SellGoldActivity.this, MainActivity.class);
//                        startActivity(intent);
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
                            this@Otp1Activity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@Otp1Activity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@Otp1Activity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }


    private fun TransferGold(user_id: String?, no: String, amount: String) {
        progressDialog!!.show()
        transferGoldFinalServiceProvider!!.transferMoney(
            user_id,
            no,
            amount,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    val Status: String? = (serviceResponse as TransferGoldFinalModel).getStatus()
                    val message: String? = (serviceResponse as TransferGoldFinalModel).getMessage()
                    try {
                        if (Status == "200") {
                            AlertDialogs().alertDialogOk(
                                this@Otp1Activity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )


//                        mAlert.onShowToastNotification(Otp1Activity.this, message);
//                        startActivity(new Intent(Otp1Activity.this,MainActivity.class));
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@Otp1Activity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                            //                        mAlert.onShowToastNotification(Otp1Activity.this, message);
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
                                this@Otp1Activity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@Otp1Activity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@Otp1Activity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            1 -> startActivity(Intent(this@Otp1Activity, MainActivity::class.java))
            11 -> {
                val LogIntent = Intent(this@Otp1Activity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}


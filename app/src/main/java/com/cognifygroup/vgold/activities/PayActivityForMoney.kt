package com.cognifygroup.vgold.activities
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.OnClick
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpModel
import com.cognifygroup.vgold.payMoneyOtp.PayMoneyOtpServiceProvider
import com.cognifygroup.vgold.transferMoney.TransferMoneyModel
import com.cognifygroup.vgold.transferMoney.TransferMoneyServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class PayActivityForMoney : AppCompatActivity(), AlertDialogOkListener {
    var name: String? = null
    var mobile: String? = null
    var mobileno: String? = null
    var otp: String? = null
    var enterbalence = 0.0
    var whichactivity = "0"

    var txtName: TextView? = null
    var txtMobile: TextView? = null
    var edtAmount: EditText? = null
    var btnPay: Button? = null
    var txtWalletBalence: TextView? = null
    var txtError: TextView? = null
    var btnRefer: Button? = null

    var btnAddGoldWallet: Button? = null

    private var transferMoneyServiceProvider: TransferMoneyServiceProvider? = null
    private var payMoneyOtpServiceProvider: PayMoneyOtpServiceProvider? = null
    var getAllTransactionMoneyServiceProvider: GetAllTransactionMoneyServiceProvider? = null
    private var transferMoneyFinalServiceProvider: TransferMoneyFinalServiceProvider? = null
    private var mAlert: AlertDialogs? = null

    var balance: String? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_for_money)

        supportActionBar?.title = "Pay For Money "
        sharedPreferences =
            this@PayActivityForMoney.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()

        txtName = findViewById(R.id.txtName)
        txtMobile = findViewById(R.id.txtMobile)
        txtWalletBalence = findViewById(R.id.txtWalletBalence)
        txtError = findViewById(R.id.txtError)
        edtAmount = findViewById(R.id.edtAmount)
        btnPay = findViewById(R.id.btnPay)
        btnRefer = findViewById(R.id.btnRefer)
        btnAddGoldWallet = findViewById(R.id.btnAddGoldWallet)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
    }

    fun init() {
        progressDialog = TransparentProgressDialog(this@PayActivityForMoney)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        transferMoneyServiceProvider = TransferMoneyServiceProvider(this)
        payMoneyOtpServiceProvider = PayMoneyOtpServiceProvider(this)
        getAllTransactionMoneyServiceProvider = GetAllTransactionMoneyServiceProvider(this)
        transferMoneyFinalServiceProvider = TransferMoneyFinalServiceProvider(this)

        /*btnPay.setVisibility(View.INVISIBLE);*/AttemptToGetMoneyTransactionHistory(userId)
        val intent = intent
        name = intent.getStringExtra("name")
        mobile = intent.getStringExtra("number")
        mobileno = intent.getStringExtra("mobileno")
        val barcode = intent.getStringExtra("code")
        whichactivity = intent.getStringExtra("whichactivity")!!
        if (whichactivity == "1") {
            txtName!!.text = name
            txtMobile!!.text = mobile
        }
        if (whichactivity == "3") {
            txtMobile!!.text = mobileno
        }
        if (whichactivity == "2") {
            // close the activity in case of empty barcode
            if (TextUtils.isEmpty(barcode)) {
                AlertDialogs().alertDialogOk(
                    this@PayActivityForMoney, "Alert", "Barcode is empty!",
                    resources.getString(R.string.btn_ok), 2, false, alertDialogOkListener
                )
                //                mAlert.onShowToastNotification(PayActivityForMoney.this, "Barcode is empty!");
//                finish();
            }
            if (barcode != null || barcode != "") {
                searchBarcode(barcode)
            }
        }
        loginStatusServiceProvider = LoginStatusServiceProvider(this)
        checkLoginSession()
        btnPay?.setOnClickListener {
            onClickOfbtnPay()
        }
    }

    private fun checkLoginSession() {
        loginStatusServiceProvider?.getLoginStatus(userId, object : APICallback {
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
                                this@PayActivityForMoney,
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
                            this@PayActivityForMoney, "Alert", message,
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
                            this@PayActivityForMoney,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    private fun searchBarcode(barcode: String?) {
        if (barcode != "" || barcode != null) {
            txtMobile!!.text = barcode.toString()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getTransferMoney(txtMobile!!.text.toString())
    }


    @OnClick(R.id.btnRefer)
    fun onClickOfBtnRefer() {
        startActivity(
            Intent(this@PayActivityForMoney, ReferActivity::class.java)
                .putExtra("no", txtMobile!!.text.toString())
        )
    }

    @OnClick(R.id.btnAddGoldWallet)
    fun onClickOfBtnAddGoldTowallet() {
        startActivity(Intent(this@PayActivityForMoney, AddMoneyActivity::class.java))
    }


    fun onClickOfbtnPay() {
        val amount = edtAmount!!.text.toString()
        val name = txtName!!.text.toString()
        val walletbalence = balance!!.toDouble()
        if (amount != "" && amount != null) {
            enterbalence = edtAmount!!.text.toString().toDouble()
        }
        if (name == "" || name == null) {
            AlertDialogs().alertDialogOk(
                this@PayActivityForMoney, "Alert", "This number is not register with Vgold",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )

//            mAlert.onShowToastNotification(PayActivityForMoney.this, "This number is not register with Vgold");
        } else {
            if (amount != "" && amount != null) {
                if (enterbalence <= walletbalence) {
                    TransferMoney(userId, txtMobile!!.text.toString(), amount)
                } else {
                    btnAddGoldWallet!!.visibility = View.VISIBLE
                    AlertDialogs().alertDialogOk(
                        this@PayActivityForMoney, "Alert", "Insufficient wallet balence",
                        resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                    )
                    //                    mAlert.onShowToastNotification(PayActivityForMoney.this, "Insufficient wallet balence");
                }
            } else {
                AlertDialogs().alertDialogOk(
                    this@PayActivityForMoney, "Alert", "Enter amount",
                    resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                )
                //                mAlert.onShowToastNotification(PayActivityForMoney.this, "Enter amount");
            }
        }
    }


    private fun getOtp(user_id: String) {
        progressDialog!!.show()
        payMoneyOtpServiceProvider?.getAddBankDetails(user_id, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                val Status: String? = (serviceResponse as PayMoneyOtpModel).status
                val message: String? = (serviceResponse as PayMoneyOtpModel).message
                otp = (serviceResponse as PayMoneyOtpModel).otp
                try {
                    if (Status == "200") {

                        //    mAlert.onShowToastNotification(PayActivityForMoney.this, "Otp sent to your register mobile no and mail");
                        startActivity(
                            Intent(
                                this@PayActivityForMoney,
                                OtpActivity::class.java
                            ).putExtra("OTP", otp)
                                .putExtra("AMOUNT", edtAmount!!.text.toString())
                                .putExtra("NO", txtMobile!!.text.toString())
                        )
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@PayActivityForMoney, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )

    //                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
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
                            this@PayActivityForMoney,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    private fun getTransferMoney(no: String) {

        // mAlert.onShowProgressDialog(PayActivityForMoney.this, true);
        transferMoneyServiceProvider?.getAddBankDetails(no, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                val Status: String? = (serviceResponse as TransferMoneyModel).getStatus()
                val message: String? = (serviceResponse as TransferMoneyModel).getMessage()
                val Name: String? = (serviceResponse as TransferMoneyModel).getName()
                txtName!!.text = Name
                try {
                    if (Status == "200") {

    //                        AlertDialogs.alertDialogOk(PayActivityForMoney.this, "Alert", message,
    //                                getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener);
    //                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
                        // btnPay.setVisibility(View.VISIBLE);
                    } else {
    //                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
                        txtError!!.visibility = View.VISIBLE
                        btnRefer!!.visibility = View.VISIBLE
                        btnPay!!.visibility = View.GONE
                        edtAmount!!.visibility = View.GONE
                        AlertDialogs().alertDialogOk(
                            this@PayActivityForMoney, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
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
                            this@PayActivityForMoney,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }


    private fun AttemptToGetMoneyTransactionHistory(user_id: String?) {
        //  mAlert.onShowProgressDialog(PayActivityForMoney.this, true);
        getAllTransactionMoneyServiceProvider!!.getAllTransactionMoneyHistory(
            user_id,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? =
                            (serviceResponse as GetAllTransactionMoneyModel).status
                        val message: String? =
                            (serviceResponse as GetAllTransactionMoneyModel).message
                        balance =
                            (serviceResponse as GetAllTransactionMoneyModel).wallet_Balance
                        txtWalletBalence!!.text = "\u20B9 $balance"
                        val mArrMoneyTransactonHistory: ArrayList<GetAllTransactionMoneyModel.Data>? =
                            (serviceResponse as GetAllTransactionMoneyModel).data
                        if (status == "200") {

                            //       mAlert.onShowToastNotification(PayActivityForMoney.this, message);
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayActivityForMoney,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                            //                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
                            // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(PayActivityForMoney.this));
                            //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(PayActivityForMoney.this,mArrGoldBookingHistory));
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
                                this@PayActivityForMoney,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
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
                                this@PayActivityForMoney,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

//                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
//                        startActivity(new Intent(PayActivityForMoney.this,SuccessActivity.class));
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayActivityForMoney,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )

//                        mAlert.onShowToastNotification(PayActivityForMoney.this, message);
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
                                this@PayActivityForMoney,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@PayActivityForMoney)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            1 -> startActivity(Intent(this@PayActivityForMoney, SuccessActivity::class.java))
            2 -> finish()
            11 -> {
                val LogIntent = Intent(this@PayActivityForMoney, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

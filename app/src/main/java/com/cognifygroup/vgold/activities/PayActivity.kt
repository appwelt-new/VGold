package com.cognifygroup.vgold.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import butterknife.OnClick
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.*
import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpModel
import com.cognifygroup.vgold.payGoldOtp.PayGoldOtpServiceProvider
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalModel
import com.cognifygroup.vgold.transferGoldFinal.TransferGoldFinalServiceProvider
import com.cognifygroup.vgold.transferMoney.TransferMoneyModel
import com.cognifygroup.vgold.transferMoney.TransferMoneyServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import java.text.DecimalFormat

class PayActivity : AppCompatActivity(),AlertDialogOkListener {
    var name: String? = null
    var mobile: String? = null
    var mobileno: String? = null
    var otp:kotlin.String? = null
    var whichactivity = "0"

    var txtName: TextView? = null
    var txtMobile: TextView? = null
    var edtAmount: EditText? = null
    var btnPay: Button? = null
    var txtError: TextView? = null
    var btnRefer: Button? = null

    var txtGoldRate: TextView? = null
    var txtViewForGold: TextView? = null

    var tt: TextView? = null

    var txtWalletBalence: TextView? = null

    var txtGoldWeight: TextView? = null
    var btnAddGoldWallet: Button? = null
    var llConversion: LinearLayout? = null

    var acg: LinearLayout? = null
    var edtGoldWeight: EditText? = null
    var txtSellAmount: TextView? = null
    var imgSwap: ImageView? = null
    var imgSwap1: ImageView? = null

    var balance: String? = null
    var result = 0.0
    var result1 = 0.0
    var amount = 0.0
    var weight = 0.0
    var walletbalence = 0.0
    var goldWeight = "0.0"
    var payment_option = ""
    var todayGoldRate = "0"
    var enterbalence = 0.0
    var enterbalence1 = 0.0

    private val alertDialogOkListener: AlertDialogOkListener = this

    private var transferMoneyServiceProvider: TransferMoneyServiceProvider? = null
    private var payGoldOtpServiceProvider: PayGoldOtpServiceProvider? = null
    var getTodayGoldRateServiceProvider: GetTodayGoldRateServiceProvider? = null
    var getAllTransactionGoldServiceProvider: GetAllTransactionGoldServiceProvider? = null
    private var transferGoldFinalServiceProvider: TransferGoldFinalServiceProvider? = null
    private var mAlert: AlertDialogs? = null
    private var progressDialog: TransparentProgressDialog? = null
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        imgSwap1 = findViewById(R.id.imgSwap1)
        imgSwap = findViewById(R.id.imgSwap)
        txtSellAmount = findViewById(R.id.txtSellAmount)
        edtGoldWeight = findViewById(R.id.edtGoldWeight)
        acg = findViewById(R.id.acg)
        llConversion = findViewById(R.id.llConversion)
        btnAddGoldWallet = findViewById(R.id.btnAddGoldWallet)
        txtGoldWeight = findViewById(R.id.txtGoldWeight)
        txtWalletBalence = findViewById(R.id.txtWalletBalence)
        tt = findViewById(R.id.tt)
        txtViewForGold = findViewById(R.id.txtViewForGold)
        txtGoldRate = findViewById(R.id.txtGoldRate)
        btnRefer = findViewById(R.id.btnRefer)
        txtError = findViewById(R.id.txtError)
        btnPay = findViewById(R.id.btnPay)
        edtAmount = findViewById(R.id.edtAmount)
        txtMobile = findViewById(R.id.txtMobile)
        txtName = findViewById(R.id.txtName)
        imgSwap = findViewById(R.id.imgSwap)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()

        imgSwap!!.setOnClickListener {
            onClickOfImgSwap()
        }
        imgSwap1!!.setOnClickListener {
            onClickOfImgSwap1()
        }
        btnAddGoldWallet!!.setOnClickListener {
            onClickOfBtnAddGoldTowallet()
        }
        btnPay!!.setOnClickListener {
            onClickOfbtnPay()
        }
        btnRefer!!.setOnClickListener {
            onClickOfBtnRefer()
        }
    }

    fun init() {
        progressDialog = TransparentProgressDialog(this@PayActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        transferMoneyServiceProvider = TransferMoneyServiceProvider(this)
        payGoldOtpServiceProvider = PayGoldOtpServiceProvider(this)
        getAllTransactionGoldServiceProvider = GetAllTransactionGoldServiceProvider(this)
        getTodayGoldRateServiceProvider = GetTodayGoldRateServiceProvider(this)
        transferGoldFinalServiceProvider = TransferGoldFinalServiceProvider(this)
        val textWatcher1: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //edtGoldWeight.addTextChangedListener(null);
                if (s.length != 0 && s.toString() != ".") {
                    weight = edtGoldWeight!!.text.toString().toDouble()
                    result1 = weight * todayGoldRate.toDouble()
                    if (result1 != 0.00) {
                        txtSellAmount!!.text = "" + DecimalFormat("##.##").format(result1) + " ₹"
                    }
                } else {
                    txtSellAmount!!.text = ""
                }
            }

            override fun afterTextChanged(s: Editable) {
                //edtGoldWeight.addTextChangedListener(null);
            }
        }
        edtGoldWeight!!.addTextChangedListener(textWatcher1)
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //edtGoldWeight.addTextChangedListener(null);
                if (s.length != 0 && s.toString() != ".") {
                    amount = edtAmount!!.text.toString().toDouble()
                    result = amount / todayGoldRate.toDouble()
                    if (result != 0.00) {
                        goldWeight = "" + DecimalFormat("##.###").format(result)
                        txtGoldWeight!!.text = "" + DecimalFormat("##.###").format(result) + " GM"
                    }
                } else {
                    txtGoldWeight!!.text = ""
                }
            }

            override fun afterTextChanged(s: Editable) {
                //edtGoldWeight.addTextChangedListener(null);
            }
        }
        edtAmount!!.addTextChangedListener(textWatcher)
        AttemptToGetGoldTransactionHistory(VGoldApp.onGetUerId())
        AttemptToGetTodayGoldRate()
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
                    this@PayActivity, "Alert", "Barcode is empty!",
                    resources.getString(R.string.btn_ok), 4, false, alertDialogOkListener
                )

//                mAlert.onShowToastNotification(PayActivity.this, "Barcode is empty!");
            }
            if (barcode != null || barcode != "") {
                searchBarcode(barcode)
            }
        }
        loginStatusServiceProvider = LoginStatusServiceProvider(this)
        checkLoginSession()
    }

    private fun checkLoginSession() {
        loginStatusServiceProvider?.getLoginStatus(VGoldApp.onGetUerId(), object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    progressDialog?.hide()
                    val status: String? = (serviceResponse as LoginSessionModel).getStatus()
                    val message: String? = (serviceResponse as LoginSessionModel).getMessage()
                    val data: Boolean = (serviceResponse as LoginSessionModel).getData() == true
                    Log.i("TAG", "onSuccess: $status")
                    Log.i("TAG", "onSuccess: $message")
                    if (status == "200") {
                        if (!data) {
                            AlertDialogs().alertDialogOk(
                                this@PayActivity,
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
                            this@PayActivity, "Alert", message,
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
                    progressDialog?.hide()
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@PayActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@PayActivity)
                    }
                } catch (e: Exception) {
                    progressDialog?.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@PayActivity)
                } finally {
                    progressDialog?.hide()
                }
            }
        })
    }

    private fun searchBarcode(barcode: String?) {
        if (barcode != "" || barcode != null) {
            txtMobile!!.text = barcode.toString()
        }
    }

    fun onClickOfImgSwap() {
        llConversion!!.visibility = View.GONE
        acg!!.visibility = View.VISIBLE
    }

    fun onClickOfImgSwap1() {
        llConversion!!.visibility = View.VISIBLE
        acg!!.visibility = View.GONE
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
    fun onClickOfBtnAddGoldTowallet() {
        startActivity(Intent(this@PayActivity, PayActivity::class.java))
    }

    fun showGoldTransferDialog() {
        val alertDialog = AlertDialog.Builder(this@PayActivity)

        // Setting Dialog Title
        alertDialog.setTitle("")

        // Setting Dialog Message
        alertDialog.setMessage("Do you really want to transfer gold")

        // On pressing Settings button
        alertDialog.setPositiveButton(
            "Yes"
        ) { dialog, which ->
            if (llConversion!!.visibility == View.VISIBLE) {
                val name = txtName!!.text.toString()
                walletbalence = balance!!.toDouble()
                val GoldW = goldWeight
                if (GoldW != "" && goldWeight != "null") {
                    enterbalence = GoldW.toDouble()
                }
                if (name == "" || name == null) {
                    AlertDialogs().alertDialogOk(
                        this@PayActivity,
                        "Alert",
                        "He is not a Registered Member, hence refer him.",
                        resources.getString(R.string.btn_ok),
                        2,
                        false,
                        alertDialogOkListener
                    )

                    //                        mAlert.onShowToastNotification(PayActivity.this, "He is not a Registered Member, hence refer him.");

                    //                        Toast.makeText(PayActivity.this,"this number is not register with Vgold",Toast.LENGTH_LONG).show();
                } else {
                    if (result != 0.0) {
                        if (enterbalence <= walletbalence) {
                            // Toast.makeText(PayActivity.this,""+GoldW,Toast.LENGTH_LONG).show();
                            getOtp(VGoldApp.onGetUerId(), txtMobile!!.text.toString(), GoldW)
                            //                                TransferGold(VGoldApp.onGetUerId(),txtMobile.getText().toString(),GoldW);
                        } else {
                            btnAddGoldWallet!!.visibility = View.VISIBLE
                            AlertDialogs().alertDialogOk(
                                this@PayActivity,
                                "Alert",
                                "Insufficient wallet balence",
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                            //                                mAlert.onShowToastNotification(PayActivity.this, "Insufficient wallet balence");
                        }
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@PayActivity, "Alert", "enter Amount",
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                            mAlert.onShowToastNotification(PayActivity.this, "enter Amount");
                    }
                }
            } else {
                val name = txtName!!.text.toString()
                walletbalence = balance!!.toDouble()
                if (name == "" || name == null) {
                    AlertDialogs().alertDialogOk(
                        this@PayActivity,
                        "Alert",
                        "He is not a Registered Member, hence refer him.",
                        resources.getString(R.string.btn_ok),
                        0,
                        false,
                        alertDialogOkListener
                    )

                    //                        mAlert.onShowToastNotification(PayActivity.this, "You do not have this Privilege as you are not a registered member");
                    //                        mAlert.onShowToastNotification(PayActivity.this, "He is not a Registered Member, hence refer him.");
                } else if (edtGoldWeight!!.text.toString() != "") {
                    enterbalence1 = edtGoldWeight!!.text.toString().toDouble()
                    if (enterbalence1 != 0.0) {
                        if (enterbalence1 <= walletbalence) {
                            getOtp(
                                VGoldApp.onGetUerId(),
                                txtMobile!!.text.toString(),
                                edtGoldWeight!!.text.toString()
                            )
                            //TODO..........................
                            //                                TransferGold(VGoldApp.onGetUerId(),txtMobile.getText().toString(),edtGoldWeight.getText().toString());
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayActivity,
                                "Alert",
                                "Insufficient wallet balence",
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )

                            //                                mAlert.onShowToastNotification(PayActivity.this, "Insufficient wallet balence");
                        }
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@PayActivity, "Alert", "Please enter amount",
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                            mAlert.onShowToastNotification(PayActivity.this, "Please enter amount");
                    }
                } else {
                    AlertDialogs().alertDialogOk(
                        this@PayActivity, "Alert", "Please enter amount",
                        resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                    )
                    //                        mAlert.onShowToastNotification(PayActivity.this, "Please enter amount");


                    /*String GoldWe=edtGoldWeight.getText().toString();
                        if (!GoldWe.equals("") && !GoldWe.equals("null")){
                            enterbalence1= Double.parseDouble(GoldWe);
                        }

                        double walletbalence= Double.parseDouble(balance);

                        if (enterbalence1 <= walletbalence){
                          //  Toast.makeText(PayActivity.this,""+GoldWe,Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(PayActivity.this,"Insufficient wallet balence",Toast.LENGTH_LONG).show();
                        }*/
                }
            }
        }

        // on pressing cancel button
        alertDialog.setNegativeButton(
            "No"
        ) { dialog, which -> dialog.cancel() }
        // Showing Alert Message
        alertDialog.show()
    }

    fun onClickOfbtnPay() {
        showGoldTransferDialog()
    }

    @OnClick(R.id.btnRefer)
    fun onClickOfBtnRefer() {
        startActivity(
            Intent(this@PayActivity, ReferActivity::class.java)
                .putExtra("no", txtMobile!!.text.toString())
        )
    }

    private fun getOtp(user_id: String?, mobNo: String, weight: String) {
        progressDialog?.show()
        payGoldOtpServiceProvider?.getAddBankDetails(user_id, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                val Status: String? = (serviceResponse as PayGoldOtpModel).getStatus()
                val message: String? = (serviceResponse as PayGoldOtpModel).getMessage()
                otp = (serviceResponse as PayGoldOtpModel).getOtp()
                try {
                    if (Status == "200") {
                        walletbalence = balance!!.toDouble()
                        //                 GoldW = goldWeight;
                        startActivity(
                            Intent(this@PayActivity, OtpVerificationActivity::class.java)
                                .putExtra("OTP", otp)
                                .putExtra("AMOUNT", "" + result)
                                .putExtra("Weight", goldWeight)
                                .putExtra("NO", txtMobile!!.text.toString())
                                .putExtra("moveFrom", "payActivity")
                        )

                        /* startActivity(new Intent(PayActivity.this, Otp1Activity.class)
                                    .putExtra("OTP", otp)
                                    .putExtra("AMOUNT", "" + result)
                                    .putExtra("Weight", goldWeight)
                                    .putExtra("NO", txtMobile.getText().toString())
                                    .putExtra("moveFrom", "payActivity")
                            );*/

    //                        AlertDialogs.alertDialogOk(PayActivity.this, "Alert", "Otp sent to your register mobile no and mail",
    //                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);

                        /*startActivity(new Intent(PayActivity.this, Otp1Activity.class)
                                    .putExtra("OTP", otp)
                                    .putExtra("AMOUNT", "" + result)
                                    .putExtra("Weight", weight)
                                    .putExtra("NO", mobNo));*/
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@PayActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )

    //                        mAlert.onShowToastNotification(PayActivity.this, message);
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    progressDialog?.hide()
                }
            }

            override fun <T> onFailure(apiErrorModel: T, extras: T) {
                try {
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@PayActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@PayActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@PayActivity)
                } finally {
                    progressDialog?.hide()
                }
            }
        })
    }


    private fun getTransferMoney(no: String) {
        progressDialog?.show()
        transferMoneyServiceProvider?.getAddBankDetails(no, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                val Status: String? = (serviceResponse as TransferMoneyModel).getStatus()
                val message: String? = (serviceResponse as TransferMoneyModel).getMessage()
                val Name: String? = (serviceResponse as TransferMoneyModel).getName()
                txtName!!.text = Name
                try {
                    if (Status == "200") {

                        //  Toast.makeText(PayActivity.this,message,Toast.LENGTH_LONG).show();
                    } else {

    //                        mAlert.onShowToastNotification(PayActivity.this, message);
                        txtError!!.visibility = View.VISIBLE
                        btnRefer!!.visibility = View.VISIBLE
                        btnPay!!.visibility = View.GONE
                        edtAmount!!.visibility = View.GONE
                        llConversion!!.visibility = View.GONE
                        acg!!.visibility = View.GONE
                        txtViewForGold!!.visibility = View.GONE
                        tt!!.visibility = View.GONE
                        txtGoldRate!!.visibility = View.GONE
                        AlertDialogs().alertDialogOk(
                            this@PayActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    progressDialog?.hide()
                }
            }

            override fun <T> onFailure(apiErrorModel: T, extras: T) {
                try {
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@PayActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@PayActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@PayActivity)
                } finally {
                    progressDialog?.hide()
                }
            }
        })
    }

    private fun AttemptToGetGoldTransactionHistory(user_id: String?) {
        // mAlert.onShowProgressDialog(PayActivity.this, true);
        getAllTransactionGoldServiceProvider?.getAllTransactionGoldHistory(
            user_id,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? =
                            (serviceResponse as GetAllTransactionGoldModel).status
                        val message: String? =
                            (serviceResponse as GetAllTransactionGoldModel).message
                        balance = (serviceResponse as GetAllTransactionGoldModel).gold_Balance
                        txtWalletBalence!!.text = "$balance GM"
                        val mArrGoldTransactonHistory: ArrayList<GetAllTransactionGoldModel.Data>? =
                            (serviceResponse as GetAllTransactionGoldModel).data
                        if (status == "200") {
                            // recyclerViewGoldWallet.setLayoutManager(new LinearLayoutManager(PayActivity.this));
                            //recyclerViewGoldWallet.setAdapter(new GoldTransactionAdapter(PayActivity.this,mArrGoldTransactonHistory));

                            // mAlert.onShowToastNotification(PayActivity.this, message);
                        } else {
    //                        mAlert.onShowToastNotification(PayActivity.this, message);
                            AlertDialogs().alertDialogOk(
                                this@PayActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )

                            // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(MoneyWalletActivity.this));
                            //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(MoneyWalletActivity.this,mArrGoldBookingHistory));
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        progressDialog?.hide()
                    }
                }

                override fun <T> onFailure(apiErrorModel: T, extras: T) {

                    try {
                        if (apiErrorModel != null) {
                            PrintUtil.showToast(
                                this@PayActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@PayActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@PayActivity)
                    } finally {
                        progressDialog?.hide()
                    }
                }
            })
    }

    private fun TransferGold(user_id: String, no: String, amount: String) {
        progressDialog?.show()
        transferGoldFinalServiceProvider?.transferMoney(user_id, no, amount, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                val Status: String? = (serviceResponse as TransferGoldFinalModel).getStatus()
                val message: String? = (serviceResponse as TransferGoldFinalModel).getMessage()
                try {
                    if (Status == "200") {
                        AlertDialogs().alertDialogOk(
                            this@PayActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 3, false, alertDialogOkListener
                        )

    //                        mAlert.onShowToastNotification(PayActivity.this, message);
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@PayActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                        mAlert.onShowToastNotification(PayActivity.this, message);
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    progressDialog?.hide()
                }
            }

            override fun <T> onFailure(apiErrorModel: T, extras: T) {

                try {
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@PayActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@PayActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@PayActivity)
                } finally {
                    progressDialog?.hide()
                }
            }
        })
    }

    private fun AttemptToGetTodayGoldRate() {
        //mAlert.onShowProgressDialog(PayActivity.this, true);
        getTodayGoldRateServiceProvider?.getTodayGoldRate(VGoldApp.onGetUerId(),object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    val status: String? = (serviceResponse as GetTodayGoldRateModel).status
                    val message: String? = (serviceResponse as GetTodayGoldRateModel).message
                    todayGoldRate =
                        (serviceResponse as GetTodayGoldRateModel).gold_purchase_rate.toString()
                    txtGoldRate!!.text = "₹ $todayGoldRate/GM"
                    if (status == "200") {
                        // mAlert.onShowToastNotification(PayActivity.this, message);
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@PayActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                        mAlert.onShowToastNotification(PayActivity.this, message);
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    progressDialog?.hide()
                }
            }

            override fun <T> onFailure(apiErrorModel: T, extras: T) {

                try {
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@PayActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@PayActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@PayActivity)
                } finally {
                    progressDialog?.hide()
                }
            }
        })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            2 -> {
                val intent = Intent(this@PayActivity, ReferActivity::class.java)
                startActivity(intent)
            }
            1 -> {
                walletbalence = balance!!.toDouble()
                //                 GoldW = goldWeight;
                startActivity(
                    Intent(this@PayActivity, Otp1Activity::class.java)
                        .putExtra("OTP", otp)
                        .putExtra("AMOUNT", "" + result)
                        .putExtra("Weight", goldWeight)
                        .putExtra("NO", txtMobile!!.text.toString())
                        .putExtra("moveFrom", "payActivity")
                )
            }
            3 -> startActivity(Intent(this@PayActivity, SuccessActivity::class.java))
            4 -> finish()
            11 -> {
                val LogIntent = Intent(this@PayActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

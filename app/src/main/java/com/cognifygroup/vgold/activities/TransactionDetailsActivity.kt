package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.OnClick
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class TransactionDetailsActivity : AppCompatActivity(),AlertDialogOkListener {

    var txtTxnInstallmentprice: TextView? = null
    var txtTxnRemainingAmt: TextView? = null

    /* @InjectView(R.id.txtTxnPeriod)
    TextView txtTxnPeriod;*/
    var txtTxnId: TextView? = null
    var txtTxnBankDetails: TextView? = null
    var txtTxnpaymentMethod: TextView? = null

    /*@InjectView(R.id.txtTxnchequeNo)
    TextView txtTxnchequeNo;*/
    /*  @InjectView(R.id.txtTxnAdminStatus)
    TextView txtTxnAdminStatus;*/
    var imgGoldTransactionDetails: Button? = null
    var txtReciptNo: TextView? = null
    var txtDate: TextView? = null
    var txtName: TextView? = null
    var txtCrn: TextView? = null
    var txtGoldAccount: TextView? = null
    var txtNextDueDate: TextView? = null

    var recipt_no: String? = null

    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    private var progressDialog: TransparentProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_details)


        sharedPreferences =
            this@TransactionDetailsActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()



        txtTxnInstallmentprice = findViewById(R.id.txtTxnInstallmentprice)
        txtTxnRemainingAmt = findViewById(R.id.txtTxnRemainingAmt)
        txtTxnId = findViewById(R.id.txtTxnId)
        txtTxnBankDetails = findViewById(R.id.txtTxnBankDetails)
        txtTxnpaymentMethod = findViewById(R.id.txtTxnpaymentMethod)
        imgGoldTransactionDetails = findViewById(R.id.imgGoldTransactionDetails)
        txtReciptNo = findViewById(R.id.txtReciptNo)
        txtDate = findViewById(R.id.txtDate)
        txtName = findViewById(R.id.txtName)
        txtCrn = findViewById(R.id.txtCrn)
        txtGoldAccount = findViewById(R.id.txtGoldAccount)
        txtNextDueDate = findViewById(R.id.txtNextDueDate)

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
        if (intent.hasExtra("installment")) {
            val intent = intent
            val installment = intent.getStringExtra("installment")
            val remainingamount = intent.getStringExtra("remainingamt")
            val peroid = intent.getStringExtra("period")
            val txnId = intent.getStringExtra("txnid")
            val bankdetail = intent.getStringExtra("bankdetail")
            val paymentMethod = intent.getStringExtra("paymentmethod")
            val chequeno = intent.getStringExtra("chequeno")
            val adminstatus = intent.getStringExtra("adminstatus")
            recipt_no = intent.getStringExtra("recipt_no")
            val date = intent.getStringExtra("date")
            val gold_id = intent.getStringExtra("gold_id")
            val next_due_date = intent.getStringExtra("next_due_date")
            txtReciptNo!!.text = "Reciept $recipt_no"
            txtDate!!.text = date
            txtName!!.text = VGoldApp.onGetFirst() + " " + VGoldApp.onGetLast()
            txtCrn!!.text = userId
            txtTxnInstallmentprice!!.text = installment
            txtTxnRemainingAmt!!.text = remainingamount
            // txtTxnPeriod.setText(peroid);
            txtTxnId!!.text = txnId
            txtTxnBankDetails!!.text = bankdetail
            txtTxnpaymentMethod!!.text = paymentMethod
            txtGoldAccount!!.text = gold_id
            txtNextDueDate!!.text = next_due_date
            // txtTxnchequeNo.setText(chequeno);
            // txtTxnAdminStatus.setText(adminstatus);
        }
        progressDialog = TransparentProgressDialog(this@TransactionDetailsActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        loginStatusServiceProvider = LoginStatusServiceProvider(this)
        checkLoginSession()
        imgGoldTransactionDetails?.setOnClickListener {
            onClickOfImgTrDetails()
        }
    }


    private fun checkLoginSession() {
        loginStatusServiceProvider!!.getLoginStatus(userId, object : APICallback {
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
                                this@TransactionDetailsActivity,
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
                            this@TransactionDetailsActivity, "Alert", message,
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
                            this@TransactionDetailsActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@TransactionDetailsActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@TransactionDetailsActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    @OnClick(R.id.imgGoldTransactionDetails)

    fun onClickOfImgTrDetails() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.vgold.co.in/dashboard/user/module/goldbooking/mmd_receipt.php?id=" + recipt_no + "&&user_id=" + userId)
        )
        startActivity(browserIntent)
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@TransactionDetailsActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

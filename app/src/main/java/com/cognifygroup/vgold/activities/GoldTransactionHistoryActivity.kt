package com.cognifygroup.vgold.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.GetGoldTransactionHistory.GetGoldTransactionHistoryModel
import com.cognifygroup.vgold.GetGoldTransactionHistory.GetGoldTransactionHistoryServiceProvider
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.adapters.GoldTransactionHistoryAdapter
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class GoldTransactionHistoryActivity : AppCompatActivity(),AlertDialogOkListener {

    var mAlert: AlertDialogs? = null
    var getGoldTransactionHistoryServiceProvider: GetGoldTransactionHistoryServiceProvider? = null

    var rvGoldTransactioHistory: RecyclerView? = null
    var booking_id: String? = null
    var tb_goldTransactionHistory: Toolbar? = null
    var imgGoldTransactionHistory: ImageView? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold_transaction_history)


        rvGoldTransactioHistory = findViewById(R.id.rvGoldTransactioHistory)
        tb_goldTransactionHistory = findViewById(R.id.tb_goldTransactionHistory)
        imgGoldTransactionHistory = findViewById(R.id.imgGoldTransactionHistory)

        setSupportActionBar(tb_goldTransactionHistory)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        tb_goldTransactionHistory!!.contentInsetStartWithNavigation = 0
        init()

        imgGoldTransactionHistory!!.setOnClickListener {
            onClickOfImgTrHistory()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun init() {
        progressDialog = TransparentProgressDialog(this@GoldTransactionHistoryActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        getGoldTransactionHistoryServiceProvider = GetGoldTransactionHistoryServiceProvider()
        val intent = intent
        booking_id = intent.getStringExtra("GOLD_BOOKING_ID")
        loginStatusServiceProvider = LoginStatusServiceProvider(this)
       // checkLoginSession()
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
                                this@GoldTransactionHistoryActivity,
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
                            this@GoldTransactionHistoryActivity, "Alert", message,
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
                            this@GoldTransactionHistoryActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@GoldTransactionHistoryActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@GoldTransactionHistoryActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        AttemptToGetGoldTransactionHistory(booking_id!!)
    }

    fun onClickOfImgTrHistory() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.vgold.co.in/dashboard/user/module/goldbooking/transaction_pdf.php?bid=" + booking_id + "&&user_id=" + VGoldApp.onGetUerId())
        )
        startActivity(browserIntent)
    }


    private fun AttemptToGetGoldTransactionHistory(gold_booking_id: String) {
        progressDialog!!.show()
        getGoldTransactionHistoryServiceProvider?.getGoldTransactionHistory(
            gold_booking_id,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? =
                            (serviceResponse as GetGoldTransactionHistoryModel).status
                        val message: String? =
                            (serviceResponse as GetGoldTransactionHistoryModel).message
                        val mArrGoldBookingHistory: ArrayList<GetGoldTransactionHistoryModel.Data>? =
                            (serviceResponse as GetGoldTransactionHistoryModel).data
                        if (status == "200") {
                            rvGoldTransactioHistory!!.layoutManager =
                                LinearLayoutManager(this@GoldTransactionHistoryActivity)
                            rvGoldTransactioHistory!!.adapter = mArrGoldBookingHistory?.let {
                                GoldTransactionHistoryAdapter(
                                    this@GoldTransactionHistoryActivity,
                                    it,
                                    booking_id!!
                                )
                            }

                            //  mAlert.onShowToastNotification(GoldTransactionHistoryActivity.this, message);
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@GoldTransactionHistoryActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                            //                        mAlert.onShowToastNotification(GoldTransactionHistoryActivity.this, message);
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
                                this@GoldTransactionHistoryActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@GoldTransactionHistoryActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@GoldTransactionHistoryActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent =
                    Intent(this@GoldTransactionHistoryActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

package com.cognifygroup.vgold.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.adapters.GoldTransactionAdapter
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.*
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat


class GoldWalletActivity : AppCompatActivity(), AlertDialogOkListener {
//    private var userId = ""
//    private val GetAmount: String? = null
//    private lateinit var progressDialog: TransparentProgressDialog
//
//    private var alertDialogOkListener: AlertDialogOkListener = this
//
//    private lateinit var loginStatusServiceProvider: LoginStatusServiceProvider
//    private lateinit var getAllTransactionGoldServiceProvider: GetAllTransactionGoldServiceProvider
//    private lateinit var getTodayGoldSellRateServiceProvider: GetTodayGoldSellRateServiceProvider
//    private lateinit var getTodayGoldRateServiceProvider: GetTodayGoldRateServiceProvider
//
//    var mAlert: AlertDialogs? = null
//    private lateinit var txtWalletGoldWeight: TextView
//    private lateinit var txSaleAmt: TextView
//    private lateinit var btnAddGoldToWallet: Button
//    private lateinit var  recyclerViewGoldWallet: RecyclerView
//    private lateinit var sharedPreferences: SharedPreferences


    var txtWalletGoldWeight: TextView? = null

    var txSaleAmt: TextView? = null
    var btnAddGoldToWallet: Button? = null

    /*  @InjectView(R.id.btnSell)
      Button btnSell;*/
    var recyclerViewGoldWallet: RecyclerView? = null
    var dialog: Dialog? = null
    var mAlert: AlertDialogs? = null
    var getAllTransactionGoldServiceProvider: GetAllTransactionGoldServiceProvider? = null
    var getTodayGoldSellRateServiceProvider: GetTodayGoldSellRateServiceProvider? = null
    var getTodayGoldRateServiceProvider: GetTodayGoldRateServiceProvider? = null

    private val GetAmount: String? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold_wallet)
        supportActionBar?.title = "Gold Wallet "

        txtWalletGoldWeight = findViewById(R.id.txtWalletGoldWeight)
        recyclerViewGoldWallet = findViewById(R.id.recyclerViewGoldWallet)
        btnAddGoldToWallet = findViewById(R.id.btnAddGoldToWallet)
        txSaleAmt = findViewById(R.id.txSaleAmt)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()

    }

    fun init() {
        progressDialog = TransparentProgressDialog(this@GoldWalletActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        getAllTransactionGoldServiceProvider = GetAllTransactionGoldServiceProvider(this)
        getTodayGoldSellRateServiceProvider = GetTodayGoldSellRateServiceProvider(this)
        getTodayGoldRateServiceProvider = GetTodayGoldRateServiceProvider(this)
        loginStatusServiceProvider = LoginStatusServiceProvider(this)
        //  checkLoginSession()

        btnAddGoldToWallet!!.setOnClickListener {
            AddMoney()
        }
    }

    private fun checkLoginSession() {
        loginStatusServiceProvider!!.getLoginStatus(VGoldApp.onGetUerId(), object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    progressDialog!!.hide()
                    val status = (serviceResponse as LoginSessionModel).getStatus()
                    val message = (serviceResponse as LoginSessionModel).getMessage()
                    val data = (serviceResponse as LoginSessionModel).getData()
                    Log.i("TAG", "onSuccess: $status")
                    Log.i("TAG", "onSuccess: $message")
                    if (status == "200") {
                        if (!data!!) {
                            AlertDialogs().alertDialogOk(
                                this@GoldWalletActivity,
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
                            this@GoldWalletActivity, "Alert", message,
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
                            this@GoldWalletActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*  @OnClick(R.id.btnSell)
    public void BtnSell() {
        startActivity(new Intent(GoldWalletActivity.this, SellGoldActivity.class));
    }*/


    /*  @OnClick(R.id.btnSell)
    public void BtnSell() {
        startActivity(new Intent(GoldWalletActivity.this, SellGoldActivity.class));
    }*/
    fun AddMoney() {
        startActivity(Intent(this@GoldWalletActivity, AddGoldActivity::class.java))
    }


    override fun onResume() {
        super.onResume()
        AttemptToGetGoldTransactionHistory(VGoldApp.onGetUerId())
    }


    private fun AttemptToGetGoldTransactionHistory(user_id: String?) {
//        progressDialog!!.show()
        /*    getAllTransactionGoldServiceProvider!!.getAllTransactionGoldHistory(
                user_id,
                object : APICallback {
                    override fun <T> onSuccess(serviceResponse: T) {
                        try {
                            val status: String? =
                                (serviceResponse as GetAllTransactionGoldModel).status
                            val message: String? =
                                (serviceResponse as GetAllTransactionGoldModel).message
                            val balance: String? =
                                (serviceResponse as GetAllTransactionGoldModel).gold_Balance
                            var gold = balance?.toDouble()
                            val numberFormat = DecimalFormat("#.000")
                            gold = numberFormat.format(gold).toDouble()
                            txtWalletGoldWeight!!.text = "$gold GM"
                            val mArrGoldTransactonHistory: ArrayList<GetAllTransactionGoldModel.Data>? =
                                (serviceResponse as GetAllTransactionGoldModel).data
                            if (status == "200") {
                                recyclerViewGoldWallet!!.layoutManager =
                                    LinearLayoutManager(this@GoldWalletActivity)
                                recyclerViewGoldWallet!!.adapter =
                                    GoldTransactionAdapter(
                                        this@GoldWalletActivity,
                                        mArrGoldTransactonHistory!!
                                    )
                                AttemptToGetTodayGoldRate(gold)

                                //  mAlert.onShowToastNotification(GoldWalletActivity.this, message);
                            } else {
                                AlertDialogs().alertDialogOk(
                                    this@GoldWalletActivity,
                                    "Alert",
                                    message,
                                    resources.getString(R.string.btn_ok),
                                    0,
                                    false,
                                    alertDialogOkListener
                                )
                                //                        mAlert.onShowToastNotification(GoldWalletActivity.this, message);
                                // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(MoneyWalletActivity.this));
                                //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(MoneyWalletActivity.this,mArrGoldBookingHistory));
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
                                    this@GoldWalletActivity,
                                    (apiErrorModel as BaseServiceResponseModel).message
                                )
                            } else {
                                PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
                        } finally {
                            progressDialog!!.hide()
                        }
                    }
                })*/


        // change in api calling


        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", VGoldApp.onGetUerId())
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/gold_wallet_transactions.php")
            //            .header("AUTHORIZATION", "Bearer $token")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                e.printStackTrace()
                Log.e("failure Response", mMessage)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val responce = response.body()!!.string()
                val json = JSONObject(responce)
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    try {
                        val jsonString: String = responce //http request
                        var dataValue = GetAllTransactionGoldModel()
                        val gson = Gson()
                        dataValue =
                            gson.fromJson(jsonString, GetAllTransactionGoldModel::class.java)
                        val status: String? =
                            json.getString("status")
                        val message: String? =
                            json.getString("Message")
                        val balance: String? =
                            dataValue.gold_Balance
                        var gold = balance?.toDouble()
                        val numberFormat = DecimalFormat("#.000")
                        gold = numberFormat.format(gold).toDouble()
                        txtWalletGoldWeight!!.text = "$gold GM"
                        val mArrGoldTransactonHistory: ArrayList<GetAllTransactionGoldModel.Data>? =
                            dataValue.data

                        Log.i("TAG", "onResponse: " + mArrGoldTransactonHistory)
                        if (status == "200") {
                            AttemptToGetTodayGoldRate(gold)



                            runOnUiThread {
                                // Stuff that updates the UI

                                recyclerViewGoldWallet!!.layoutManager =
                                    LinearLayoutManager(this@GoldWalletActivity)
                                recyclerViewGoldWallet!!.adapter =
                                    GoldTransactionAdapter(
                                        this@GoldWalletActivity,
                                        mArrGoldTransactonHistory!!
                                    )
                            }
                            //  mAlert.onShowToastNotification(GoldWalletActivity.this, message);
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@GoldWalletActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                            //                        mAlert.onShowToastNotification(GoldWalletActivity.this, message);
                            // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(MoneyWalletActivity.this));
                            //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(MoneyWalletActivity.this,mArrGoldBookingHistory));
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            }
        })


    }


    private fun AttemptToGetTodayGoldRate(gold: Double) {
        // mAlert.onShowProgressDialog(SellGoldActivity.this, true);
        /*       getTodayGoldRateServiceProvider!!.getTodayGoldRate(VGoldApp.onGetUerId(),
                   object : APICallback {
                       override fun <T> onSuccess(serviceResponse: T) {
                           try {
                               val status: String? = (serviceResponse as GetTodayGoldRateModel).status
                               val message: String? = (serviceResponse as GetTodayGoldRateModel).message
                               val todayGoldPurchaseRate: String? =
                                   (serviceResponse as GetTodayGoldRateModel).gold_purchase_rate
                               val sellingRate = gold * todayGoldPurchaseRate!!.toDouble()
                               val amt = DecimalFormat("##.##").format(sellingRate)
                               txSaleAmt!!.text = "₹ $amt"

                               if (status == "200") {
                                   //  mAlert.onShowToastNotification(SellGoldActivity.this, message);
                               } else {
                                   AlertDialogs().alertDialogOk(
                                       this@GoldWalletActivity,
                                       "Alert",
                                       message,
                                       resources.getString(R.string.btn_ok),
                                       0,
                                       false,
                                       alertDialogOkListener
                                   )
                                   //                        mAlert.onShowToastNotification(SellGoldActivity.this, message);
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
                                       this@GoldWalletActivity,
                                       (apiErrorModel as BaseServiceResponseModel).message
                                   )
                               } else {
                                   PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
                               }
                           } catch (e: Exception) {
                               e.printStackTrace()
                               PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
                           } finally {
                               progressDialog!!.hide()
                           }
                       }
                   })*/


        // change api calling


        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", VGoldApp.onGetUerId())
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_purchase_rate.php")
//            .header("AUTHORIZATION", "Bearer $token")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                e.printStackTrace()
                Log.e("failure Response", mMessage)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val responce = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    val json = JSONObject(responce)
                    val status = json.get("status").toString()

                    val jsonString: String = responce //http request
                    var dataValue = GetTodayGoldRateModel()
                    val gson = Gson()
                    dataValue =
                        gson.fromJson(jsonString, GetTodayGoldRateModel::class.java)

                    if (status == "200") {
                        Log.e(" Response", responce)
                        val sale_value: String? = dataValue.gold_sale_rate;
                        val sellingRate: Double = gold * sale_value!!.toDouble()
                        val amt = DecimalFormat("##.##").format(sellingRate)
                        txSaleAmt!!.text = "₹ $amt"

                    } else {
                        /* PrintUtil.showToast(
                             this@SellGoldActivity,
                             json.getString("Message").toString()
                         )*/
                    }
                }
            }
        })


    }


    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@GoldWalletActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }

}


//        sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
//
//        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        txtWalletGoldWeight = findViewById(R.id.txtWalletGoldWeight)
//        recyclerViewGoldWallet = findViewById(R.id.recyclerViewGoldWallet)
//        btnAddGoldToWallet = findViewById(R.id.btnAddGoldToWallet)
//        txSaleAmt = findViewById(R.id.txSaleAmt)
//
//        init1()
//
//        btnAddGoldToWallet.setOnClickListener {
//            startActivity(Intent(this@GoldWalletActivity, AddGoldActivity::class.java))
//
//        }
//
//    }
//
//    fun init1() {
//        progressDialog = TransparentProgressDialog(this)
//        progressDialog.setCancelable(false)
//        setFinishOnTouchOutside(false)
//        mAlert = AlertDialogs().getInstance()
//        getAllTransactionGoldServiceProvider = GetAllTransactionGoldServiceProvider(this)
//        getTodayGoldSellRateServiceProvider = GetTodayGoldSellRateServiceProvider(this)
//        getTodayGoldRateServiceProvider = GetTodayGoldRateServiceProvider(this)
//        loginStatusServiceProvider = LoginStatusServiceProvider(this)
//        checkLoginSession()
//    }
//
//    private fun checkLoginSession() {
//        loginStatusServiceProvider.getLoginStatus(userId, object : APICallback {
//            override fun <T> onSuccess(serviceResponse: T) {
//                try {
//                    progressDialog.hide()
//                    val json = JSONObject(serviceResponse.toString())
//                    val gson = Gson()
//                    val loginSessionModel = gson.fromJson(json.toString(), LoginSessionModel::class.java)
//                    val status: String? = loginSessionModel.getStatus()
//                    val message: String? = loginSessionModel.getMessage()
//                    val data: Boolean = loginSessionModel.getData() == true
//                    Log.i("TAG", "onSuccess: $status")
//                    Log.i("TAG", "onSuccess: $message")
//                    if (status == "200") {
//                        if (!data) {
//                            AlertDialogs().alertDialogOk(
//                                this@GoldWalletActivity,
//                                "Alert",
//                                "$message,  Please relogin to app",
//                                resources.getString(R.string.btn_ok),
//                                11,
//                                false,
//                                alertDialogOkListener
//                            )
//                        }
//                    } else {
//                        AlertDialogs().alertDialogOk(
//                            this@GoldWalletActivity, "Alert", message,
//                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
//                        )
//                        //                        mAlert.onShowToastNotification(AddGoldActivity.this, message);
//                    }
//                } catch (e: Exception) {
//                    //  progressDialog.hide();
//                    e.printStackTrace()
//                } finally {
//                    //  progressDialog.hide();
//                }
//            }
//
//           override fun <T> onFailure(apiErrorModel: T, extras: T) {
//                try {
//                    progressDialog.hide()
//                    if (apiErrorModel != null) {
//                        PrintUtil.showToast(
//                            this@GoldWalletActivity,
//                            (apiErrorModel as BaseServiceResponseModel).message
//                        )
//                    } else {
//                        PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
//                    }
//                } catch (e: Exception) {
//                    progressDialog.hide()
//                    e.printStackTrace()
//                    PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
//                } finally {
//                    progressDialog.hide()
//                }
//            }
//        })
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                finish()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    /*  @OnClick(R.id.btnSell)
//    public void BtnSell() {
//        startActivity(new Intent(GoldWalletActivity.this, SellGoldActivity.class));
//    }*/
//
//
//    /*  @OnClick(R.id.btnSell)
//    public void BtnSell() {
//        startActivity(new Intent(GoldWalletActivity.this, SellGoldActivity.class));
//    }*/
//    @OnClick(R.id.btnAddGoldToWallet)
//    fun AddMoney() {
//        startActivity(Intent(this@GoldWalletActivity, AddGoldActivity::class.java))
//
//    }
//
//
//    private fun AttemptToGetGoldTransactionHistory(user_id: String) {
////        progressDialog.show()
//        getAllTransactionGoldServiceProvider.getAllTransactionGoldHistory(
//            user_id,
//            object : APICallback{
//                @SuppressLint("SetTextI18n")
//                override fun <T> onSuccess(serviceResponse: T) {
//                    try {
//                        val status: String? =
//                            (serviceResponse as GetAllTransactionGoldModel).status
//                        val message: String? =
//                            (serviceResponse as GetAllTransactionGoldModel).message
//                        val balance: String? =
//                            (serviceResponse as GetAllTransactionGoldModel).gold_Balance
//                        var gold = balance!!.toDouble()
//                        val numberFormat = DecimalFormat("#.000")
//                        gold = numberFormat.format(gold).toDouble()
//                        txtWalletGoldWeight.text = "$gold GM"
//                        val mArrGoldTransactonHistory: ArrayList<GetAllTransactionGoldModel.Data>? =
//                            (serviceResponse as GetAllTransactionGoldModel).data
//                        if (status == "200") {
//                            recyclerViewGoldWallet.layoutManager =
//                                LinearLayoutManager(this@GoldWalletActivity)
//                            recyclerViewGoldWallet.adapter = GoldTransactionAdapter(
//                                this@GoldWalletActivity,
//                                mArrGoldTransactonHistory!!
//                            )
//                            AttemptToGetTodayGoldRate(gold)
//
//                            //  mAlert.onShowToastNotification(GoldWalletActivity.this, message);
//                        } else {
//                            AlertDialogs().alertDialogOk(
//                                this@GoldWalletActivity,
//                                "Alert",
//                                message,
//                                resources.getString(R.string.btn_ok),
//                                0,
//                                false,
//                                alertDialogOkListener
//                            )
//                            //                        mAlert.onShowToastNotification(GoldWalletActivity.this, message);
//                            // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(MoneyWalletActivity.this));
//                            //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(MoneyWalletActivity.this,mArrGoldBookingHistory));
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    } finally {
//                        progressDialog.hide()
//                    }
//                }
//
//                 override fun <T> onFailure(apiErrorModel: T, extras: T) {
//                    try {
//                        if (apiErrorModel != null) {
//                            PrintUtil.showToast(
//                                this@GoldWalletActivity,
//                                (apiErrorModel as BaseServiceResponseModel).message
//                            )
//                        } else {
//                            PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
//                    } finally {
//                        progressDialog.hide()
//                    }
//                }
//            })
//    }
//
//
//    private fun AttemptToGetTodayGoldRate(gold: Double) {
//        // mAlert.onShowProgressDialog(SellGoldActivity.this, true);
//        getTodayGoldRateServiceProvider.getTodayGoldRate(object : APICallback {
//           override fun <T> onSuccess(serviceResponse: T) {
//                try {
//                    val status: String? = (serviceResponse as GetTodayGoldRateModel).status
//                    val message: String? = (serviceResponse as GetTodayGoldRateModel).message
//                    val todayGoldPurchaseRate: String? =
//                        (serviceResponse as GetTodayGoldRateModel).gold_purchase_rate
//                    val sellingRate = gold * todayGoldPurchaseRate!!.toDouble()
//                    val amt = DecimalFormat("##.##").format(sellingRate)
//                    txSaleAmt.text = "₹ $amt"
//                    if (status == "200") {
//                        //  mAlert.onShowToastNotification(SellGoldActivity.this, message);
//                    } else {
//                        AlertDialogs().alertDialogOk(
//                            this@GoldWalletActivity, "Alert", message,
//                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
//                        )
//                        //                        mAlert.onShowToastNotification(SellGoldActivity.this, message);
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                } finally {
//                    progressDialog!!.hide()
//                }
//            }
//
//         override fun <T> onFailure(apiErrorModel: T, extras: T) {
//                try {
//                    if (apiErrorModel != null) {
//                        PrintUtil.showToast(
//                            this@GoldWalletActivity,
//                            (apiErrorModel as BaseServiceResponseModel).message
//                        )
//                    } else {
//                        PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    PrintUtil.showNetworkAvailableToast(this@GoldWalletActivity)
//                } finally {
//                    progressDialog.hide()
//                }
//            }
//        })
//    }
//
// override fun onDialogOk(resultCode: Int) {
//        when (resultCode) {
//            11 -> {
//                val LogIntent = Intent(this@GoldWalletActivity, LoginActivity::class.java)
//                startActivity(LogIntent)
//                finish()
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        AttemptToGetGoldTransactionHistory(userId)
//    }
//}
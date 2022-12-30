package com.cognifygroup.vgold.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.OnClick
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.adapters.MoneyTransactionAdapter
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class MoneyWalletActivity : AppCompatActivity() {
    private lateinit var txtRupees: TextView
    private lateinit var txSaleAmt: TextView
    private lateinit var btnAddMoneyToWallet: Button
    private lateinit var recyclerViewMoneyWallet: RecyclerView

    var dialog: Dialog? = null
    var mAlert: AlertDialogs? = null
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var getAllTransactionMoneyServiceProvider: GetAllTransactionMoneyServiceProvider

    private val GetAmount: String? = null
    private lateinit var progressDialog: TransparentProgressDialog
    private lateinit var alertDialogOkListener: AlertDialogOkListener
    private lateinit var loginStatusServiceProvider: LoginStatusServiceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_wallet)
        sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Money Wallet "
        txtRupees = findViewById(R.id.txtRupees)
        recyclerViewMoneyWallet = findViewById(R.id.recyclerViewMoneyWallet)
        btnAddMoneyToWallet = findViewById(R.id.btnAddMoneyToWallet)

        init()

        btnAddMoneyToWallet.setOnClickListener {
            startActivity(Intent(this@MoneyWalletActivity, AddMoneyActivity::class.java))

        }
    }


    fun init() {
        progressDialog = TransparentProgressDialog(this@MoneyWalletActivity)
        progressDialog.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        getAllTransactionMoneyServiceProvider = GetAllTransactionMoneyServiceProvider(this)
        //     loginStatusServiceProvider = LoginStatusServiceProvider(this)
        // checkLoginSession()
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
                                this@MoneyWalletActivity,
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
                            this@MoneyWalletActivity, "Alert", message,
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
                            this@MoneyWalletActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@MoneyWalletActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@MoneyWalletActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }
//    private fun checkLoginSession() {
//        loginStatusServiceProvider.getLoginStatus(userId, object : APICallback {
//            override fun <T> onSuccess(serviceResponse: T) {
//                try {
//                    progressDialog.hide()
//                    val json = JSONObject(serviceResponse.toString())
//                    val gson = Gson()
//                    val loginSessionModel = gson.fromJson(json.toString(), LoginSessionModel::class.java)
//
//                    val status: String? = loginSessionModel.getStatus()
//                    val message: String? = loginSessionModel.getMessage()
//                    val data: Boolean = loginSessionModel.getData() == true
//                    Log.i("TAG", "onSuccess: $status")
//                    Log.i("TAG", "onSuccess: $message")
//                    if (status == "200") {
//                        if (!data) {
//                            AlertDialogs().alertDialogOk(
//                                this@MoneyWalletActivity,
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
//                            this@MoneyWalletActivity, "Alert", message,
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
//            override fun <T> onFailure(apiErrorModel: T, extras: T) {
//                try {
//                    progressDialog.hide()
//                    if (apiErrorModel != null) {
//                        PrintUtil.showToast(
//                            this@MoneyWalletActivity,
//                            (apiErrorModel as BaseServiceResponseModel).message
//                        )
//                    } else {
//                        PrintUtil.showNetworkAvailableToast(this@MoneyWalletActivity)
//                    }
//                } catch (e: Exception) {
//                    progressDialog.hide()
//                    e.printStackTrace()
//                    PrintUtil.showNetworkAvailableToast(this@MoneyWalletActivity)
//                } finally {
//                    progressDialog.hide()
//                }
//            }
//        })
//    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @OnClick(R.id.btnAddMoneyToWallet)
    fun AddMoney() {
        startActivity(Intent(this@MoneyWalletActivity, AddMoneyActivity::class.java))


        /*dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_for_wallet_money);
        dialog.show();

        final EditText edtAmount = (EditText) dialog.findViewById(R.id.edtAmount);
        final Button textViewCancel = (Button) dialog.findViewById(R.id.textViewCancel);
        final Button textViewOk = (Button) dialog.findViewById(R.id.textViewOk);

        dialog.setCancelable(false);

        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAmount.getText().length() < 1) {
                    edtAmount.setError("Enter amount");
                } else {
                    GetAmount = edtAmount.getText().toString();
                    Intent intent=new Intent(MoneyWalletActivity.this,AddPaymentToMoneyWalletActivity.class);
                    intent.putExtra("AMOUNT",GetAmount);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    override fun onResume() {
        super.onResume()
        AttemptToGetMoneyTransactionHistory(userId)
    }

    private fun AttemptToGetMoneyTransactionHistory(user_id: String?) {
//        progressDialog.show()
        /* getAllTransactionMoneyServiceProvider.getAllTransactionMoneyHistory(
             user_id,
             object : APICallback {
                 override fun <T> onSuccess(serviceResponse: T) {
                     try {
                         val status: String? =
                             (serviceResponse as GetAllTransactionMoneyModel).status
                         val message: String? =
                             (serviceResponse as GetAllTransactionMoneyModel).message
                         val balance: String? =
                             (serviceResponse as GetAllTransactionMoneyModel).wallet_Balance
                         txtRupees.text = balance
                         val mArrMoneyTransactonHistory: ArrayList<GetAllTransactionMoneyModel.Data>? =
                             (serviceResponse as GetAllTransactionMoneyModel).data
                         if (status == "200") {
                             recyclerViewMoneyWallet.layoutManager =
                                 LinearLayoutManager(this@MoneyWalletActivity)
                             recyclerViewMoneyWallet.adapter = mArrMoneyTransactonHistory?.let {
                                 MoneyTransactionAdapter(
                                     this@MoneyWalletActivity,
                                     it
                                 )
                             }

                             //   mAlert.onShowToastNotification(MoneyWalletActivity.this, message);
                         } else {
                             AlertDialogs().alertDialogOk(
                                 this@MoneyWalletActivity,
                                 "Alert",
                                 message,
                                 resources.getString(R.string.btn_ok),
                                 0,
                                 false,
                                 alertDialogOkListener
                             )
                             //                        mAlert.onShowToastNotification(MoneyWalletActivity.this, message);
                             // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(MoneyWalletActivity.this));
                             //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(MoneyWalletActivity.this,mArrGoldBookingHistory));
                         }
                     } catch (e: Exception) {
                         e.printStackTrace()
                     } finally {
                         progressDialog.hide()
                     }
                 }

                 override fun <T> onFailure(apiErrorModel: T, extras: T) {
                     try {
                         if (apiErrorModel != null) {
                             PrintUtil.showToast(
                                 this@MoneyWalletActivity,
                                 (apiErrorModel as BaseServiceResponseModel).message
                             )
                         } else {
                             PrintUtil.showNetworkAvailableToast(this@MoneyWalletActivity)
                         }
                     } catch (e: Exception) {
                         e.printStackTrace()
                         PrintUtil.showNetworkAvailableToast(this@MoneyWalletActivity)
                     } finally {
                         progressDialog.hide()
                     }
                 }
             })*/


        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", VGoldApp.onGetUerId())
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/money_wallet_transactions.php")
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
                val resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    val json = JSONObject(resp)
                    val status = json.get("status").toString()
                    if (status == "200") {
                        Log.e(" Response", resp)
                        if (json.getString("Message").toString().equals("Success")) {

                            val jsonString: String = resp //http request
                            var dataValue = GetAllTransactionMoneyModel()
                            val gson = Gson()
                            dataValue =
                                gson.fromJson(jsonString, GetAllTransactionMoneyModel::class.java)


                            val mArrMoneyTransactonHistory: ArrayList<GetAllTransactionMoneyModel.Data>? =
                                dataValue.data


                            runOnUiThread {
                                // Stuff that updates the UI

                                txtRupees.text = dataValue.wallet_Balance
                                recyclerViewMoneyWallet.layoutManager =
                                    LinearLayoutManager(this@MoneyWalletActivity)
                                recyclerViewMoneyWallet.adapter = mArrMoneyTransactonHistory?.let {
                                    MoneyTransactionAdapter(
                                        this@MoneyWalletActivity,
                                        it
                                    )
                                }
                            }


                        }
                    } else {
                        PrintUtil.showToast(
                            this@MoneyWalletActivity,
                            json.getString("Message").toString()
                        )
                    }
                }
            }
        })

    }

    fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@MoneyWalletActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

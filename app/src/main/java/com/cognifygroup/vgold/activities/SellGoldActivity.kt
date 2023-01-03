package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.*
import com.cognifygroup.vgold.sellGold.SellGoldServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat

class SellGoldActivity : AppCompatActivity(), AlertDialogOkListener {
    var edtGoldWeight: EditText? = null
    var txtSellAmount: TextView? = null
    var btnSell: TextView? = null
    var txtWalletBalence: TextView? = null
    var txtGoldRate: TextView? = null

    var result = 0.0
    var amount = 0.0
    var weight = 0.0
    var enterbalence = 0.0
    var TodayGoldSellRate: String? = null
    var TodayGoldSellRateValue: String? = null
    var balance: String? = null
    private var goldWeight: String? = null
    private var amt: kotlin.String? = null

    var mAlert: AlertDialogs? = null

    var sellGoldServiceProvider: SellGoldServiceProvider? = null

    //  var getBankServiceProvider: GetBankServiceProvider? = null
    // var getAllTransactionGoldServiceProvider: GetAllTransactionGoldServiceProvider? = null
    //  var getTodayGoldSellRateServiceProvider: GetTodayGoldSellRateServiceProvider? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    // private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_gold)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.title = "Sell Gold"


        sharedPreferences =
            this@SellGoldActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()



        edtGoldWeight = findViewById(R.id.edtGoldWeight)
        txtSellAmount = findViewById(R.id.txtSellAmount)
        btnSell = findViewById(R.id.btnSell)
        txtWalletBalence = findViewById(R.id.txtWalletBalence)
        txtGoldRate = findViewById(R.id.txtGoldRate)

        progressDialog = TransparentProgressDialog(this@SellGoldActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        mAlert = AlertDialogs().getInstance()

        // sellGoldServiceProvider = SellGoldServiceProvider(this)
        // getBankServiceProvider = GetBankServiceProvider()
        //  getAllTransactionGoldServiceProvider = GetAllTransactionGoldServiceProvider(this)
        //   getTodayGoldSellRateServiceProvider = GetTodayGoldSellRateServiceProvider(this)
        //  loginStatusServiceProvider = LoginStatusServiceProvider(this)
        //   checkLoginSession()

        AttemptToGetTodayGoldRateOld()


        AttemptToGetGoldTransactionHistory(userId)

        btnSell!!.setOnClickListener {
            onClickOfBtnbtnSell()
        }


        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //edtGoldWeight.addTextChangedListener(null);

            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable) {
                //edtGoldWeight.addTextChangedListener(null);
                if (s.length != 0 && s.toString() != ".") {
                    weight = edtGoldWeight!!.text.toString().toDouble()
                    result = weight * TodayGoldSellRate!!.toDouble()
                    if (result != 0.00) {
                        txtSellAmount!!.text = "" + DecimalFormat("##.##").format(result) + " ₹"
                    }
                } else {
                    txtSellAmount!!.text = ""
                }
            }
        }

        edtGoldWeight!!.addTextChangedListener(textWatcher)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    fun onClickOfBtnbtnSell() {
        val walletbalence = balance!!.toDouble()
        if (edtGoldWeight!!.text.toString() != "") {
            enterbalence = edtGoldWeight!!.text.toString().toDouble()


//            AttemptToSellGold(VGoldApp.onGetUerId(), edtGoldWeight.getText().toString(), "" + result);
            if (enterbalence != 0.0 && enterbalence >= 1.0) {
                if (enterbalence <= walletbalence) {
                    goldWeight = edtGoldWeight!!.text.toString()
                    amt = "" + result
                    getOtp(userId)

//                    AttemptToSellGold(VGoldApp.onGetUerId(), edtGoldWeight.getText().toString(), "" + result);
                } else {
                    AlertDialogs().alertDialogOk(
                        this@SellGoldActivity, "Alert", "Insufficient wallet balance",
                        resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                    )
                }
            } else {
                AlertDialogs().alertDialogOk(
                    this@SellGoldActivity, "Alert", "Amount should be grater than 1 gm to sell",
                    resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                )
            }
        } else {
            AlertDialogs().alertDialogOk(
                this@SellGoldActivity, "Alert", "Please enter amount",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
        }
    }

    private fun getOtp(user_id: String?) {
        //     progressDialog!!.show()
/*        sellGoldServiceProvider?.getOTP(user_id, "send_otp", object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                val Status: String? = (serviceResponse as SellGoldModel).getStatus()
                val message: String? = (serviceResponse as SellGoldModel).getMessage()
                try {
                    if (Status == "200") {

                        //                        AlertDialogs.alertDialogOk(SellGoldActivity.this, "Alert", "Otp sent to your register mobile no and mail",
                        //                                getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);
                        startActivity(
                            Intent(this@SellGoldActivity, OtpVerificationActivity::class.java)
                                .putExtra("moveFrom", "SellGold")
                                .putExtra("Weight", goldWeight)
                                .putExtra("AMOUNT", amt)
                        )


                        *//*startActivity(new Intent(PayActivity.this, Otp1Activity.class)
                                    .putExtra("OTP", otp)
                                    .putExtra("AMOUNT", "" + result)
                                    .putExtra("Weight", weight)
                                    .putExtra("NO", mobNo));*//*
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@SellGoldActivity, "Alert", message,
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
                            this@SellGoldActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@SellGoldActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@SellGoldActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })*/


        // change in api calling

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("action", "send_otp")
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/sale_gold_verifyotp.php")
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
                val mMessage = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(mMessage)
                    var status = json.get("status").toString()
                    var msg = json.get("Message").toString()
                    try {
                        runOnUiThread {
                            if (status == "200") {
                                startActivity(
                                    Intent(
                                        this@SellGoldActivity,
                                        OtpVerificationActivity::class.java
                                    )
                                        .putExtra("moveFrom", "SellGold")
                                        .putExtra("Weight", goldWeight)
                                        .putExtra("AMOUNT", amt)
                                )
                            } else {
                                AlertDialogs().alertDialogOk(
                                    this@SellGoldActivity, "Alert",
                                    msg,
                                    resources.getString(R.string.btn_ok),
                                    0,
                                    false,
                                    alertDialogOkListener
                                )
                            }
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

    private fun AttemptToSellGold(user_id: String, gold: String, amount: String) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        /* sellGoldServiceProvider?.getAddBankDetails(user_id, gold, amount, object : APICallback {
             override fun <T> onSuccess(serviceResponse: T) {
                 try {
                     val status: String? = (serviceResponse as SellGoldModel).getStatus()
                     val message: String? = (serviceResponse as SellGoldModel).getMessage()
                     if (status == "200") {

                         //  mAlert.onShowToastNotification(SellGoldActivity.this, message);
                         val intent = Intent(this@SellGoldActivity, SuccessActivity::class.java)
                         intent.putExtra("message", message)
                         startActivity(intent)
                         finish()
                     } else {
                         AlertDialogs().alertDialogOk(
                             this@SellGoldActivity, "Alert", message,
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
                             this@SellGoldActivity,
                             (apiErrorModel as BaseServiceResponseModel).message
                         )
                     } else {
                         PrintUtil.showNetworkAvailableToast(this@SellGoldActivity)
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     PrintUtil.showNetworkAvailableToast(this@SellGoldActivity)
                 } finally {
                     progressDialog!!.hide()
                 }
             }
         })*/
    }


    private fun AttemptToGetGoldTransactionHistory(user_id: String?) {
        // mAlert.onShowProgressDialog(SellGoldActivity.this, true);
        /*       getAllTransactionGoldServiceProvider?.getAllTransactionGoldHistory(
                   user_id,
                   object : APICallback {
                       override fun <T> onSuccess(serviceResponse: T) {
                           try {
                               val status: String? =
                                   (serviceResponse as GetAllTransactionGoldModel).status
                               val message: String? =
                                   (serviceResponse as GetAllTransactionGoldModel).message
                               balance = (serviceResponse as GetAllTransactionGoldModel).gold_Balance
                               txtWalletBalence!!.text = balance + "GM"
                               val mArrGoldTransactonHistory: ArrayList<GetAllTransactionGoldModel.Data>? =
                                   (serviceResponse as GetAllTransactionGoldModel).data
                               if (status == "200") {
                                   // recyclerViewGoldWallet.setLayoutManager(new LinearLayoutManager(SellGoldActivity.this));
                                   //recyclerViewGoldWallet.setAdapter(new GoldTransactionAdapter(SellGoldActivity.this,mArrGoldTransactonHistory));

                                   //  mAlert.onShowToastNotification(SellGoldActivity.this, message);
                               } else {
                                   AlertDialogs().alertDialogOk(
                                       this@SellGoldActivity,
                                       "Alert",
                                       message,
                                       resources.getString(R.string.btn_ok),
                                       0,
                                       false,
                                       alertDialogOkListener
                                   )
                                   //                        mAlert.onShowToastNotification(SellGoldActivity.this, message);
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
                                       this@SellGoldActivity,
                                       (apiErrorModel as BaseServiceResponseModel).message
                                   )
                               } else {
                                   PrintUtil.showNetworkAvailableToast(this@SellGoldActivity)
                               }
                           } catch (e: Exception) {
                               e.printStackTrace()
                               PrintUtil.showNetworkAvailableToast(this@SellGoldActivity)
                           } finally {
                               progressDialog?.hide()
                           }
                       }
                   })*/


        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
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

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: okhttp3.Response) {
                var mMessage = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    runOnUiThread {
                        var json = JSONObject(mMessage)
                        var status = json.get("status").toString()
                        if (status == "200") {
                            if (json.getString("Message").toString().equals("Success")) {
                                var gold_Balance = json.optString("gold_Balance").toString()
                                balance = gold_Balance
                                txtWalletBalence!!.text = balance + "GM"
                            }
                        } else {
                            PrintUtil.showToast(
                                this@SellGoldActivity,
                                json.getString("Message").toString()
                            )
                        }
                    }
                }
            }
        })
    }


    private fun AttemptToGetTodayGoldRateOld() {
        // mAlert.onShowProgressDialog(SellGoldActivity.this, true);
        /* getTodayGoldSellRateServiceProvider?.getTodayGoldRate(object : APICallback {
             override fun <T> onSuccess(serviceResponse: T) {
                 try {
                     val status: String? = (serviceResponse as GetTodayGoldSellModel).status
                     val message: String? = (serviceResponse as GetTodayGoldSellModel).message
                     TodayGoldSellRate =
                         (serviceResponse as GetTodayGoldSellModel).gold_sale_rate
                     txtGoldRate!!.text = "₹ $TodayGoldSellRate/GM"
                     if (status == "200") {
                         //  mAlert.onShowToastNotification(SellGoldActivity.this, message);
                     } else {
                         AlertDialogs().alertDialogOk(
                             this@SellGoldActivity, "Alert", message,
                             resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                         )
                         //                        mAlert.onShowToastNotification(SellGoldActivity.this, message);
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
                             this@SellGoldActivity,
                             (apiErrorModel as BaseServiceResponseModel).message
                         )
                     } else {
                         PrintUtil.showNetworkAvailableToast(this@SellGoldActivity)
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     PrintUtil.showNetworkAvailableToast(this@SellGoldActivity)
                 } finally {
                     progressDialog?.hide()
                 }
             }
         })*/


        // change in api calling

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_sale_rate.php")
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

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val mMessage = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(mMessage)
                    var status = json.get("status").toString()
                    runOnUiThread {
                        if (status == "200") {
                            Log.e(" Response", mMessage)
                            TodayGoldSellRate = json.optString("Gold_sale_rate").toString()
                            txtGoldRate!!.text = "₹ $TodayGoldSellRate/GM"
                        } else {
                            PrintUtil.showToast(
                                this@SellGoldActivity,
                                json.getString("Message").toString()
                            )
                        }
                    }
                }
            }
        })

    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            1 ->                 /*startActivity(new Intent(SellGoldActivity.this, Otp1Activity.class)
                        .putExtra("moveFrom", "SellGold")
                        .putExtra("Weight", goldWeight)
                        .putExtra("AMOUNT", amt));*/startActivity(
                Intent(
                    this@SellGoldActivity,
                    OtpVerificationActivity::class.java
                )
                    .putExtra("moveFrom", "SellGold")
                    .putExtra("Weight", goldWeight)
                    .putExtra("AMOUNT", amt)
            )
            11 -> {
                val LogIntent = Intent(this@SellGoldActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

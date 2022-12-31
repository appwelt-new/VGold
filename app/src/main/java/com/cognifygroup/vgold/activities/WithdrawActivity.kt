package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.getbankdetails.GetBankModel
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class WithdrawActivity : AppCompatActivity(), AlertDialogOkListener {
    var mAlert: AlertDialogs? = null
    // var getBankServiceProvider: GetBankServiceProvider? = null
    // var withdrawMoneyServiceProvider: WithdrawMoneyServiceProvider? = null
    // var getAllTransactionMoneyServiceProvider: GetAllTransactionMoneyServiceProvider? = null


    var spinnerBank: Spinner? = null
    var edtWithdrawAmount: EditText? = null

    var edtComment: EditText? = null

    var btnWithdrawMoney: Button? = null
    var txtWalletBalence: TextView? = null

    var BankId: String? = null
    var BankName: kotlin.String? = null
    var balance: String? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
   // private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)

        supportActionBar?.title = "Withdraw "

        spinnerBank = findViewById(R.id.spinnerBank)
        edtWithdrawAmount = findViewById(R.id.edtWithdrawAmount)
        edtComment = findViewById(R.id.edtComment)
        btnWithdrawMoney = findViewById(R.id.btnWithdrawMoney)
        txtWalletBalence = findViewById(R.id.txtWalletBalence)


        progressDialog = TransparentProgressDialog(this)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        mAlert = AlertDialogs().getInstance()

        // getBankServiceProvider = GetBankServiceProvider()
        //  withdrawMoneyServiceProvider = WithdrawMoneyServiceProvider(this)
        //  getAllTransactionMoneyServiceProvider = GetAllTransactionMoneyServiceProvider(this)
        // loginStatusServiceProvider = LoginStatusServiceProvider(this)
        //  checkLoginSession()

        AttemptToGetMoneyTransactionHistory(VGoldApp.onGetUerId())
      //  AttemptToBank(VGoldApp.onGetUerId())

        btnWithdrawMoney!!.setOnClickListener {
            onClickOfBtnWithdrawMoney()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClickOfBtnWithdrawMoney() {
        var walletbalence = 0.0
        if (balance != null && !TextUtils.isEmpty(balance)) {
            walletbalence = balance!!.toDouble()
        }
        var withdrawbalence = 0.0
        if (!TextUtils.isEmpty(edtWithdrawAmount!!.text.toString())) {
            withdrawbalence = edtWithdrawAmount!!.text.toString().toDouble()
        }


//        double withdrawbalence = Double.parseDouble(edtWithdrawAmount.getText().toString());
        if (withdrawbalence <= walletbalence && withdrawbalence >= 500) {
            AttemptToWithdrawMoney(
                VGoldApp.onGetUerId(),
                BankId!!, edtWithdrawAmount!!.text.toString(), edtComment!!.text.toString()
            )
        } else {
            AlertDialogs().alertDialogOk(
                this@WithdrawActivity,
                "Alert",
                "Amount should be greater than 500 or equal to or less than wallet balance",
                resources.getString(R.string.btn_ok),
                0,
                false,
                alertDialogOkListener
            )
            //            mAlert.onShowToastNotification(WithdrawActivity.this, "Amount should be greater than 500 or equal to or less than wallet balance");
        }
    }


    private fun AttemptToBank(user_id: String?) {
        // mAlert.onShowProgressDialog(SignUpActivity.this, true);
        /* getBankServiceProvider?.getAddBankDetails(user_id, object : APICallback {
             override fun <T> onSuccess(serviceResponse: T) {
                 try {
                     val status: String? = (serviceResponse as GetBankModel).status
                     val message: String? = (serviceResponse as GetBankModel).message
                     val mArrCity: ArrayList<GetBankModel.Data>? =
                         (serviceResponse as GetBankModel).data
                     if (status == "200") {
                         val adapter: ArrayAdapter<GetBankModel.Data> =
                             ArrayAdapter<GetBankModel.Data>(
                                 this@WithdrawActivity,
                                 R.layout.support_simple_spinner_dropdown_item,
                                 mArrCity!!
                             )
                         adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                         spinnerBank!!.adapter = adapter
                         spinnerBank!!.onItemSelectedListener =
                             object : AdapterView.OnItemSelectedListener {
                                 override fun onItemSelected(
                                     parent: AdapterView<*>?,
                                     view: View,
                                     position: Int,
                                     id: Long
                                 ) {
                                     BankId =
                                         java.lang.String.valueOf(mArrCity?.get(position)?.bank_id)
                                     BankName =
                                         java.lang.String.valueOf(mArrCity?.get(position)?.bank_name)
                                 }

                                 override fun onNothingSelected(parent: AdapterView<*>?) {}
                             }
                     } else {
                         AlertDialogs().alertDialogOk(
                             this@WithdrawActivity, "Alert", message,
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
                             this@WithdrawActivity,
                             (apiErrorModel as BaseServiceResponseModel).message
                         )
                     } else {
                         PrintUtil.showNetworkAvailableToast(this@WithdrawActivity)
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     PrintUtil.showNetworkAvailableToast(this@WithdrawActivity)
                 } finally {
                     progressDialog?.hide()
                 }
             }
         })*/


        // change in api calling
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", user_id)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_bank_details.php")
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
                var resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(resp)
                    var status = json.get("status").toString()
                    var message: String = json.get("Message").toString()

                    if (status == "200") {
                        Log.e(" Response", resp)

                        var jsonString: String = resp //http request
                        var dataValue = GetBankModel()
                        var gson = Gson()
                        dataValue =
                            gson.fromJson(jsonString, GetBankModel::class.java)

                        val message: String? =
                            dataValue.message;
                        runOnUiThread {
                            val mArrCity: ArrayList<GetBankModel.Data>? =
                                dataValue.data
                            val adapter: ArrayAdapter<GetBankModel.Data> =
                                ArrayAdapter<GetBankModel.Data>(
                                    this@WithdrawActivity,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    mArrCity!!
                                )
                            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                            spinnerBank!!.adapter = adapter
                            spinnerBank!!.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View,
                                        position: Int,
                                        id: Long
                                    ) {
                                        BankId =
                                            java.lang.String.valueOf(mArrCity?.get(position)?.bank_id)
                                        BankName =
                                            java.lang.String.valueOf(mArrCity?.get(position)?.bank_name)
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                                }
                        }

                    } else {
                        AlertDialogs().alertDialogOk(
                            this@WithdrawActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                    }
                }
            }
        })
    }

    private fun AttemptToWithdrawMoney(
        user_id: String?,
        bank_id: String,
        amount: String,
        comment: String
    ) {
        /*progressDialog?.show()
        withdrawMoneyServiceProvider?.getAddBankDetails(
            user_id,
            bank_id,
            amount,
            comment,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? = (serviceResponse as WithdrawMoneyModel).status
                        val message: String? = (serviceResponse as WithdrawMoneyModel).message
                        if (status == "200") {

                            // mAlert.onShowToastNotification(WithdrawActivity.this, message);
                            val intent = Intent(this@WithdrawActivity, SuccessActivity::class.java)
                            intent.putExtra("message", message)
                            startActivity(intent)
                            finish()
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@WithdrawActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )

                            //                        mAlert.onShowToastNotification(WithdrawActivity.this, message);
                            //                        Intent intent=new Intent(WithdrawActivity.this,MainActivity.class);
                            //                        startActivity(intent);
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
                                this@WithdrawActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@WithdrawActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@WithdrawActivity)
                    } finally {
                        progressDialog?.hide()
                    }
                }
            })*/

        //change in api calling

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", user_id)
            .addFormDataPart("bank_id", bank_id)
            .addFormDataPart("amount", amount)
            .addFormDataPart("comment", comment)

            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/add_withdraw_request.php")
//            .header("AUTHORIZATION", "Bearer $token")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                var mMessage = e.message.toString()
                e.printStackTrace()
                Log.e("failure Response", mMessage)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                var resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(resp)
                    var status = json.get("status").toString()
                    var message = json.get("Message").toString()
                    runOnUiThread {
                        if (status == "200") {
                            val intent = Intent(this@WithdrawActivity, SuccessActivity::class.java)
                            intent.putExtra("message", message)
                            startActivity(intent)
                            finish()
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@WithdrawActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )

                        }
                    }
                }
            }
        })

    }

    private fun AttemptToGetMoneyTransactionHistory(user_id: String?) {
//        progressDialog?.show()
        /*    getAllTransactionMoneyServiceProvider!!.getAllTransactionMoneyHistory(
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

                                //       mAlert.onShowToastNotification(WithdrawActivity.this, message);
                            } else {
                                AlertDialogs().alertDialogOk(
                                    this@WithdrawActivity,
                                    "Alert",
                                    message,
                                    resources.getString(R.string.btn_ok),
                                    0,
                                    false,
                                    alertDialogOkListener
                                )
                                //                        mAlert.onShowToastNotification(WithdrawActivity.this, message);
                                // rvGoldBookingHistory.setLayoutManager(new LinearLayoutManager(WithdrawActivity.this));
                                //rvGoldBookingHistory.setAdapter(new GoldBookingHistoryAdapter(WithdrawActivity.this,mArrGoldBookingHistory));
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
                                    this@WithdrawActivity,
                                    (apiErrorModel as BaseServiceResponseModel).message
                                )
                            } else {
                                PrintUtil.showNetworkAvailableToast(this@WithdrawActivity)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            PrintUtil.showNetworkAvailableToast(this@WithdrawActivity)
                        } finally {
                            progressDialog?.hide()
                        }
                    }
                })*/

        // api calling change

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

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: okhttp3.Response) {
                var resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(resp)
                    var status = json.optString("status").toString()
                    var Wallet_Balance = json.optString("Wallet_Balance").toString()
                    var message: String = json.optString("Message").toString()
                    runOnUiThread {
                        if (status == "200") {
                            balance = Wallet_Balance
                            if (balance != null) {
                                txtWalletBalence!!.text = "\u20B9 $balance"
                            }

                        } else {
                            AlertDialogs().alertDialogOk(
                                this@WithdrawActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        }
                    }
                }
            }
        })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@WithdrawActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}



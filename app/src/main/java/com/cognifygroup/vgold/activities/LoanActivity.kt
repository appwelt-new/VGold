package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class LoanActivity : AppCompatActivity(), AlertDialogOkListener {
//    private lateinit var loanEligibleAmt: TextView
//    private lateinit var txtError:TextView
//    private lateinit var btnApply: Button
//    private lateinit var edtPayableAmount: EditText
//    private lateinit var spinnerReason: Spinner
//    private lateinit var loanLayout:LinearLayoutCompat
//    private var userId = ""
//    private lateinit var sharedPreferences: SharedPreferences
//  //  private val progressDialog: TransparentProgressDialog? = null
//  private var alertDialogOkListener: AlertDialogOkListener = this
//
//    private var loanAmt = ""
//    private var loanVal =""
//
//    var mAlert: AlertDialogs? = null

    var loanEligibleAmt: TextView? = null
    var txtError: TextView? = null
    var btnApply: Button? = null
    var edtPayableAmount: EditText? = null
    var spinnerReason: Spinner? = null
    var loanLayout: LinearLayoutCompat? = null

    private var loanAmt: String? = null

    var mAlert: AlertDialogs? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this

    // private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    // var getLoanServiceProvider: LoanServiceProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)

        supportActionBar!!.title = "Loan"


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        loanEligibleAmt = findViewById(R.id.loanEligibleAmt)
        txtError = findViewById(R.id.txtError)
        btnApply = findViewById(R.id.btnApply)
        edtPayableAmount = findViewById(R.id.edtPayableAmount)
        spinnerReason = findViewById(R.id.spinnerReason)
        loanLayout = findViewById(R.id.loanLayout)
        progressDialog = TransparentProgressDialog(this@LoanActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        mAlert = AlertDialogs().getInstance()

        //  getLoanServiceProvider = LoanServiceProvider(this)
        AttemptToLoanDetails(VGoldApp.onGetUerId())

        /*intrestedId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llAmt.setVisibility(View.VISIBLE);
                    llRemark.setVisibility(View.VISIBLE);
                    btnApply.setVisibility(View.VISIBLE);
                } else {
                    llAmt.setVisibility(View.GONE);
                    llRemark.setVisibility(View.GONE);
                    btnApply.setVisibility(View.GONE);
                }
            }
        });*/

        btnApply!!.setOnClickListener {
            btnApply()
        }
    }


    private fun AttemptToLoanDetails(user_id: String?) {
//        progressDialog!!.show()
        /*    getLoanServiceProvider?.getLoanEligibility(user_id, object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? = (serviceResponse as LoanModel).status
                        val message: String? = (serviceResponse as LoanModel).message
                        val loanModel: LoanModel.Data? = (serviceResponse as LoanModel).data
                        if (status.equals("200", ignoreCase = true)) {
                            if (loanModel != null) {
                                loanAmt = loanModel.loan_amount
                                loanEligibleAmt!!.text =
                                    "Congratulations " + VGoldApp.onGetFirst().toString() + "!" +
                                            " You can apply for a loan of up to, " + resources.getString(
                                        R.string.rs
                                    ) + loanModel.loan_amount.toString() + "/-"
                                txtError!!.visibility = View.GONE
                                loanLayout!!.visibility = View.VISIBLE
                            }
                        } else {
                            txtError!!.visibility = View.VISIBLE
                            loanLayout!!.visibility = View.GONE
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
    //                        PrintUtil.showToast(
    //                            this@LoanActivity,
    //                            (apiErrorModel as BaseServiceResponseModel).message
    //                        )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@LoanActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@LoanActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })*/

        // change in api calling

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", user_id.toString())
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_user_loan_eligiblity.php")
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

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: okhttp3.Response) {
                var mMessage = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(mMessage)
                    var status = json.get("status").toString()
                    runOnUiThread {
                        if (status.equals("200", ignoreCase = true)) {
                            var jsonString: String = mMessage //http request
                            var data = json.optJSONObject("data")
                            if (data != null) {
                                runOnUiThread {
                                    loanAmt = data.optString("loan_amount")
                                    loanEligibleAmt!!.text =
                                        "Congratulations " + VGoldApp.onGetFirst()
                                            .toString() + "!" +
                                                " You can apply for a loan of up to, " + resources.getString(
                                            R.string.rs
                                        ) + loanAmt.toString() + "/-"
                                    txtError!!.visibility = View.GONE
                                    loanLayout!!.visibility = View.VISIBLE
                                }
                            }

                        } else {
                            txtError!!.visibility = View.VISIBLE
                            loanLayout!!.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun ApplyForLoan(user_id: String?, amt: String, comment: String) {
//        progressDialog!!.show()
        /*     getLoanServiceProvider?.applyForLoan(user_id, amt, comment, object : APICallback {
                 override fun <T> onSuccess(serviceResponse: T) {
                     try {
                         val status: String? = (serviceResponse as LoanModel).status
                         val message: String? = (serviceResponse as LoanModel).message
                         if (status.equals("200", ignoreCase = true)) {
                             AlertDialogs().alertDialogOk(
                                 this@LoanActivity, "Alert", message,
                                 resources.getString(R.string.btn_ok), 1, false, alertDialogOkListener
                             )
                         } else {
                             AlertDialogs().alertDialogOk(
                                 this@LoanActivity, "Alert", message,
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
     //                        PrintUtil.showToast(
     //                            this@LoanActivity,
     //                            (apiErrorModel as BaseServiceResponseModel).message
     //                        )
                         } else {
                             //     PrintUtil.showNetworkAvailableToast(this@LoanActivity)
                         }
                     } catch (e: Exception) {
                         e.printStackTrace()
                         //   PrintUtil.showNetworkAvailableToast(this@LoanActivity)
                     } finally {
                         progressDialog!!.hide()
                     }
                 }
             })*/


        // change in api calling

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", user_id.toString())
            .addFormDataPart("amount", amt)
            .addFormDataPart("comment", comment)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/save_loan_request.php")
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
                var mMessage = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(mMessage)
                    var status = json.get("status").toString()
                    var message = json.get("Message").toString()
                    runOnUiThread {
                        if (status.equals("200", ignoreCase = true)) {
                            AlertDialogs().alertDialogOk(
                                this@LoanActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

                        } else {
                            AlertDialogs().alertDialogOk(
                                this@LoanActivity,
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun btnApply() {
        val amt = edtPayableAmount!!.text.toString().trim { it <= ' ' }
        val remark = spinnerReason!!.selectedItem.toString()
        if (!TextUtils.isEmpty(amt) && amt.toDouble() > 0) {
            if (loanAmt != null && !TextUtils.isEmpty(loanAmt)) {
                if (loanAmt!!.toDouble() >= amt.toDouble()) {
                    ApplyForLoan(VGoldApp.onGetUerId(), amt, remark)
                } else {
                    AlertDialogs().alertDialogOk(
                        this@LoanActivity,
                        "Alert",
                        "Amount should not more than eligible loan amount.",
                        resources.getString(R.string.btn_ok),
                        0,
                        false,
                        alertDialogOkListener
                    )
                }
            } else {
                AlertDialogs().alertDialogOk(
                    this@LoanActivity, "Alert", "Something went wrong!! Try later.",
                    resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                )
            }
        } else {
            edtPayableAmount!!.error = "Required"
        }
    }


    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@LoanActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
            1 -> {
                val mainIntent = Intent(this@LoanActivity, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
    }
}


//          sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
//     userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()
//        supportActionBar?.title = "Loan"
//
//        loanEligibleAmt= findViewById(R.id.loanEligibleAmt)
//        txtError = findViewById(R.id.txtError)
//        btnApply= findViewById(R.id.btnApply)
//        edtPayableAmount  = findViewById(R.id.edtPayableAmount)
//        spinnerReason = findViewById(R.id.spinnerReason)
//        loanLayout = findViewById(R.id.loanLayout)
//
//
//     attemptToLoanDetails(userId)
//    getLoanVal()
//
//
//     btnApply.setOnClickListener {
//
//         val amt = edtPayableAmount.text.toString().trim { it <= ' ' }
//         val remark = spinnerReason.selectedItem.toString()
//         if (!TextUtils.isEmpty(amt) && amt.toDouble() > 0) {
//             if (loanAmt != "null" && !TextUtils.isEmpty(loanAmt)) {
//                 if (loanAmt!!.toDouble() >= amt.toDouble()) {
//                     applyforloan(userId, amt, remark)
//                 } else {
//                     AlertDialogs().alertDialogOk(
//                         this@LoanActivity,
//                         "Alert",
//                         "Amount should not more than eligible loan amount.",
//                         resources.getString(R.string.btn_ok),
//                         0,
//                         false,
//                         alertDialogOkListener
//                     )
//                 }
//             } else {
//                 AlertDialogs().alertDialogOk(
//                     this@LoanActivity, "Alert", "Something went wrong!! Try later.",
//                     resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
//                 )
//             }
//         } else {
//             edtPayableAmount.error = "Required"
//         }
//     }
//    }
//
//    private fun getLoanVal() {
//        val client = OkHttpClient().newBuilder().build()
//        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("user_id", userId)
//            .build()
//        val request = okhttp3.Request.Builder()
//            .url("https://www.vgold.co.in/dashboard/webservices/get_user_loan_eligiblity.php")
////            .header("AUTHORIZATION", "Bearer $token")
//            .header("Accept", "application/json")
//            .header("Content-Type", "application/json")
//            .post(requestBody)
//            .build()
//        client.newCall(request).enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                val mMessage = e.message.toString()
//                e.printStackTrace()
//                Log.e("failure Response", mMessage)
//            }
//
//            override fun onResponse(call: Call, response: okhttp3.Response) {
//                val mMessage = response.body()!!.string()
//                if (!response.isSuccessful) {
//                    throw IOException("Unexpected code" + response)
//                } else {
//                    val json = JSONObject(mMessage)
//                    val status = json.get("status").toString()
//                    if (status == "200"){
//
//                        val prdds = json.getJSONObject("data")
//
////
////                        loanEligibleAmt = prdds.get("loan_amount") as TextView
////                        loanEligibleAmt.text=  resources.getString(R.string.rs) + loanVal + "/-"
////
//
////                    Log.d("Upload Status", "Image Uploaded Successfully !")
//
////                        requireActivity().runOnUiThread(Runnable {
////
////
////                        })
//
//                    }
//
//                }
//            }
//        })
//    }
//
//    private fun applyforloan(user_id: String, amt: String, comment: String) {
//        val client = OkHttpClient().newBuilder().build()
//        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("user_id", user_id)
//            .addFormDataPart("amount", amt)
//            .addFormDataPart("comment", comment)
//            .build()
//        val request = okhttp3.Request.Builder()
//            .url("https://www.vgold.co.in/dashboard/webservices/save_loan_request.php")
////            .header("AUTHORIZATION", "Bearer $token")
//            .header("Accept", "application/json")
////            .header("Content-Type", "application/json")
//            .post(requestBody)
//            .build()
//        client.newCall(request).enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                val mMessage = e.message.toString()
//                e.printStackTrace()
//                Log.e("failure Response", mMessage)
//            }
//
//            override fun onResponse(call: Call, response: okhttp3.Response) {
//                val mMessage = response.body()!!.string()
//                if (!response.isSuccessful) {
//                    throw IOException("Unexpected code" + response)
//                } else {
////                    Log.d("Upload Status", "Image Uploaded Successfully !")
//                    val json = JSONObject(mMessage)
//                    val status = json.get("status").toString()
//                    val message = json.get("Message").toString()
//
//
//                    runOnUiThread(Runnable {
//                        if (status.equals("200", ignoreCase = true)) {
//                            val prdds = json.getJSONObject("data")
//
//
//                            AlertDialogs().alertDialogOk(
//                                this@LoanActivity, "Alert", message,
//                                resources.getString(R.string.btn_ok), 1, false, alertDialogOkListener
//                            )
//                        } else {
//                            AlertDialogs().alertDialogOk(
//                                this@LoanActivity, "Alert", message,
//                                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
//                            )
//                        }
//
//                    })
//                }
//            }
//        })
//    }
//
//
//    private fun attemptToLoanDetails(user_id: String) {
//        val client = OkHttpClient().newBuilder().build()
//        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("user_id", user_id)
//            .build()
//        val request = okhttp3.Request.Builder()
//            .url("https://www.vgold.co.in/dashboard/webservices/get_user_loan_eligiblity.php")
////            .header("AUTHORIZATION", "Bearer $token")
//            .header("Accept", "application/json")
////            .header("Content-Type", "application/json")
//            .post(requestBody)
//            .build()
//        client.newCall(request).enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                val mMessage = e.message.toString()
//                e.printStackTrace()
//                Log.e("failure Response", mMessage)
//            }
//
//            override fun onResponse(call: Call, response: okhttp3.Response) {
//                val mMessage = response.body()!!.string()
//                if (!response.isSuccessful) {
//                    throw IOException("Unexpected code" + response)
//                } else {
////                    Log.d("Upload Status", "Image Uploaded Successfully !")
//                    val json = JSONObject(mMessage)
//                    val status = json.get("status").toString()
//                    val message = json.get("Message").toString()
//                    if (status == "200"){
//                        val data = json.getJSONObject("data")
//                        loanAmt = data.get("loan_amount").toString()
//                    }
//                    runOnUiThread(Runnable {
//
//
//
//                    })
//                }
//            }
//        })
//    }
//
//    override fun onDialogOk(resultCode: Int) {
//        when (resultCode) {
//            11 -> {
//                val LogIntent = Intent(this@LoanActivity, LoginActivity::class.java)
//                startActivity(LogIntent)
//                finish()
//            }
//            1 -> {
//                val mainIntent = Intent(this@LoanActivity, MainActivity::class.java)
//                startActivity(mainIntent)
//                finish()
//
//        }
//        }
//
//    }
//}

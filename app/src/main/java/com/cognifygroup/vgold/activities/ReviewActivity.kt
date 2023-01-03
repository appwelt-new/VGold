package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRatingBar
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.addcomplain.AddComplainServiceProvider
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class ReviewActivity : AppCompatActivity(), AlertDialogOkListener {

    var edt_comment: EditText? = null
    var btnSubmitReview: Button? = null
    var ratingBar: AppCompatRatingBar? = null

    var addComplainServiceProvider: AddComplainServiceProvider? = null
    var mAlert: AlertDialogs? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        supportActionBar?.title = "Review  "


        sharedPreferences =
            this@ReviewActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        btnSubmitReview = findViewById(R.id.btnSubmitReview)
        edt_comment = findViewById(R.id.edt_comment)
        ratingBar = findViewById(R.id.ratingBar)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()

        btnSubmitReview!!.setOnClickListener {
            onClickOfbtnSubmitReview()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        progressDialog = TransparentProgressDialog(this@ReviewActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        // addComplainServiceProvider = AddComplainServiceProvider(this)
        // loginStatusServiceProvider = LoginStatusServiceProvider(this)
        // checkLoginSession()
    }

    private fun checkLoginSession() {
        loginStatusServiceProvider!!.getLoginStatus(userId, object : APICallback {
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
                                this@ReviewActivity,
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
                            this@ReviewActivity, "Alert", message,
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
                            this@ReviewActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@ReviewActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@ReviewActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }


    fun onClickOfbtnSubmitReview() {
        AttemptToAddReiew(userId, edt_comment!!.text.toString())
    }


    private fun AttemptToAddReiew(user_id: String?, comment: String) {
        // progressDialog!!.show()
        /* addComplainServiceProvider!!.addReview(user_id, comment, object : APICallback {
             override fun <T> onSuccess(serviceResponse: T) {
                 try {
                     val status = (serviceResponse as AddComplainModel).getStatus()
                     val message = (serviceResponse as AddComplainModel).getMessage()
                     if (status == "200") {
                         AlertDialogs().alertDialogOk(
                             this@ReviewActivity, "Alert", "Thank You for the valuable review",
                             resources.getString(R.string.btn_ok), 1, false, alertDialogOkListener
                         )

 //                        mAlert.onShowToastNotification(ComplainActivity.this, message);
 //                        Intent intent=new Intent(ComplainActivity.this,MainActivity.class);
 //                        startActivity(intent);
                     } else {
                         AlertDialogs().alertDialogOk(
                             this@ReviewActivity, "Alert", message,
                             resources.getString(R.string.btn_ok), 1, false, alertDialogOkListener
                         )

 //                        mAlert.onShowToastNotification(ComplainActivity.this, message);
 //                        Intent intent=new Intent(ComplainActivity.this,MainActivity.class);
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
                             this@ReviewActivity,
                             (apiErrorModel as BaseServiceResponseModel).message
                         )
                     } else {
                         PrintUtil.showNetworkAvailableToast(this@ReviewActivity)
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     PrintUtil.showNetworkAvailableToast(this@ReviewActivity)
                 } finally {
                     progressDialog!!.hide()
                 }
             }
         })*/


        // change in api

        // change in api
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("notes", comment)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/user_notes.php")
//            .header("AUTHORIZATION", "Bearer $token")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                e.printStackTrace()
                progressDialog!!.hide()

                Log.e("failure Response", mMessage)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val mMessage = response.body()!!.string()
                val json2 = JSONObject(mMessage)
                val message = json2.get("Message").toString()
                progressDialog!!.hide()

                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    val json = JSONObject(mMessage)
                    val status = json.get("status").toString()
                    if (status.equals("200", ignoreCase = true)) {
                        runOnUiThread {
                            AlertDialogs().alertDialogOk(
                                this@ReviewActivity,
                                "Alert",
                                "Thank You for the valuable review",
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )
                        }

                    } else {
                        runOnUiThread {
                            AlertDialogs().alertDialogOk(
                                this@ReviewActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
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
            1 -> {
                val intent = Intent(this@ReviewActivity, MainActivity::class.java)
                startActivity(intent)
            }
            11 -> {
                val LogIntent = Intent(this@ReviewActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

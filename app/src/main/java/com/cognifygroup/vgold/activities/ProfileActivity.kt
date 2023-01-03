package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.json.JSONObject

class ProfileActivity : AppCompatActivity(), AlertDialogOkListener {

    var txtName: TextView? = null

    //  @InjectView(R.id.txtPanNumber)
    var txtPanNumber: TextView? = null

    //  @InjectView(R.id.txtCRN)
    var txtCRN: TextView? = null

    //
//    @InjectView(R.id.txtMail)
//    var txtMail: TextView? = null
    private lateinit var txtMail: TextView

    //    @InjectView(R.id.txtPhone)
//    var txtPhone: TextView? = null
    private lateinit var txtPhone: TextView

    //    @InjectView(R.id.txtAddress)
//    var txtAddress: TextView? = null
//
//    @InjectView(R.id.imgBarcode)
//    var imgBarcode: ImageView? = null
    private lateinit var txtAddress: TextView
    private lateinit var imgBarcode: ImageView

    private lateinit var btnUpdateProfile: Button
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this

    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //  ButterKnife.inject(this)
        sharedPreferences =
            this@ProfileActivity.getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()

        txtCRN = findViewById(R.id.txtCRN)
        txtPanNumber = findViewById(R.id.txtPanNumber)
        txtName = findViewById(R.id.txtName)
        txtMail = findViewById(R.id.txtMail)
        txtPhone = findViewById(R.id.txtPhone)
        txtAddress = findViewById(R.id.txtAddress)
        imgBarcode = findViewById(R.id.imgBarcode)
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile"
        init()
        btnUpdateProfile.setOnClickListener {
            startActivity(Intent(this@ProfileActivity, UpdateProfileActivity::class.java))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun init() {
        progressDialog = TransparentProgressDialog(this@ProfileActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        Log.i("TAG", "init: " + VGoldApp)
        txtName!!.text = VGoldApp.onGetFirst() + " " + VGoldApp.onGetLast()
        txtCRN!!.text = userId
        var pan = VGoldApp.onGetPanNo()
        if (pan == "" || pan == null) {
            pan = "0000000000"
        }

        try {
            val screte_pan = pan.substring(pan.length - 4, pan.length)
            txtPanNumber!!.text = "XXXXXX$screte_pan"
        } catch (e: Exception) {

        }


        txtMail.text = VGoldApp.onGetEmail()
        txtPhone.text = VGoldApp.onGetNo()
        txtAddress.text = (VGoldApp.onGetAddress() + ", " + VGoldApp.onGetCity()
                + ", " + VGoldApp.onGetState())
//        Picasso.with(this)
//            .load(VGoldApp.onGetQrCode()) //.placeholder(R.drawable.images)
//            .resize(400, 400)
//            .into(imgBarcode, object : Callback {
//                override fun onSuccess() {
//                    /*if (holder.progressbar_category !=null){
//                            holder.progressbar_category.setVisibility(View.GONE);
//                        }*/
//
//                }
//
//                override fun onError() {}
//            })
        //   loginStatusServiceProvider = LoginStatusServiceProvider(this)
        //  checkLoginSession()
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
                                this@ProfileActivity,
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
                            this@ProfileActivity, "Alert", message,
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
                            this@ProfileActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@ProfileActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@ProfileActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }
//    private fun checkLoginSession() {
//        loginStatusServiceProvider!!.getLoginStatus(VGoldApp.onGetUerId(), object : APICallback {
//            override fun <T> onSuccess(serviceResponse: T) {
//                try {
//                    progressDialog!!.hide()
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
//                            //
//                            AlertDialogs().alertDialogOk(
//                                this@ProfileActivity,
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
//                            this@ProfileActivity, "Alert", message,
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
//                    progressDialog!!.hide()
//                    if (apiErrorModel != null) {
//                        PrintUtil.showToast(
//                            this@ProfileActivity,
//                            (apiErrorModel as BaseServiceResponseModel).message
//                        )
//                    } else {
//                        PrintUtil.showNetworkAvailableToast(this@ProfileActivity)
//                    }
//                } catch (e: Exception) {
//                    progressDialog!!.hide()
//                    e.printStackTrace()
//                    PrintUtil.showNetworkAvailableToast(this@ProfileActivity)
//                } finally {
//                    progressDialog!!.hide()
//                }
//            }
//        })
//    }

//    @OnClick(R.id.btnUpdateProfile)
//    fun onClickOfBtnUpdateProfile() {
//        startActivity(Intent(this@ProfileActivity, UpdateProfileActivity::class.java))
//    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@ProfileActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

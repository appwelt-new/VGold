package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog


class MembershipActivity : AppCompatActivity(), AlertDialogOkListener {

    var txtClientName: TextView? = null
    var txtCrnNo: TextView? = null


    var txtValiditydate: TextView? = null

    var imgClient: ImageView? = null

    var imageView3: ImageView? = null
    var rlMembershipCard: RelativeLayout? = null
    var mAlert: AlertDialogs? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this

    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_membership)

        ButterKnife.inject(this@MembershipActivity)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Membership"


        sharedPreferences =
            this@MembershipActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()

        txtClientName = findViewById(R.id.txtClientName)
        txtCrnNo = findViewById(R.id.txtCrnNo)
        rlMembershipCard = findViewById(R.id.rlMembershipCard)
        imageView3 = findViewById(R.id.imageView3)

        imgClient = findViewById(R.id.imgClient)
        txtValiditydate = findViewById(R.id.txtValiditydate)

        progressDialog = TransparentProgressDialog(this@MembershipActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        mAlert = AlertDialogs().getInstance()
        // loginStatusServiceProvider = LoginStatusServiceProvider(this)

        // checkLoginSession()
        loadData()
    }

    private fun loadData() {
        txtCrnNo!!.text = "CRN - " + userId
        txtClientName!!.text = "" + VGoldApp.onGetFirst().toString() + " " + VGoldApp.onGetLast()
        if (VGoldApp.onGetUserRole().equals("member")) {
            rlMembershipCard!!.setBackgroundResource(R.drawable.membershipcardlifetime)
            txtValiditydate!!.visibility = View.GONE
        } else {
            txtValiditydate!!.text = VGoldApp.onGetValidity()
            rlMembershipCard!!.setBackgroundResource(R.drawable.membershipcardlimited)
        }

        if (!VGoldApp.onGetUserImg().equals("")) {
            Glide.with(this).load(VGoldApp.onGetUserImg())
                .placeholder(R.drawable.grayavator)
                .error(R.drawable.grayavator)
                .into(imgClient!!)
        }
    }

    private fun checkLoginSession() {
        loginStatusServiceProvider?.getLoginStatus(VGoldApp.onGetUerId(), object : APICallback {
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
                                this@MembershipActivity,
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
                            this@MembershipActivity, "Alert", message,
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
                            this@MembershipActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@MembershipActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@MembershipActivity)
                } finally {
                    progressDialog!!.hide()
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


    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@MembershipActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
            1 -> {
                val mainIntent = Intent(this@MembershipActivity, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
    }
}

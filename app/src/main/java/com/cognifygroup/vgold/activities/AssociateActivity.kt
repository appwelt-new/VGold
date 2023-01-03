package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.InjectView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.adapters.VendorOfferAdapter
import com.cognifygroup.vgold.getVendorOffer.VendorOfferModel
import com.cognifygroup.vgold.getVendorOffer.VendorOfferServiceProvider
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class AssociateActivity : AppCompatActivity() , AlertDialogOkListener {

    var rc_vendorOffer: RecyclerView? = null

    var txtError: TextView? = null


    var mAlert: AlertDialogs? = null
    private var vendorOfferAdapter: VendorOfferAdapter? = null
    private var vendorOfferServiceProvider: VendorOfferServiceProvider? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_associate)

        supportActionBar?.title = "Associate "
        sharedPreferences =
            this@AssociateActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()



        rc_vendorOffer = findViewById(R.id.rc_vendorOffer);
        txtError = findViewById(R.id.txtError);

        init()
    }

    private fun init() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        progressDialog = TransparentProgressDialog(this@AssociateActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        vendorOfferServiceProvider = VendorOfferServiceProvider(this)
        loginStatusServiceProvider = LoginStatusServiceProvider(this)
    //    checkLoginSession()
        AttemptToGetVendorOffer()
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
                                this@AssociateActivity,
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
                            this@AssociateActivity, "Alert", message,
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
                            this@AssociateActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@AssociateActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@AssociateActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }


    private fun AttemptToGetVendorOffer() {
//        progressDialog!!.show()
        vendorOfferServiceProvider!!.getGoldBookingHistory(object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    val status = (serviceResponse as VendorOfferModel).status
                    val message = (serviceResponse as VendorOfferModel).message
                    val mArrSaloonServices = (serviceResponse as VendorOfferModel).data
                    if (status == "200") {
                        txtError!!.visibility = View.GONE
                        rc_vendorOffer!!.visibility = View.VISIBLE
                        vendorOfferAdapter = VendorOfferAdapter(
                            this@AssociateActivity,
                            mArrSaloonServices!!,
                            "venders"
                        )
                        //                        rc_vendorOffer.setLayoutManager(new LinearLayoutManager(OurVendersActivity.this, LinearLayoutManager.VERTICAL, false));
                        rc_vendorOffer!!.layoutManager =
                            GridLayoutManager(this@AssociateActivity, 3)
                        rc_vendorOffer!!.adapter = vendorOfferAdapter
                    } else {
                        txtError!!.visibility = View.VISIBLE
                        rc_vendorOffer!!.visibility = View.GONE
                        AlertDialogs().alertDialogOk(
                            this@AssociateActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )

//                        mAlert.onShowToastNotification(OurVendersActivity.this, message);
                        /* Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(intent);*/
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
                            this@AssociateActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@AssociateActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@AssociateActivity)
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
                val LogIntent = Intent(this@AssociateActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

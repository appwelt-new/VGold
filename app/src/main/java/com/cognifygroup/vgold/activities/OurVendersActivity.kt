package com.cognifygroup.vgold.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.adapters.VendorOfferAdapter
import com.cognifygroup.vgold.getVendorOffer.VendorOfferModel
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import okhttp3.Call
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.IOException

class OurVendersActivity : AppCompatActivity(), AlertDialogOkListener {

    var rc_vendorOffer: RecyclerView? = null
    var txtError: TextView? = null


    var mAlert: AlertDialogs? = null
    private var vendorOfferAdapter: VendorOfferAdapter? = null

    //  private var vendorOfferServiceProvider: VendorOfferServiceProvider? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this

    //private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_our_venders)

        rc_vendorOffer = findViewById(R.id.rc_vendorOffer)
        txtError = findViewById(R.id.txtError)


        init()
    }

    private fun init() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Our Vendors"
        progressDialog = TransparentProgressDialog(this@OurVendersActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        //  vendorOfferServiceProvider = VendorOfferServiceProvider(this)
        // loginStatusServiceProvider = LoginStatusServiceProvider(this)
        //  checkLoginSession()
        AttemptToGetVendorOffer()
    }

    /* private fun checkLoginSession() {
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
                                 this@OurVendersActivity,
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
                             this@OurVendersActivity, "Alert", message,
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
                             this@OurVendersActivity,
                             (apiErrorModel as BaseServiceResponseModel).message
                         )
                     } else {
                         PrintUtil.showNetworkAvailableToast(this@OurVendersActivity)
                     }
                 } catch (e: Exception) {
                     progressDialog!!.hide()
                     e.printStackTrace()
                     PrintUtil.showNetworkAvailableToast(this@OurVendersActivity)
                 } finally {
                     progressDialog!!.hide()
                 }
             }
         })
     }*/

    override fun onBackPressed() {
        finish()
    }


    private fun AttemptToGetVendorOffer() {
        // progressDialog!!.show()
        /*  vendorOfferServiceProvider!!.getGoldBookingHistory(object : APICallback {
              override fun <T> onSuccess(serviceResponse: T) {
                  try {
                      val status: String? = (serviceResponse as VendorOfferModel).status
                      val message: String? = (serviceResponse as VendorOfferModel).message
                      val mArrSaloonServices: ArrayList<VendorOfferModel.Data>? =
                          (serviceResponse as VendorOfferModel).data
                      if (status == "200") {
                          txtError!!.visibility = View.GONE
                          rc_vendorOffer!!.visibility = View.VISIBLE
                          vendorOfferAdapter = VendorOfferAdapter(
                              this@OurVendersActivity,
                              mArrSaloonServices!!,
                              "venders"
                          )
                          //                        rc_vendorOffer.setLayoutManager(new LinearLayoutManager(OurVendersActivity.this, LinearLayoutManager.VERTICAL, false));
                          rc_vendorOffer!!.layoutManager =
                              GridLayoutManager(this@OurVendersActivity, 3)
                          rc_vendorOffer!!.adapter = vendorOfferAdapter
                      } else {
                          txtError!!.visibility = View.VISIBLE
                          rc_vendorOffer!!.visibility = View.GONE
                          AlertDialogs().alertDialogOk(
                              this@OurVendersActivity, "Alert", message,
                              resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                          )

  //                        mAlert.onShowToastNotification(OurVendersActivity.this, message);
                           Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(intent);
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
//                            this@OurVendersActivity,
//                            (apiErrorModel as BaseServiceResponseModel).message
//                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@OurVendersActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@OurVendersActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })*/


        val client = OkHttpClient().newBuilder().build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/vendor_upload.php")
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
                val json = JSONObject(responce)
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    try {
                        val status: String? =
                            json.getString("status")
                        val message: String? =
                            json.getString("Message")
                        if (status == "200") {
                            runOnUiThread {
                                val prdds = json.getJSONArray("Data")
                                val cndList = ArrayList<VendorOfferModel.Data>()
                                for (i in 0 until prdds.length()) {
                                    val item = VendorOfferModel.Data(
                                        prdds.getJSONObject(i).getString("vendor_id"),
                                        prdds.getJSONObject(i).getString("logo_path"),
                                        prdds.getJSONObject(i).getString("letter_path"),
                                        prdds.getJSONObject(i).getString("advertisement_path")
                                    )
                                    cndList += item
                                }


                                val mArrSaloonServices: ArrayList<VendorOfferModel.Data> =
                                    cndList
                                txtError!!.visibility = View.GONE
                                rc_vendorOffer!!.visibility = View.VISIBLE
                                if (!mArrSaloonServices.isEmpty()) {
                                    vendorOfferAdapter = VendorOfferAdapter(
                                        this@OurVendersActivity,
                                        mArrSaloonServices,
                                        "venders"
                                    )
                                    rc_vendorOffer!!.layoutManager =
                                        GridLayoutManager(this@OurVendersActivity, 3)
                                    rc_vendorOffer!!.adapter = vendorOfferAdapter
                                }
                            }
                        } else {
                            runOnUiThread {
                                txtError!!.visibility = View.VISIBLE
                                rc_vendorOffer!!.visibility = View.GONE
                                AlertDialogs().alertDialogOk(
                                    this@OurVendersActivity,
                                    "Alert",
                                    message,
                                    resources.getString(R.string.btn_ok),
                                    0,
                                    false,
                                    alertDialogOkListener
                                )
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
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


    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@OurVendersActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

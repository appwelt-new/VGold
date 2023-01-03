package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.CPModule.CPServiceProvider
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.adapters.CPUserDetailsAdapter
import com.cognifygroup.vgold.channelpartner.UserDetailsModel_
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ChannelPartnerActivity : AppCompatActivity(), AlertDialogOkListener {
    var txtUserCountr: TextView? = null
    var txtCommission: TextView? = null
    var txtGold: TextView? = null
    var noData: TextView? = null

    var recyclerUsers: RecyclerView? = null
    var etUserSearch: AppCompatEditText? = null

    var mAlert: AlertDialogs? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    // private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    // private var mCPUserServiceProvider: CPServiceProvider? = null

    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences


    private var mAdapter: CPUserDetailsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_partner)
        supportActionBar?.title = "Channel Partner"


        sharedPreferences =
            this@ChannelPartnerActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()

        txtUserCountr = findViewById(R.id.txtUserCountr)
        etUserSearch = findViewById(R.id.etUserSearch)
        recyclerUsers = findViewById(R.id.recyclerUsers)
        noData = findViewById(R.id.noData)
        txtGold = findViewById(R.id.txtGold)
        txtCommission = findViewById(R.id.txtCommission)



        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        progressDialog = TransparentProgressDialog(this@ChannelPartnerActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        //  mCPUserServiceProvider = CPServiceProvider(this)
        // loginStatusServiceProvider = LoginStatusServiceProvider(this)
        recyclerUsers!!.layoutManager = LinearLayoutManager(this@ChannelPartnerActivity)
        if (VGoldApp.onGetUserTot() != null && !TextUtils.isEmpty(VGoldApp.onGetUserTot())) {
            txtUserCountr!!.text = VGoldApp.onGetUserTot()
        }
        if (VGoldApp.onGetCommissionTot() != null && !TextUtils.isEmpty(VGoldApp.onGetCommissionTot())) {
            txtCommission!!.text = resources.getString(R.string.rs) + VGoldApp.onGetCommissionTot()
        }
        if (VGoldApp.onGetGoldTot() != null && !TextUtils.isEmpty(VGoldApp.onGetGoldTot())) {
            txtGold!!.text = VGoldApp.onGetGoldTot() + " gm"
        }

        etUserSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count < before) {
                    if (mAdapter != null) {
                        mAdapter!!.resetData()
                    }
                }
                if (mAdapter != null) {
                    mAdapter!!.getFilter()?.filter(s.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        // checkLoginSession()
        getUserDetails()
    }

    fun getUserDetails() {
        //  progressDialog!!.show()
        Log.i("TAG", "getUserDetails: " + userId)
        /* mCPUserServiceProvider?.getUserDetails(VGoldApp.onGetUerId(), object : APICallback {
             override fun <T> onSuccess(serviceResponse: T) {
                 try {
                     val Status: String? = (serviceResponse as UserDetailsModel).status
                     val message: String? = (serviceResponse as UserDetailsModel).message
                     val userDetailsModelArrayList: ArrayList<UserDetailsModel.Data>? =
                         (serviceResponse as UserDetailsModel).data
                     Log.i("TAG", "onSuccess: $userDetailsModelArrayList")
                     if (Status == "200") {
                         if (userDetailsModelArrayList != null && userDetailsModelArrayList.size > 0) {
                             noData!!.visibility = View.GONE
                             recyclerUsers!!.layoutManager =
                                 LinearLayoutManager(this@ChannelPartnerActivity)
                             mAdapter = CPUserDetailsAdapter(
                                 this@ChannelPartnerActivity,
                                 userDetailsModelArrayList
                             )
                             recyclerUsers!!.adapter = mAdapter
                             mAdapter!!.notifyDataSetChanged()
                         } else {
                             noData!!.visibility = View.VISIBLE
                         }
                     } else {
                         AlertDialogs().alertDialogOk(
                             this@ChannelPartnerActivity, "Alert", message,
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
 //                            this@ChannelPartnerActivity,
 //                            (apiErrorModel as BaseServiceResponseModel).message
 //                        )
                     } else {
                         PrintUtil.showNetworkAvailableToast(this@ChannelPartnerActivity)
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     PrintUtil.showNetworkAvailableToast(this@ChannelPartnerActivity)
                 } finally {
                     progressDialog!!.hide()
                 }
             }
         })
 */

        // change in api
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/cp_user_list.php")
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

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: okhttp3.Response) {
                var mMessage = response.body()!!.string()
                var json2 = JSONObject(mMessage)
                var message = json2.get("Message").toString()
                progressDialog!!.hide()

                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(mMessage)
                    var status = json.get("status").toString()
                    if (status.equals("200", ignoreCase = true)) {
                        runOnUiThread {
                            var jsonArray: JSONArray = json.optJSONArray("data") as JSONArray

                            var cndList = ArrayList<UserDetailsModel_.Data>()
                            if (jsonArray.length() > 0) {
                                for (i in 0 until jsonArray.length()) {
                                    val item = UserDetailsModel_.Data()
                                    item.uid = jsonArray.optJSONObject(i).optString("uid")
                                    item.uname = jsonArray.optJSONObject(i).optString("uname")
                                    item.umobile = jsonArray.optJSONObject(i).optString("umobile")
                                    item.uemail = jsonArray.optJSONObject(i).optString("uemail")
                                    item.urole = jsonArray.optJSONObject(i).optString("urole")
                                    item.upic = jsonArray.optJSONObject(i).optString("upic")
                                    item.total_gold_in_account = jsonArray.optJSONObject(i)
                                        .optString("total_gold_in_account")
                                    item.total_commission_created = jsonArray.optJSONObject(i)
                                        .optString("total_commission_created")
                                    cndList += item
                                }
                            }

                            val userDetailsModelArrayList: ArrayList<UserDetailsModel_.Data> =
                                cndList
                            if (userDetailsModelArrayList.size > 0) {
                                noData!!.visibility = View.GONE
                                recyclerUsers!!.layoutManager =
                                    LinearLayoutManager(this@ChannelPartnerActivity)
                                mAdapter = CPUserDetailsAdapter(
                                    this@ChannelPartnerActivity,
                                    userDetailsModelArrayList
                                )
                                recyclerUsers!!.adapter = mAdapter
                                mAdapter!!.notifyDataSetChanged()
                            } else {
                                noData!!.visibility = View.VISIBLE
                            }
                        }

                    } else {
                        runOnUiThread {
                            AlertDialogs().alertDialogOk(
                                this@ChannelPartnerActivity,
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
                val LogIntent = Intent(this@ChannelPartnerActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

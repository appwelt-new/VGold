package com.cognifygroup.vgold.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.goldbooking.GoldBookingModel
import com.cognifygroup.vgold.goldbooking.GoldBookingServiceProvider
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class GoldBookingActivity : AppCompatActivity(), AlertDialogOkListener {
    //    private lateinit var spgoldWeight: Spinner
//    private lateinit var spTennure: Spinner
//    private lateinit var edtProCode: EditText
//    private var userId = ""
//    private lateinit var sharedPreferences: SharedPreferences
//    private lateinit var btnNext : Button
    var spinner_goldWeight: Spinner? = null
    var spinner_tennure: Spinner? = null


    var edtPromoCode: EditText? = null
    var edtCustomGoldWeight: EditText? = null
    var llCustom: LinearLayout? = null
    var btnNext: Button? = null
    var customweight = 0
    var goldWeight1: String? = null
    var tennure: String? = null

    var mAlert: AlertDialogs? = null
    var goldBookingServiceProvider: GoldBookingServiceProvider? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold_booking)
        // sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)


        supportActionBar?.title = "Gold Booking "

        spinner_tennure = findViewById(R.id.spinner_tennure)
        spinner_goldWeight = findViewById(R.id.spinner_goldWeight)
        btnNext = findViewById(R.id.btnNext)
        edtPromoCode = findViewById(R.id.edtPromoCode)
        edtCustomGoldWeight = findViewById(R.id.edtCustomGoldWeight)
        llCustom = findViewById(R.id.llCustom)


        progressDialog = TransparentProgressDialog(this@GoldBookingActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        mAlert = AlertDialogs().getInstance()

        //  goldBookingServiceProvider = GoldBookingServiceProvider(this)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.GoldWeight_array1, android.R.layout.simple_spinner_item
        )
// Specify the layout to use when the list of choices appears
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
// Apply the adapter to the spinner
        // Apply the adapter to the spinner
        spinner_goldWeight!!.adapter = adapter
        spinner_goldWeight!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val goldWeight = parent.getItemAtPosition(pos) as String
                if (goldWeight == "custom") {
                    llCustom!!.visibility = View.VISIBLE
                } else {
                    llCustom!!.visibility = View.GONE
                    goldWeight1 = goldWeight
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.Tennure_array, android.R.layout.simple_spinner_item
        )
// Specify the layout to use when the list of choices appears
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(R.layout.custom_spinner_item)
// Apply the adapter to the spinner
        // Apply the adapter to the spinner
        spinner_tennure!!.adapter = adapter1
        spinner_tennure!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                tennure = parent.getItemAtPosition(pos) as String
                tennure = if (tennure == "12 month") {
                    "12"
                } else if (tennure == "24 month") {
                    "24"
                } else if (tennure == "36 month") {
                    "36"
                } else if (tennure == "48 month") {
                    "48"
                } else if (tennure == "60 month") {
                    "60"
                } else if (tennure == "72 month") {
                    "72"
                } else if (tennure == "84 month") {
                    "84"
                } else if (tennure == "96 month") {
                    "96"
                } else if (tennure == "108 month") {
                    "108"
                } else if (tennure == "120 month") {
                    "120"
                } else {
                    ""
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }


        btnNext!!.setOnClickListener {
            onClickOnBtnNext()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClickOnBtnNext() {
        if (llCustom!!.visibility == View.VISIBLE) {
            customweight = try {
                edtCustomGoldWeight!!.text.toString().toInt()
            } catch (e: Exception) {
                0
            }
            if (customweight < 10) {
                edtCustomGoldWeight!!.error = "Gold Weight Greater than 10"
            } else {
                if (llCustom!!.visibility == View.VISIBLE) {
                    goldWeight1 = edtCustomGoldWeight!!.text.toString()
                }
                AttemptToGetGoldBooking(goldWeight1!!, tennure!!, edtPromoCode!!.text.toString())
            }
        } else {
            AttemptToGetGoldBooking(goldWeight1!!, tennure!!, edtPromoCode!!.text.toString())
        }
    }


    private fun AttemptToGetGoldBooking(
        quantity: String, tennure: String,
        pc: String
    ) {
        /*progressDialog?.show()
        goldBookingServiceProvider?.getAddBankDetails(
            quantity,
            tennure,
            pc,
            VGoldApp.onGetUerId(),
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? = (serviceResponse as GoldBookingModel).status
                        val message: String? = (serviceResponse as GoldBookingModel).message
                        val monthly: String? = (serviceResponse as GoldBookingModel).monthly
                        val booking_value: String? =
                            (serviceResponse as GoldBookingModel).booking_value
                        val down_payment: String? =
                            (serviceResponse as GoldBookingModel).down_payment
                        val gold_rate: String? = (serviceResponse as GoldBookingModel).gold_rate
                        val booking_charge: String? =
                            (serviceResponse as GoldBookingModel).bookingCharge
                        val initialBookingCharge: String? =
                            (serviceResponse as GoldBookingModel).initial_Booking_charges
                        val bookingChargeDisc: String? =
                            (serviceResponse as GoldBookingModel).booking_charges_discount
                        if (status == "200") {

                            //  mAlert.onShowToastNotification(GoldBookingActivity.this, message);
                            val intent =
                                Intent(this@GoldBookingActivity, BookingDetailsActivity::class.java)
                            intent.putExtra("monthly", monthly)
                            intent.putExtra("booking_value", booking_value)
                            intent.putExtra("down_payment", down_payment)
                            intent.putExtra("gold_rate", gold_rate)
                            intent.putExtra("booking_charge", booking_charge)
                            intent.putExtra("quantity", quantity)
                            intent.putExtra("tennure", tennure)
                            intent.putExtra("pc", pc)
                            intent.putExtra("initBookingCharge", initialBookingCharge)
                            intent.putExtra("disc", bookingChargeDisc)
                            startActivity(intent)
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@GoldBookingActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )

                            //                        mAlert.onShowToastNotification(GoldBookingActivity.this, message);
                            //                        Intent intent = new Intent(GoldBookingActivity.this, BookingDetailActivity.class);
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
                                this@GoldBookingActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@GoldBookingActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@GoldBookingActivity)
                    } finally {
                        progressDialog?.hide()
                    }
                }
            })
*/
        // change in api


        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("quantity", quantity)
            .addFormDataPart("tennure", tennure)
            .addFormDataPart("pc", pc)
            .addFormDataPart("user_id", VGoldApp.onGetUerId())
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/booking_details.php")
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
                    var message = json.get("Message").toString()
                    if (status == "200") {
                        Log.e(" Response", resp)
                        runOnUiThread {
                            var monthly: String? = json.optString("Monthly")
                            var booking_value: String? =
                                json.optString("Booking_value")
                            var down_payment: String? =
                                json.optString("Down_payment")
                            var gold_rate: String? = json.optString("Gold_rate")
                            var booking_charge: String? = json.optString("Booking_charges")
                            var initialBookingCharge: String? =
                                json.optString("Initial_Booking_charges")
                            var bookingChargeDisc: String? =
                                json.optString("Booking_charges_discount")

                            //  mAlert.onShowToastNotification(GoldBookingActivity.this, message);
                            var intent =
                                Intent(
                                    this@GoldBookingActivity,
                                    BookingDetailsActivity::class.java
                                )
                            intent.putExtra("monthly", monthly)
                            intent.putExtra("booking_value", booking_value)
                            intent.putExtra("down_payment", down_payment)
                            intent.putExtra("gold_rate", gold_rate)
                            intent.putExtra("booking_charge", booking_charge)
                            intent.putExtra("quantity", quantity)
                            intent.putExtra("tennure", tennure)
                            intent.putExtra("pc", pc)
                            intent.putExtra("initBookingCharge", initialBookingCharge)
                            intent.putExtra("disc", bookingChargeDisc)
                            startActivity(intent)

                        }
                    } else {
                        runOnUiThread {
                            AlertDialogs().alertDialogOk(
                                this@GoldBookingActivity,
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
            1 -> {
                val intent = Intent(this@GoldBookingActivity, BookingDetailsActivity::class.java)
                startActivity(intent)
            }
            11 -> {
                val LogIntent = Intent(this@GoldBookingActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}


//        spgoldWeight = findViewById(R.id.spinner_goldWeight)
//        spTennure = findViewById(R.id.spinner_tennure)
//        edtProCode = findViewById(R.id.edtPromoCode)
//        btnNext = findViewById(R.id.btnNext)
//
//        btnNext.setOnClickListener {
//
//
//            if (spTennure.selectedItemPosition == 0) {
//                Toast.makeText(this, "Please Select Tennure !", Toast.LENGTH_SHORT)
//                    .show()
//                spTennure.requestFocus()
//            } else
//                logintoVgold()
//
//        }
//    }
//    private fun logintoVgold() {
//        val client = OkHttpClient().newBuilder().build()
//        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("quantity", spgoldWeight.selectedItemPosition.toString())
//            .addFormDataPart("tennure", spTennure.selectedItemPosition.toString())
//            .addFormDataPart("pc", edtProCode.text.toString())
//            .build()
//        val request = okhttp3.Request.Builder()
//            .url("https://www.vgold.co.in/dashboard/webservices/booking_details.php?")
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
//                    runOnUiThread(Runnable {
//                        val intent = Intent(this@GoldBookingActivity, BookingDetailsActivity::class.java)
//                       // intent.putExtra("email_id", edtEmail.text.toString())
//                        startActivity(intent)
//                        // Toast.makeText(this@LoginActivity, ".$mMessage", Toast.LENGTH_SHORT).show()
//                        finish()
//                    })
//                }
//            }
//        })
//
//
//            val intent= Intent(this, BookingDetailsActivity::class.java)
//
//            startActivity(intent)
//        }
//    }

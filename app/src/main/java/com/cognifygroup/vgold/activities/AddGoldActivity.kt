package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.cognifygroup.vgold.BuildConfig
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.addgold.AddGoldServiceProvider
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.GetTodayGoldRateModel
import com.cognifygroup.vgold.model.GetTodayGoldRateServiceProvider
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

class AddGoldActivity : AppCompatActivity(), AlertDialogOkListener {

    var edtAmount: EditText? = null

    var txtGoldWeight: TextView? = null
    var txtGoldRate: TextView? = null
    var spinner_payment_option: Spinner? = null
    var llCheque: LinearLayout? = null
    var llRTGS: LinearLayout? = null
    var btnProceedToPayment: Button? = null
    var btnShareBankDetails: Button? = null
    var edtBankDetail: EditText? = null
    var edtChequeNo: EditText? = null
    var edtRtgsBankDetail: EditText? = null
    var edtTxnId: EditText? = null
    var objbnkdetiv: ImageView? = null
    var shreiv: ImageView? = null
//    var bnkdethideiv: ImageView? = null

    val UPI_PAYMENT = 0
    var GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    var GOOGLE_PAY_REQUEST_CODE = 123
    private var succesMsg: String? = null

    var mAlert: AlertDialogs? = null
    var getTodayGoldRateServiceProvider: GetTodayGoldRateServiceProvider? = null
    var addGoldServiceProvider: AddGoldServiceProvider? = null
    private val alertDialogOkListener: AlertDialogOkListener = this

    var result = 0.0
    var amount = 0.0
    var goldWeight = "0.0"
    var payment_option = ""
    var todayGoldRate = "0"
    var todayGoldRateWithGst = "0"
    private var progressDialog: TransparentProgressDialog? = null
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gold)

        txtGoldRate = findViewById(R.id.txtGoldRate)
        txtGoldWeight = findViewById(R.id.txtGoldWeight)
        edtAmount = findViewById(R.id.edtAmount)
        edtBankDetail = findViewById(R.id.edtBankDetail)
        edtChequeNo = findViewById(R.id.edtChequeNo)
        edtRtgsBankDetail = findViewById(R.id.edtRtgsBankDetail)
        edtTxnId = findViewById(R.id.edtTxnId)
        btnProceedToPayment = findViewById(R.id.btnProceedToPayment)
        spinner_payment_option = findViewById(R.id.spinner_payment_option)
        llCheque = findViewById(R.id.llCheque)
        llRTGS = findViewById(R.id.llRTGS)
        objbnkdetiv = findViewById(R.id.bnkdetiv)
        btnShareBankDetails = findViewById(R.id.btnShareBankDetails)
        shreiv = findViewById(R.id.shreiv)
//        bnkdethideiv = findViewById(R.id.bnkdethideiv)
        supportActionBar?.title = "Add Gold "
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()

        Glide.with(this)
            .load("https://www.vgold.co.in/dashboard/vgold_rate/bank%20details.png")
            .into(objbnkdetiv!!);

        btnProceedToPayment!!.setOnClickListener {
            OnClickOfProceedToPayment()

            btnShareBankDetails!!.setOnClickListener {

                val bmpUri: Uri? = getLocalBitmapUri(objbnkdetiv!!)
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image

                    // Construct a ShareIntent with link to image
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                    shareIntent.type = "image/*"
                    // Launch sharing dialog for image
                    // Launch sharing dialog for image
                    startActivity(Intent.createChooser(shareIntent, "Share VGold Bank Details"))

                }
            }

        }

        /*val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //edtGoldWeight.addTextChangedListener(null);
                if (s.length != 0 && s.toString() != ".") {
                    amount = edtAmount!!.text.toString().toDouble()
                    //                    result = amount / Double.parseDouble(todayGoldRate);
                    result = amount / todayGoldRateWithGst.toDouble()
                    if (result != 0.00) {
                        txtGoldWeight!!.text = "" + DecimalFormat("##.###").format(result) + " gm"
                    }
                } else {
                    txtGoldWeight!!.text = ""
                }
            }


            override fun afterTextChanged(s: Editable) {
                //edtGoldWeight.addTextChangedListener(null);
            }
        }*/

        edtAmount!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length != 0 && s.toString() != ".") {
                    amount = edtAmount!!.text.toString().toDouble()
                    result = amount / todayGoldRateWithGst.toDouble()
                    if (result != 0.00) {
                        txtGoldWeight!!.text = "" + DecimalFormat("##.###").format(result) + " gm"
                    }
                } else {
                    txtGoldWeight!!.text = ""
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        //    edtAmount!!.addTextChangedListener(textWatcher)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.payment_option, android.R.layout.simple_spinner_item
        )
// Specify the layout to use when the list of choices appears
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
// Apply the adapter to the spinner
        // Apply the adapter to the spinner
        spinner_payment_option!!.adapter = adapter
        spinner_payment_option!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    val paymentoption = parent.getItemAtPosition(pos) as String
                    if (paymentoption == "cheque") {
                        payment_option = "Cheque"
                        llCheque!!.visibility = View.VISIBLE
                        llRTGS!!.visibility = View.GONE
                    } else if (paymentoption == "NEFT/RTGS/Online Transfer") {
                        payment_option = "Online"
                        llRTGS!!.visibility = View.VISIBLE
                        llCheque!!.visibility = View.GONE
                    } else if (paymentoption == "Wallet") {
                        payment_option = "Wallet"
                        llCheque!!.visibility = View.GONE
                        llRTGS!!.visibility = View.GONE
                    } else if (paymentoption == "Credit/Debit/Net Banking(Payment Gateway)") {
                        payment_option = "Payumoney"
                        llCheque!!.visibility = View.GONE
                        llRTGS!!.visibility = View.GONE
                    } else if (paymentoption == "UPI Payment") {
                        payment_option = "UPI Payment"
                        llCheque!!.visibility = View.GONE
                        llRTGS!!.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }


        shreiv!!.setOnClickListener {

            val bmpUri: Uri? = getLocalBitmapUri(objbnkdetiv!!)
            if (bmpUri != null) {
                // Construct a ShareIntent with link to image

                // Construct a ShareIntent with link to image
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                shareIntent.type = "image/*"
                // Launch sharing dialog for image
                // Launch sharing dialog for image
                startActivity(Intent.createChooser(shareIntent, "Share VGold Bank Details"))

            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    fun init() {
        progressDialog = TransparentProgressDialog(this@AddGoldActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()

        // comment list as api calling change
        // getTodayGoldRateServiceProvider = GetTodayGoldRateServiceProvider(this)
        // addGoldServiceProvider = AddGoldServiceProvider(this)
        //   loginStatusServiceProvider = LoginStatusServiceProvider(this)
        //   checkLoginSession()
        AttemptToGetTodayGoldRate()

    }

    fun getLocalBitmapUri(imageView: ImageView): Uri? {
        // Extract Bitmap from ImageView drawable
        val drawable = imageView.drawable
        var bmp: Bitmap? = null
        bmp = if (drawable is BitmapDrawable) {
            (imageView.drawable as BitmapDrawable).bitmap
        } else {
            return null
        }

        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {

            val file = File(
                this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            file.parentFile.mkdirs()
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    /*  private fun checkLoginSession() {
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
                                  this@AddGoldActivity,
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
                              this@AddGoldActivity, "Alert", message,
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
                              this@AddGoldActivity,
                              (apiErrorModel as BaseServiceResponseModel).message
                          )
                      } else {
                          PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
                      }
                  } catch (e: Exception) {
                      progressDialog!!.hide()
                      e.printStackTrace()
                      PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
                  } finally {
                      progressDialog!!.hide()
                  }
              }
          })
      }*/

    fun OnClickOfProceedToPayment() {
        if (goldWeight != "0.0" && goldWeight != null) {
            if (payment_option == "Cheque") {
                AttemptToAddGold(
                    VGoldApp.onGetUerId(),
                    goldWeight,
                    "" + amount,
                    payment_option,
                    edtBankDetail!!.text.toString(),
                    "",
                    edtChequeNo!!.text.toString()
                )
            } else if (payment_option == "Online") {
                AttemptToAddGold(
                    VGoldApp.onGetUerId(),
                    goldWeight,
                    "" + amount,
                    payment_option,
                    edtRtgsBankDetail!!.text.toString(),
                    edtTxnId!!.text.toString(),
                    ""
                )
            } else if (payment_option == "Wallet") {
                AttemptToAddGold(
                    VGoldApp.onGetUerId(),
                    goldWeight,
                    "" + amount,
                    payment_option,
                    "",
                    "",
                    ""
                )
            } else if (payment_option == "UPI Payment") {
                integrateGpay(amount, goldWeight)
            } else if (payment_option == "Payumoney") {
                startActivity(
                    Intent(this@AddGoldActivity, PayUMoneyActivity::class.java)
                        .putExtra("AMOUNT", "" + amount)
                        .putExtra("whichActivity", "gold")
                        .putExtra("goldweight", goldWeight)
                )
            } else {
                AlertDialogs().alertDialogOk(
                    this@AddGoldActivity, "Alert", "Please select payment option",
                    resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                )
                //                mAlert.onShowToastNotification(AddGoldActivity.this, "Please select payment option");
            }
        } else {
            AlertDialogs().alertDialogOk(
                this@AddGoldActivity, "Alert", "Please enter vaild Amount",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
            //            mAlert.onShowToastNotification(AddGoldActivity.this, "Please enter vaild Amount");
        }
    }

    private fun integrateGpay(amount: Double, weight: String) {
        var no = "00000"
        if (VGoldApp.onGetNo() != null && !TextUtils.isEmpty(VGoldApp.onGetNo())) {
            no = VGoldApp.onGetNo()!!.substring(0, 5)
        }
        val name: String?
        name = if (VGoldApp.onGetFirst() != null && !TextUtils.isEmpty(VGoldApp.onGetFirst())) {
            if (VGoldApp.onGetLast() != null && !TextUtils.isEmpty(VGoldApp.onGetLast())) {
                VGoldApp.onGetFirst() + " " + VGoldApp.onGetLast()
            } else {
                VGoldApp.onGetFirst()
            }
        } else {
            "NA"
        }
        val transNo = VGoldApp.onGetUerId() + "-" + BaseActivity.date
        val uri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", "9881136531@okbizaxis")
            .appendQueryParameter("pn", "VGold Pvt. Ltd.")
            .appendQueryParameter("mc", "")
            .appendQueryParameter("tr", transNo)
            .appendQueryParameter(
                "tn",
                "GP_ " + weight + "_" + todayGoldRateWithGst + " " + name + "(" + VGoldApp.onGetUerId() + ")"
            )
            .appendQueryParameter("am", amount.toString())
            .appendQueryParameter(
                "cu",
                "INR"
            ) //                        .appendQueryParameter("url", "your-transaction-url")
            .build()


        /*Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "9881136531@okbizaxis")
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "101222")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", transNo)
                .appendQueryParameter("tn", "GP_ " + weight + "_" + todayGoldRateWithGst + " " + name + "(" + VGoldApp.onGetUerId() + ")")
                .appendQueryParameter("am", String.valueOf(amount))
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();*/
        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")
        // check if intent resolves
        if (null != chooser.resolveActivity(packageManager)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            Toast.makeText(
                this@AddGoldActivity,
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }


//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UPI_PAYMENT -> if (RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.e("UPI", "onActivityResult: $trxt")
                    val dataList = ArrayList<String?>()
                    dataList.add(trxt)
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = ArrayList<String?>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null")
                val dataList = ArrayList<String?>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String?>) {
        if (isConnectionAvailable(this@AddGoldActivity)) {
            var str = data[0]
            Log.e("UPIPAY", "upiPaymentDataOperation: $str")
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&").toTypedArray()
            for (i in response.indices) {
                val equalStr = response[i].split("=").toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].lowercase(Locale.getDefault()) == "Status".lowercase(Locale.getDefault())) {
                        status = equalStr[1].lowercase(Locale.getDefault())
                    } else if (equalStr[0].lowercase(Locale.getDefault()) == "ApprovalRefNo".lowercase(
                            Locale.getDefault()
                        ) || equalStr[0].lowercase(Locale.getDefault()) == "txnRef".lowercase(
                            Locale.getDefault()
                        )
                    ) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }
            if (status == "success") {
                //Code to handle successful transaction here.
//                Log.e("UPI", "payment successfull: "+approvalRefNo);
                AttemptToAddGold(
                    VGoldApp.onGetUerId(),
                    goldWeight,
                    "" + amount,
                    payment_option,
                    "",
                    approvalRefNo,
                    ""
                )
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(
                    this@AddGoldActivity,
                    "Payment cancelled by user.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "Cancelled by user: $approvalRefNo")
            } else {
                Toast.makeText(
                    this@AddGoldActivity,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "failed payment: $approvalRefNo")
            }
        } else {
            Log.e("UPI", "Internet issue: ")
            Toast.makeText(
                this@AddGoldActivity,
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected
                && netInfo.isConnectedOrConnecting
                && netInfo.isAvailable
            ) {
                return true
            }
        }
        return false
    }

    fun AttemptToGetTodayGoldRate() {
        //   progressDialog!!.show()
        /*     getTodayGoldRateServiceProvider!!.getTodayGoldRate(VGoldApp.onGetUerId(),
                 object : APICallback {
                     override fun <T> onSuccess(serviceResponse: T) {
                         try {
                             progressDialog!!.hide()
                             val status: String? = (serviceResponse as GetTodayGoldRateModel).status
                             val message: String? = (serviceResponse as GetTodayGoldRateModel).message
                             todayGoldRate =
                                 (serviceResponse as GetTodayGoldRateModel).gold_purchase_rate.toString()
                             todayGoldRateWithGst =
                                 (serviceResponse as GetTodayGoldRateModel).gold_purchase_rate_with_gst.toString()
                             txtGoldRate!!.setText("₹ " + todayGoldRate + "/GM");
                             txtGoldRate!!.text = "₹ $todayGoldRateWithGst/GM"
                             if (status == "200") {
                                 // mAlert.onShowToastNotification(AddGoldActivity.this, message);
                             } else {
                                 AlertDialogs().alertDialogOk(
                                     this@AddGoldActivity,
                                     "Alert",
                                     message,
                                     resources.getString(R.string.btn_ok),
                                     0,
                                     false,
                                     alertDialogOkListener
                                 )
                                 //                        mAlert.onShowToastNotification(AddGoldActivity.this, message);
                             }
                         } catch (e: Exception) {
                             progressDialog!!.hide()
                             e.printStackTrace()
                         } finally {
                             progressDialog!!.hide()
                         }
                     }

                     override fun <T> onFailure(apiErrorModel: T, extras: T) {

                         try {
                             progressDialog!!.hide()
                             if (apiErrorModel != null) {
     //                        PrintUtil.showToast(
     //                            this@AddGoldActivity,
     //                            (apiErrorModel as BaseServiceResponseModel).message
     //                        )
                             } else {
                                 PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
                             }
                         } catch (e: Exception) {
                             progressDialog!!.hide()
                             e.printStackTrace()
                             PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
                         } finally {
                             progressDialog!!.hide()
                         }
                     }

                 })*/

        // change in api calling

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", VGoldApp.onGetUerId())
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_purchase_rate.php")
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
                val resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    val json = JSONObject(resp)
                    val status = json.get("status").toString()
                    if (status == "200") {
                        Log.e(" Response", resp)

                        runOnUiThread {
                            val jsonString: String = resp //http request
                            var dataValue = GetTodayGoldRateModel()
                            val gson = Gson()
                            dataValue =
                                gson.fromJson(jsonString, GetTodayGoldRateModel::class.java)

                            val status: String? = dataValue.status
                            val message: String? =
                                dataValue.message
                            todayGoldRate =
                                dataValue.gold_purchase_rate.toString()
                            todayGoldRateWithGst =
                                dataValue.gold_purchase_rate_with_gst.toString()
                            txtGoldRate!!.setText("₹ " + todayGoldRate + "/GM");
                            txtGoldRate!!.text = "₹ $todayGoldRateWithGst/GM"
                        }

                    } else {
                        runOnUiThread {
                            PrintUtil.showToast(
                                this@AddGoldActivity,
                                json.getString("Message").toString()
                            )
                        }
                    }
                }
            }
        })

    }


    private fun AttemptToAddGold(
        user_id: String?,
        gold: String,
        amount: String,
        payment_option: String,
        bank_details: String,
        tr_id: String,
        cheque_no: String
    ) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        /* addGoldServiceProvider!!.getAddBankDetails(
             user_id,
             gold,
             amount,
             payment_option,
             bank_details,
             tr_id,
             cheque_no,
             object : APICallback {
                 override fun <T> onSuccess(serviceResponse: T) {
                     try {
                         val status = (serviceResponse as AddGoldModel).getStatus()
                         val message = (serviceResponse as AddGoldModel).getMessage()
                         if (status == "200") {
                             succesMsg = message

                                       mAlert.onShowToastNotification(AddGoldActivity.this, message);
                             val intent = Intent(this@AddGoldActivity, SuccessActivity::class.java)
                             intent.putExtra("message", message)
                             startActivity(intent)
                             finish()
                         } else {
                             AlertDialogs().alertDialogOk(
                                 this@AddGoldActivity,
                                 "Alert",
                                 message,
                                 resources.getString(R.string.btn_ok),
                                 0,
                                 false,
                                 alertDialogOkListener
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
                             PrintUtil.showToast(
                                 this@AddGoldActivity,
                                 (apiErrorModel as BaseServiceResponseModel).message
                             )
                         } else {
                             PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
                         }
                     } catch (e: Exception) {
                         e.printStackTrace()
                         PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
                     } finally {
                         progressDialog!!.hide()
                     }
                 }
             })
 */


        //change in api calling

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", user_id)
            .addFormDataPart("gold", gold)
            .addFormDataPart("amount", amount)
            .addFormDataPart("payment_option", payment_option)
            .addFormDataPart("bank_details", bank_details)
            .addFormDataPart("tr_id", tr_id)
            .addFormDataPart("cheque_no", cheque_no)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/add_gold.php")
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
                val resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    val json = JSONObject(resp)
                    val status = json.get("status").toString()
                    runOnUiThread {
                        if (status == "200") {
                            succesMsg = json.getString("Message")
                            /* AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", message,
                            getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);*/

//                        mAlert.onShowToastNotification(AddGoldActivity.this, message);
                            val intent = Intent(this@AddGoldActivity, SuccessActivity::class.java)
                            intent.putExtra("message", succesMsg)
                            startActivity(intent)
                            finish()

                        } else {
                            PrintUtil.showToast(
                                this@AddGoldActivity,
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
            1 -> {
                val intent = Intent(this@AddGoldActivity, SuccessActivity::class.java)
                intent.putExtra("message", succesMsg)
                startActivity(intent)
                finish()
            }
            11 -> {
                val LogIntent = Intent(this@AddGoldActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}


//        sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
//
//        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()
//
//
//        tvgoldRate = findViewById(R.id.txtGoldRate)
//        txtGoldWeight = findViewById(R.id.txtGoldWeight)
//        edtAmt = findViewById(R.id.edtAmount)
//        edtBnkDet = findViewById(R.id.edtBankDetail)
//        edtChqNo = findViewById(R.id.edtChequeNo)
//        rtgsBnkDet = findViewById(R.id.edtRtgsBankDetail)
//        transnId = findViewById(R.id.edtTxnId)
//        btnProcedtoPay = findViewById(R.id.btnProceedToPayment)
//        spiPayOption = findViewById(R.id.spinner_payment_option)
//        llchq = findViewById(R.id.llCheque)
//        llRtgs = findViewById(R.id.llRTGS)
//
//
//        supportActionBar?.title = "Add Gold "
//        init()
//
//        val textWatcher: TextWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                //edtGoldWeight.addTextChangedListener(null);
//                if (s.length != 0 && s.toString() != ".") {
//                    amount = edtAmt.getText().toString().toDouble()
//                    //                    result = amount / Double.parseDouble(todayGoldRate);
//                    result = amount / todayGoldRateWithGst.toDouble()
//                    if (result != 0.00) {
//                        goldWeight = "" + DecimalFormat("##.###").format(result)
//                        txtGoldWeight.setText("" + DecimalFormat("##.###").format(result) + " gm")
//                    }
//                } else {
//                    txtGoldWeight.setText("")
//                }
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                //edtGoldWeight.addTextChangedListener(null);
//            }
//        }
//
//
//        edtAmt.addTextChangedListener(textWatcher)
//
//        val adapter = ArrayAdapter.createFromResource(
//            this,
//            R.array.payment_option, android.R.layout.simple_spinner_item
//        )
//// Specify the layout to use when the list of choices appears
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
//// Apply the adapter to the spinner
//        // Apply the adapter to the spinner
//        spiPayOption.setAdapter(adapter)
//        spiPayOption.setOnItemSelectedListener(object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
//                val paymentoption = parent.getItemAtPosition(pos) as String
//                if (paymentoption == "cheque") {
//                    payment_option = "Cheque"
//                    llchq.setVisibility(View.VISIBLE)
//                    llRtgs.setVisibility(View.GONE)
//                } else if (paymentoption == "NEFT/RTGS/Online Transfer") {
//                    payment_option = "Online"
//                    llRtgs.setVisibility(View.VISIBLE)
//                    llchq.setVisibility(View.GONE)
//                } else if (paymentoption == "Wallet") {
//                    payment_option = "Wallet"
//                    llchq.setVisibility(View.GONE)
//                    llRtgs.setVisibility(View.GONE)
//                } else if (paymentoption == "Credit/Debit/Net Banking(Payment Gateway)") {
//                    payment_option = "Payumoney"
//                    llchq.setVisibility(View.GONE)
//                    llRtgs.setVisibility(View.GONE)
//                } else if (paymentoption == "UPI Payment") {
//                    payment_option = "UPI Payment"
//                    llchq.setVisibility(View.GONE)
//                    llRtgs.setVisibility(View.GONE)
//                }
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//        })
//
//
//        btnProcedtoPay.setOnClickListener {
//            OnClickOfProceedToPayment()
////            if (edtAmt.text.isEmpty()) {
////                edtAmt.error = "Please Enter the Ammount !"
////                edtAmt.requestFocus()
//////            } else if (spiPayOption.isEmpty()) {
//////                spiPayOption.error = "Please Enter the account Number !"
//////
//////                spiPayOption.requestFocus()
////
////            } else {
////                init()
////            }
//        }
//    }
//
////        fun AddGold() {
////        val client = OkHttpClient().newBuilder().build()
////        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
////            .addFormDataPart("user_id", userId)
////            .addFormDataPart("gold", tvgoldRate.text.toString())
////
////
////            .build()
////        val request = okhttp3.Request.Builder()
////            .url("https://www.vgold.co.in/dashboard/webservices/add_gold.php?")
//////            .header("AUTHORIZATION", "Bearer $token")
////            .header("Accept", "application/json")
//////            .header("Content-Type", "application/json")
////            .post(requestBody)
////            .build()
////        client.newCall(request).enqueue(object : okhttp3.Callback {
////            override fun onFailure(call: Call, e: IOException) {
////                val mMessage = e.message.toString()
////                e.printStackTrace()
////                Log.e("failure Response", mMessage)
////            }
////
////            override fun onResponse(call: Call, response: okhttp3.Response) {
////                val mMessage = response.body()!!.string()
////                if (!response.isSuccessful) {
////                    throw IOException("Unexpected code" + response)
////                } else {
//////                    Log.d("Upload Status", "Image Uploaded Successfully !")
////                    //runOnUiThread(Runnable {
////                    val intent = Intent(this@AddGoldActivity, SuccessActivity::class.java)
////
////                    // intent.putExtra("email_id", edtEmail.text.toString())
////                    startActivity(intent)
////                    // Toast.makeText(this@LoginActivity, ".$mMessage", Toast.LENGTH_SHORT).show()
////
////                }
////            }
////        })
////    }
//
//
//    fun init() {
//        progressDialog = TransparentProgressDialog(this@AddGoldActivity)
//        progressDialog!!.setCancelable(false)
//        setFinishOnTouchOutside(false)
//        mAlert = AlertDialogs().getInstance()
//        getTodayGoldRateServiceProvider = GetTodayGoldRateServiceProvider(this)
//        addGoldServiceProvider = AddGoldServiceProvider()
//        loginStatusServiceProvider = LoginStatusServiceProvider(this)
//        checkLoginSession()
//        AttemptToGetTodayGoldRate()
//    }
//
//    private fun checkLoginSession() {
//        loginStatusServiceProvider?.getLoginStatus(VGoldApp.onGetUerId(), object : APICallback {
//            override fun <T> onSuccess(serviceResponse: T) {
//                try {
//                    progressDialog?.hide()
//                    val status: String? = (serviceResponse as LoginSessionModel).getStatus()
//                    val message: String? = (serviceResponse as LoginSessionModel).getMessage()
//                    val data: Boolean = (serviceResponse as LoginSessionModel).getData() == true
//                    Log.i("TAG", "onSuccess: $status")
//                    Log.i("TAG", "onSuccess: $message")
//                    if (status == "200") {
//                        if (!data) {
//                            AlertDialogs().alertDialogOk(
//                                this@AddGoldActivity,
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
//                            this@AddGoldActivity, "Alert", message,
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
//
//                try {
//                    progressDialog?.hide()
//                    if (apiErrorModel != null) {
//                        PrintUtil.showToast(
//                            this@AddGoldActivity,
//                            (apiErrorModel as BaseServiceResponseModel).message
//                        )
//                    } else {
//                        PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
//                    }
//                } catch (e: Exception) {
//                    progressDialog?.hide()
//                    e.printStackTrace()
//                    PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
//                } finally {
//                    progressDialog?.hide()
//                }
//            }
//        })
//    }
//
//
//    fun OnClickOfProceedToPayment() {
//        if (goldWeight != "0.0" && goldWeight != null) {
//            if (payment_option == "Cheque") {
//                AttemptToAddGold(
//                    VGoldApp.onGetUerId(),
//                    goldWeight,
//                    "" + amount,
//                    payment_option,
//                    edtBnkDet.getText().toString(),
//                    "",
//                    edtChqNo.getText().toString()
//                )
//            } else if (payment_option == "Online") {
//                AttemptToAddGold(
//                    VGoldApp.onGetUerId(),
//                    goldWeight,
//                    "" + amount,
//                    payment_option,
//                    rtgsBnkDet.getText().toString(),
//                    transnId.getText().toString(),
//                    ""
//                )
//            } else if (payment_option == "Wallet") {
//                AttemptToAddGold(
//                    VGoldApp.onGetUerId(),
//                    goldWeight,
//                    "" + amount,
//                    payment_option,
//                    "",
//                    "",
//                    ""
//                )
//            } else if (payment_option == "UPI Payment") {
//                integrateGpay(amount, goldWeight)
//            } else if (payment_option == "Payumoney") {
//                startActivity(
//                    Intent(this@AddGoldActivity, PayUMoneyActivity::class.java)
//                        .putExtra("AMOUNT", "" + amount)
//                        .putExtra("whichActivity", "gold")
//                        .putExtra("goldweight", goldWeight)
//                )
//            } else {
//                AlertDialogs().alertDialogOk(
//                    this@AddGoldActivity, "Alert", "Please select payment option",
//                    resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
//                )
//                //                mAlert.onShowToastNotification(AddGoldActivity.this, "Please select payment option");
//            }
//        } else {
//            AlertDialogs().alertDialogOk(
//                this@AddGoldActivity, "Alert", "Please enter vaild Amount",
//                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
//            )
//            //            mAlert.onShowToastNotification(AddGoldActivity.this, "Please enter vaild Amount");
//        }
//    }
//
//    private fun integrateGpay(amount: Double, weight: String) {
//        var no = "00000"
//        if (VGoldApp.onGetNo() != null && !TextUtils.isEmpty(VGoldApp.onGetNo())) {
//            no = VGoldApp.onGetNo()!!.substring(0, 5)
//        }
//        val name: String?
//        name = if (VGoldApp.onGetFirst() != null && !TextUtils.isEmpty(VGoldApp.onGetFirst())) {
//            if (VGoldApp.onGetLast() != null && !TextUtils.isEmpty(VGoldApp.onGetLast())) {
//                VGoldApp.onGetFirst() + " " + VGoldApp.onGetLast()
//            } else {
//                VGoldApp.onGetFirst()
//            }
//        } else {
//            "NA"
//        }
//        val transNo = VGoldApp.onGetUerId() + "-" + BaseActivity.date
//        val uri = Uri.Builder()
//            .scheme("upi")
//            .authority("pay")
//            .appendQueryParameter("pa", "9881136531@okbizaxis")
//            .appendQueryParameter("pn", "VGold Pvt. Ltd.")
//            .appendQueryParameter("mc", "")
//            .appendQueryParameter("tr", transNo)
//            .appendQueryParameter(
//                "tn",
//                "GP_ " + weight + "_" + todayGoldRateWithGst + " " + name + "(" + VGoldApp.onGetUerId() + ")"
//            )
//            .appendQueryParameter("am", amount.toString())
//            .appendQueryParameter(
//                "cu",
//                "INR"
//            ) //                        .appendQueryParameter("url", "your-transaction-url")
//            .build()
//
//
//        /*Uri uri = Uri.parse("upi://pay").buildUpon()
//                .appendQueryParameter("pa", "9881136531@okbizaxis")
//                .appendQueryParameter("pn", name)
//                .appendQueryParameter("mc", "101222")
//                //.appendQueryParameter("tid", "02125412")
//                .appendQueryParameter("tr", transNo)
//                .appendQueryParameter("tn", "GP_ " + weight + "_" + todayGoldRateWithGst + " " + name + "(" + VGoldApp.onGetUerId() + ")")
//                .appendQueryParameter("am", String.valueOf(amount))
//                .appendQueryParameter("cu", "INR")
//                //.appendQueryParameter("refUrl", "blueapp")
//                .build();*/
//        val upiPayIntent = Intent(Intent.ACTION_VIEW)
//        upiPayIntent.data = uri
//        val chooser = Intent.createChooser(upiPayIntent, "Pay with")
//        // check if intent resolves
//        if (null != chooser.resolveActivity(packageManager)) {
//            startActivityForResult(chooser, UPI_PAYMENT)
//        } else {
//            Toast.makeText(
//                this@AddGoldActivity,
//                "No UPI app found, please install one to continue",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//
////        Intent intent = new Intent(Intent.ACTION_VIEW);
////        intent.setData(uri);
////        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
////        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            UPI_PAYMENT -> if (RESULT_OK == resultCode || resultCode == 11) {
//                if (data != null) {
//                    val trxt = data.getStringExtra("response")
//                    Log.e("UPI", "onActivityResult: $trxt")
//                    val dataList = ArrayList<String?>()
//                    dataList.add(trxt)
//                    upiPaymentDataOperation(dataList)
//                } else {
//                    Log.e("UPI", "onActivityResult: " + "Return data is null")
//                    val dataList = ArrayList<String?>()
//                    dataList.add("nothing")
//                    upiPaymentDataOperation(dataList)
//                }
//            } else {
//                //when user simply back without payment
//                Log.e("UPI", "onActivityResult: " + "Return data is null")
//                val dataList = ArrayList<String?>()
//                dataList.add("nothing")
//                upiPaymentDataOperation(dataList)
//            }
//        }
//    }
//
//    private fun upiPaymentDataOperation(data: ArrayList<String?>) {
//        if (isConnectionAvailable(this@AddGoldActivity)) {
//            var str = data[0]
//            Log.e("UPIPAY", "upiPaymentDataOperation: $str")
//            var paymentCancel = ""
//            if (str == null) str = "discard"
//            var status = ""
//            var approvalRefNo = ""
//            val response = str.split("&").toTypedArray()
//            for (i in response.indices) {
//                val equalStr = response[i].split("=").toTypedArray()
//                if (equalStr.size >= 2) {
//                    if (equalStr[0].lowercase(Locale.getDefault()) == "Status".lowercase(Locale.getDefault())) {
//                        status = equalStr[1].lowercase(Locale.getDefault())
//                    } else if (equalStr[0].lowercase(Locale.getDefault()) == "ApprovalRefNo".lowercase(
//                            Locale.getDefault()
//                        ) || equalStr[0].lowercase(Locale.getDefault()) == "txnRef".lowercase(
//                            Locale.getDefault()
//                        )
//                    ) {
//                        approvalRefNo = equalStr[1]
//                    }
//                } else {
//                    paymentCancel = "Payment cancelled by user."
//                }
//            }
//            if (status == "success") {
//                //Code to handle successful transaction here.
////                Log.e("UPI", "payment successfull: "+approvalRefNo);
//                AttemptToAddGold(
//                    VGoldApp.onGetUerId(),
//                    goldWeight,
//                    "" + amount,
//                    payment_option,
//                    "",
//                    approvalRefNo,
//                    ""
//                )
//            } else if ("Payment cancelled by user." == paymentCancel) {
//                Toast.makeText(
//                    this@AddGoldActivity,
//                    "Payment cancelled by user.",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Log.e("UPI", "Cancelled by user: $approvalRefNo")
//            } else {
//                Toast.makeText(
//                    this@AddGoldActivity,
//                    "Transaction failed.Please try again",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Log.e("UPI", "failed payment: $approvalRefNo")
//            }
//        } else {
//            Log.e("UPI", "Internet issue: ")
//            Toast.makeText(
//                this@AddGoldActivity,
//                "Internet connection is not available. Please check and try again",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//    fun isConnectionAvailable(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (connectivityManager != null) {
//            val netInfo = connectivityManager.activeNetworkInfo
//            if (netInfo != null && netInfo.isConnected
//                && netInfo.isConnectedOrConnecting
//                && netInfo.isAvailable
//            ) {
//                return true
//            }
//        }
//        return false
//    }
//
//    private fun AttemptToGetTodayGoldRate() {
//        progressDialog?.show()
//
//        getTodayGoldRateServiceProvider?.getTodayGoldRate(object : APICallback {
//            override fun <T> onSuccess(serviceResponse: T) {
//                try {
//                    progressDialog?.hide()
//                    val status: String? = (serviceResponse as GetTodayGoldRateModel).status
//                    val message: String? = (serviceResponse as GetTodayGoldRateModel).message
//                    todayGoldRate =
//                        (serviceResponse as GetTodayGoldRateModel).gold_purchase_rate.toString()
//                    todayGoldRateWithGst =
//                        (serviceResponse as GetTodayGoldRateModel).gold_purchase_rate_with_gst.toString()
//                    //                    txtGoldRate.setText("₹ " + todayGoldRate + "/GM");
//                    tvgoldRate.setText("₹ $todayGoldRateWithGst/GM")
//                    if (status == "200") {
//                        // mAlert.onShowToastNotification(AddGoldActivity.this, message);
//                    } else {
//                        AlertDialogs().alertDialogOk(
//                            this@AddGoldActivity, "Alert", message,
//                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
//                        )
//                        //                        mAlert.onShowToastNotification(AddGoldActivity.this, message);
//                    }
//
//                } catch (e: Exception) {
//                    progressDialog?.hide()
//                    e.printStackTrace()
//                } finally {
//                    progressDialog?.hide()
//                }
//            }
//
//            override fun <T> onFailure(apiErrorModel: T, extras: T) {
//
//                try {
//                    progressDialog?.hide()
//                    if (apiErrorModel != null) {
//                        PrintUtil.showToast(
//                            this@AddGoldActivity,
//                            (apiErrorModel as BaseServiceResponseModel).message
//                        )
//                    } else {
//                        PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
//                    }
//                } catch (e: Exception) {
//                    progressDialog?.hide()
//                    e.printStackTrace()
//                    PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
//                } finally {
//                    progressDialog?.hide()
//                }
//            }
//        })
//    }
//
//    private fun AttemptToAddGold(
//        user_id: String?,
//        gold: String,
//        amount: String,
//        payment_option: String,
//        bank_details: String,
//        tr_id: String,
//        cheque_no: String
//    ) {
//        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
//        addGoldServiceProvider?.getAddBankDetails(
//            user_id,
//            gold,
//            amount,
//            payment_option,
//            bank_details,
//            tr_id,
//            cheque_no,
//            object : APICallback {
//                override fun <T> onSuccess(serviceResponse: T) {
//                    try {
//                        val status: String? = (serviceResponse as AddGoldModel).getStatus()
//                        val message: String? = (serviceResponse as AddGoldModel).getMessage()
//                        if (status == "200") {
//                            succesMsg = message
//
//                            /* AlertDialogs.alertDialogOk(AddGoldActivity.this, "Alert", message,
//                                    getResources().getString(R.string.btn_ok), 1, false, alertDialogOkListener);*/
//
//    //                        mAlert.onShowToastNotification(AddGoldActivity.this, message);
//                            val intent = Intent(this@AddGoldActivity, SuccessActivity::class.java)
//                            intent.putExtra("message", message)
//                            startActivity(intent)
//                            finish()
//                        } else {
//                            AlertDialogs().alertDialogOk(
//                                this@AddGoldActivity,
//                                "Alert",
//                                message,
//                                resources.getString(R.string.btn_ok),
//                                0,
//                                false,
//                                alertDialogOkListener
//                            )
//                            /* mAlert.onShowToastNotification(AddGoldActivity.this, message);
//                            Intent intent = new Intent(AddGoldActivity.this, MainActivity.class);
//                            startActivity(intent);*/
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    } finally {
//                        progressDialog?.hide()
//                    }
//                }
//
//                override fun <T> onFailure(apiErrorModel: T, extras: T) {
//
//                    try {
//                        if (apiErrorModel != null) {
//                            PrintUtil.showToast(
//                                this@AddGoldActivity,
//                                (apiErrorModel as BaseServiceResponseModel).message
//                            )
//                        } else {
//                            PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        PrintUtil.showNetworkAvailableToast(this@AddGoldActivity)
//                    } finally {
//                        progressDialog?.hide()
//                    }
//                }
//            })
//    }
//
//    override fun onDialogOk(resultCode: Int) {
//        when (resultCode) {
//            1 -> {
//                val intent = Intent(this@AddGoldActivity, SuccessActivity::class.java)
//                intent.putExtra("message", succesMsg)
//                startActivity(intent)
//                finish()
//            }
//            11 -> {
//                val LogIntent = Intent(this@AddGoldActivity, LoginActivity::class.java)
//                startActivity(LogIntent)
//                finish()
//            }
//        }
//    }
//}
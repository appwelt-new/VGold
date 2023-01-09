package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.FileProvider
import butterknife.ButterKnife
import com.cognifygroup.vgold.BuildConfig
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.goldbookingrequest.GoldBookingRequestModel
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.*
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.bumptech.glide.Glide
import com.cognifygroup.vgold.utilities.Constants
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

class BookingDetailsActivity : AppCompatActivity(), AlertDialogOkListener {
//    private lateinit var tvBookVal: TextView
//    private lateinit var tvGoldRate: TextView
//    private lateinit var tvDownPayment: TextView
//    private lateinit var tvInitBCharge: TextView
//    private lateinit var tvDiscount: TextView
//    private lateinit var tvdiscBookCharge: TextView
//    private lateinit var tvMonthInstalment: TextView
//    private lateinit var spPayOption: Spinner
//    private lateinit var btnPayOnline: Button
//    private lateinit var edtBankDet: EditText
//    private lateinit var edtChqNo: EditText
//    private lateinit var llchq: LinearLayout
//    private lateinit var llRtgs: LinearLayout
//    private lateinit var sharedPreferences: SharedPreferences

    var shreiv: ImageView? = null
    var bnkimg: ImageView? = null
    var txtBookingValue: TextView? = null
    var txtGoldRate: TextView? = null

    var txtDownPayment: TextView? = null
    var txtBookingCharge: TextView? = null

    var txtMonthly: TextView? = null

    var spinner_payment_option: Spinner? = null

    var llCheque: LinearLayout? = null
    var llRTGS: LinearLayout? = null

    var btnPayOnline: Button? = null

    var edtBankDetail: EditText? = null

    var edtChequeNo: EditText? = null
    var edtRtgsBankDetail: EditText? = null
    var edtTxnId: EditText? = null
    var txtInitBookingCharge: TextView? = null
    var txtBookingChargeDisc: TextView? = null
    var initChargeLbl: TextView? = null

    var discLbl: TextView? = null

    var calculationLayout: LinearLayoutCompat? = null

    var txtPayableAmtId: TextView? = null

    var txtWalletAmtId: TextView? = null
    var txtSaleRateId: TextView? = null
    var txtGSTAmtId: TextView? = null
    var txtGSTPayableAmtId: TextView? = null

    var txtDeductedGoldId: TextView? = null
    var txtBalanceRemainId: TextView? = null

    var remainWalletAmt = 0.0
    private var GoldAmt: String? = null

    var payment_option: String? = null
    var monthly: String? = null
    var booking_value: String? = null
    var down_payment: String? = null
    var gold_rate: String? = null
    var quantity: String? = null
    var tennure: String? = null
    var pc: String? = null
    var initBookingCharge: String? = null
    var disc: String? = null
    var booking_charge: String? = null
    val UPI_PAYMENT = 0

    var objbnkdetiv: ImageView? = null

    //   var getAllTransactionMoneyServiceProvider: GetAllTransactionMoneyServiceProvider? = null
    //  var getAllTransactionGoldServiceProvider: GetAllTransactionGoldServiceProvider? = null
    // var getTodayGoldRateServiceProvider: GetTodayGoldRateServiceProvider? = null

    var GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    var GOOGLE_PAY_REQUEST_CODE = 123
    private var moneyWalletBal: String? = null

    var mAlert: AlertDialogs? = null

    //   var goldBookingRequestServiceProvider: GoldBookingRequestServiceProvider? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
//    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    private var dataModel: GoldBookingRequestModel.Data? = null

    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)
        ButterKnife.inject(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.title = "Booking Details"

        sharedPreferences =
            this@BookingDetailsActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()

        shreiv = findViewById(R.id.shreiv)
        objbnkdetiv = findViewById(R.id.bnkdetiv)
        txtBookingValue = findViewById(R.id.txtBookingValue)
        txtBalanceRemainId = findViewById(R.id.txtBalanceRemainId)
        txtDeductedGoldId = findViewById(R.id.txtDeductedGoldId)
        txtGSTPayableAmtId = findViewById(R.id.txtGSTPayableAmtId)
        txtGSTAmtId = findViewById(R.id.txtGSTAmtId)
        txtSaleRateId = findViewById(R.id.txtSaleRateId)
        txtWalletAmtId = findViewById(R.id.txtWalletAmtId)
        txtPayableAmtId = findViewById(R.id.txtPayableAmtId)
        calculationLayout = findViewById(R.id.calculationLayout)
        discLbl = findViewById(R.id.discLbl)
        initChargeLbl = findViewById(R.id.initChargeLbl)
        txtBookingChargeDisc = findViewById(R.id.txtBookingChargeDisc)
        txtInitBookingCharge = findViewById(R.id.txtInitBookingCharge)
        edtTxnId = findViewById(R.id.edtTxnId)
        edtRtgsBankDetail = findViewById(R.id.edtRtgsBankDetail)
        edtChequeNo = findViewById(R.id.edtChequeNo)
        edtBankDetail = findViewById(R.id.edtBankDetail)
        btnPayOnline = findViewById(R.id.btnPayOnline)
        llRTGS = findViewById(R.id.llRTGS)
        llCheque = findViewById(R.id.llCheque)
        spinner_payment_option = findViewById(R.id.spinner_payment_option)
        txtMonthly = findViewById(R.id.txtMonthly)
        txtBookingCharge = findViewById(R.id.txtBookingCharge)

        txtDownPayment = findViewById(R.id.txtDownPayment)
        txtGoldRate = findViewById(R.id.txtGoldRate)

        Glide.with(this)
            .load("https://www.vgold.co.in/dashboard/vgold_rate/bank%20details.png")
            .into(objbnkdetiv!!);

        init()

        btnPayOnline!!.setOnClickListener {
            onClickOfBtnPayOnline()
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
                this@BookingDetailsActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            file.parentFile.mkdirs()
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(
                this@BookingDetailsActivity,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun init() {
        progressDialog = TransparentProgressDialog(this@BookingDetailsActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        //  goldBookingRequestServiceProvider = GoldBookingRequestServiceProvider(this)
        // getAllTransactionMoneyServiceProvider = GetAllTransactionMoneyServiceProvider(this)
        // getAllTransactionGoldServiceProvider = GetAllTransactionGoldServiceProvider(this)
        // getTodayGoldRateServiceProvider = GetTodayGoldRateServiceProvider(this)
        if (intent.hasExtra("monthly")) {
            monthly = intent.getStringExtra("monthly")
            booking_value = intent.getStringExtra("booking_value")
            down_payment = intent.getStringExtra("down_payment")
            gold_rate = intent.getStringExtra("gold_rate")
            booking_charge = intent.getStringExtra("booking_charge")
            quantity = intent.getStringExtra("quantity")
            tennure = intent.getStringExtra("tennure")
            pc = intent.getStringExtra("pc")
            initBookingCharge = intent.getStringExtra("initBookingCharge")
            disc = intent.getStringExtra("disc")
            txtMonthly!!.text = monthly
            txtBookingValue!!.text = booking_value
            txtDownPayment!!.text = down_payment
            txtBookingCharge!!.text = booking_charge
            txtGoldRate!!.text = gold_rate
            if (disc.equals("0", ignoreCase = true)) {
                txtBookingChargeDisc!!.visibility = View.GONE
                txtInitBookingCharge!!.visibility = View.GONE
                initChargeLbl!!.visibility = View.GONE
                discLbl!!.visibility = View.GONE
            } else {
                txtBookingChargeDisc!!.visibility = View.VISIBLE
                txtInitBookingCharge!!.visibility = View.VISIBLE
                initChargeLbl!!.visibility = View.VISIBLE
                discLbl!!.visibility = View.VISIBLE
                txtBookingChargeDisc!!.text = disc
                txtInitBookingCharge!!.text = initBookingCharge
            }
            val adapter = ArrayAdapter.createFromResource(
                this,
                R.array.payment_option_wallet, android.R.layout.simple_spinner_item
            )
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
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
                            calculationLayout!!.visibility = View.GONE
                            llCheque!!.visibility = View.VISIBLE
                            llRTGS!!.visibility = View.GONE
                        } else if (paymentoption == "NEFT/RTGS/Online Transfer") {
                            payment_option = "Online"
                            calculationLayout!!.visibility = View.GONE
                            llRTGS!!.visibility = View.VISIBLE
                            llCheque!!.visibility = View.GONE
                        } else if (paymentoption == "Gold Wallet") {
                            payment_option = "Gold Wallet"
                            calculationLayout!!.visibility = View.VISIBLE
                            llCheque!!.visibility = View.GONE
                            llRTGS!!.visibility = View.GONE
                            AttemptToGoldBookingRequest(
                                userId,
                                booking_value!!, down_payment!!,
                                monthly!!, gold_rate!!, quantity!!, tennure!!,
                                pc!!, payment_option!!,
                                edtRtgsBankDetail!!.text.toString(), edtTxnId!!.text.toString(), "",
                                initBookingCharge!!, disc!!, booking_charge!!, "0"
                            )
                        } else if (paymentoption == "Money Wallet") {
                            calculateMoneyAmount(
                                down_payment!!.toDouble() + txtBookingCharge!!.text.toString()
                                    .trim { it <= ' ' }
                                    .toDouble())
                            payment_option = "Money Wallet"
                            calculationLayout!!.visibility = View.VISIBLE
                            llCheque!!.visibility = View.GONE
                            llRTGS!!.visibility = View.GONE
                        } else if (paymentoption == "UPI Payment") {
                            calculationLayout!!.visibility = View.GONE
                            payment_option = "UPI Payment"
                            llCheque!!.visibility = View.GONE
                            llRTGS!!.visibility = View.GONE
                        }
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {}
                }
            //  loginStatusServiceProvider = LoginStatusServiceProvider(this)
            // checkLoginSession()
            AttemptToGetMoneyBalance(userId)
            AttemptToGetGoldBalance(userId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calculateMoneyAmount(payableAmt: Double) {
        if (moneyWalletBal != null && !TextUtils.isEmpty(moneyWalletBal) && !moneyWalletBal.equals(
                "null",
                ignoreCase = true
            )
        ) {
            remainWalletAmt = moneyWalletBal!!.toDouble() - payableAmt
            if (remainWalletAmt > 0) {
                txtWalletAmtId!!.text = String.format("%.2f", remainWalletAmt)
            } else {
                AlertDialogs().alertDialogOk(
                    this@BookingDetailsActivity, "Alert", "Low balance in money wallet",
                    resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                )
            }
        }
        calculationLayout!!.visibility = View.VISIBLE
        txtGSTAmtId!!.visibility = View.GONE
        txtGSTPayableAmtId!!.visibility = View.GONE
        txtDeductedGoldId!!.visibility = View.GONE
        txtSaleRateId!!.visibility = View.GONE
        txtPayableAmtId!!.text = ("Payable Amount : " + resources.getString(R.string.rs)
                + payableAmt)
        txtWalletAmtId!!.text = ("Money in Wallet : " + resources.getString(R.string.rs)
                + moneyWalletBal)
        txtBalanceRemainId!!.text = ("Remaining Money in Wallet : "
                + resources.getString(R.string.rs) + remainWalletAmt)
    }

    private fun AttemptToGetMoneyBalance(user_id: String?) {
        /*  progressDialog!!.show()
          getAllTransactionMoneyServiceProvider!!.getAllTransactionMoneyHistory(
              user_id,
              object : APICallback {
                  override fun <T> onSuccess(serviceResponse: T) {
                      try {
                          val status: String? =
                              (serviceResponse as GetAllTransactionMoneyModel).status
                          val message: String? =
                              (serviceResponse as GetAllTransactionMoneyModel).message
                          moneyWalletBal =
                              (serviceResponse as GetAllTransactionMoneyModel).wallet_Balance

  //                    txtWalletRupee.setText(getResources().getString(R.string.rs));
  //                    txtWalletAmount.setText(moneyWalletBal);
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
                                  this@BookingDetailsActivity,
                                  (apiErrorModel as BaseServiceResponseModel).message
                              )
                          } else {
                              PrintUtil.showNetworkAvailableToast(this@BookingDetailsActivity)
                          }
                      } catch (e: Exception) {
                          e.printStackTrace()
                          PrintUtil.showNetworkAvailableToast(this@BookingDetailsActivity)
                      } finally {
                          progressDialog!!.hide()
                      }
                  }
              })

  */
        // change in api calling
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
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

            override fun onResponse(call: Call, response: okhttp3.Response) {
                var resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(resp)
                    var status = json.get("status").toString()
                    if (status.equals("200"))
                        runOnUiThread {
                            moneyWalletBal = json.optString("Wallet_Balance")
                        }
                }
            }
        })
    }

    private fun AttemptToGetGoldBalance(user_id: String?) {
        //  progressDialog!!.show()
        /*getAllTransactionGoldServiceProvider!!.getAllTransactionGoldHistory(
            user_id,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? =
                            (serviceResponse as GetAllTransactionGoldModel).status
                        val message: String? =
                            (serviceResponse as GetAllTransactionGoldModel).message
                        val balance: String? =
                            (serviceResponse as GetAllTransactionGoldModel).gold_Balance
                        var gold = balance?.toDouble()
                        val numberFormat = DecimalFormat("#.000")
                        gold = numberFormat.format(gold).toDouble()
                        //                    txtGoldAmount.setText(gold + " GM");
                        AttemptToGetTodayGoldRate(gold)
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
                                this@BookingDetailsActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@BookingDetailsActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@BookingDetailsActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })*/

        // change in api calling
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/gold_wallet_transactions.php")
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
                    if (status.equals("200"))
                        runOnUiThread {
                            val balance: String? =
                                json.optString("gold_Balance")
                            var gold = balance?.toDouble()
                            val numberFormat = DecimalFormat("#.000")
                            gold = numberFormat.format(gold).toDouble()
                            //                    txtGoldAmount.setText(gold + " GM");
                            AttemptToGetTodayGoldRate(gold.toDouble())
                        }
                }
            }
        })
    }

    private fun AttemptToGetTodayGoldRate(gold: Double) {
        /*getTodayGoldRateServiceProvider!!.getTodayGoldRate(VGoldApp.onGetUerId(),
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? = (serviceResponse as GetTodayGoldRateModel).status
                        val message: String? = (serviceResponse as GetTodayGoldRateModel).message
                        val todayGoldPurchaseRate: String? =
                            (serviceResponse as GetTodayGoldRateModel).gold_purchase_rate
                        val sellingRate = gold * todayGoldPurchaseRate?.toDouble()!!
                        GoldAmt = DecimalFormat("##.##").format(sellingRate)
                        //                    txtGoldValue.setText("(Worth ₹ " + amt + ")");
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
                                this@BookingDetailsActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@BookingDetailsActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@BookingDetailsActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })*/

        // change in api calling
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_purchase_rate.php")
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
                    if (status.equals("200"))
                        runOnUiThread {
                            var todayGoldPurchaseRate: String? =
                                json.optString("Gold_purchase_rate")
                            var sellingRate = gold * todayGoldPurchaseRate?.toDouble()!!
                            GoldAmt = DecimalFormat("##.##").format(sellingRate)
                        }
                }
            }
        })
    }

    fun onClickOfBtnPayOnline() {
        if (payment_option == "Cheque") {
            AttemptToGoldBookingRequest(
                userId, booking_value!!, down_payment!!,
                monthly!!, gold_rate!!, quantity!!, tennure!!, pc!!, payment_option!!,
                edtBankDetail!!.text.toString(), "",
                edtChequeNo!!.text.toString(), initBookingCharge!!, disc!!, booking_charge!!, "0"
            )
        } else if (payment_option == "Online") {
            AttemptToGoldBookingRequest(
                userId,
                booking_value!!,
                down_payment!!,
                monthly!!,
                gold_rate!!,
                quantity!!,
                tennure!!,
                pc!!,
                payment_option!!,
                edtRtgsBankDetail!!.text.toString(),
                edtTxnId!!.text.toString(),
                "",
                initBookingCharge!!,
                disc!!,
                booking_charge!!,
                "0"
            )
        } else if (payment_option == "Money Wallet") {
            val payableAmt =
                down_payment!!.toDouble() + txtBookingCharge!!.text.toString().trim { it <= ' ' }
                    .toDouble()
            if (moneyWalletBal != null && !TextUtils.isEmpty(moneyWalletBal) && !moneyWalletBal.equals(
                    "null",
                    ignoreCase = true
                )
            ) {
                val remainWalletAmt = moneyWalletBal!!.toDouble() - payableAmt
                if (remainWalletAmt > 0) {
                    ShowPopupDialog(null, "moneyWallet", payableAmt)
                } else {
                    AlertDialogs().alertDialogOk(
                        this@BookingDetailsActivity,
                        "Alert",
                        "Money Wallet balance is less then entered amount",
                        resources.getString(R.string.btn_ok),
                        0,
                        false,
                        alertDialogOkListener
                    )
                }
            } else {
                ShowPopupDialog(null, "moneyWallet", payableAmt)
            }
        } else if (payment_option == "Gold Wallet") {
            val payableAmt =
                down_payment!!.toDouble() + txtBookingCharge!!.text.toString().trim { it <= ' ' }
                    .toDouble()
            if (GoldAmt != null && !TextUtils.isEmpty(GoldAmt) && !GoldAmt.equals(
                    "null",
                    ignoreCase = true
                )
            ) {
                remainWalletAmt = GoldAmt!!.toDouble() - payableAmt
                if (remainWalletAmt > 0) {
                    ShowPopupDialog(dataModel, "goldWallet", payableAmt)
                } else {
                    AlertDialogs().alertDialogOk(
                        this@BookingDetailsActivity,
                        "Alert",
                        "Gold Wallet balance is less then entered amount",
                        resources.getString(R.string.btn_ok),
                        0,
                        false,
                        alertDialogOkListener
                    )
                }
            } else {
                ShowPopupDialog(dataModel, "goldWallet", payableAmt)
            }
        } else if (payment_option == "UPI Payment") {
            integrateGpay(
                down_payment!!.toDouble() + txtBookingCharge!!.text.toString().trim { it <= ' ' }
                    .toDouble(),
                gold_rate!!, quantity!!
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun ShowPopupDialog(
        dataModel: GoldBookingRequestModel.Data?,
        key: String,
        payableAmt: Double
    ) {
        val dialog = Dialog(this@BookingDetailsActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.wallet_payment_dialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val window = dialog.window!!
        window.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val btnYes = dialog.findViewById<AppCompatButton>(R.id.btnYes)
        val btnNo = dialog.findViewById<AppCompatButton>(R.id.btnNo)
        val txtBalanceRemainId = dialog.findViewById<TextView>(R.id.txtBalanceRemainId)
        val txtPayableAmtId = dialog.findViewById<TextView>(R.id.txtPayableAmtId)
        val txtSaleRateId = dialog.findViewById<TextView>(R.id.txtSaleRateId)
        val txtGSTAmtId = dialog.findViewById<TextView>(R.id.txtGSTAmtId)
        val txtWalletAmtId = dialog.findViewById<TextView>(R.id.txtWalletAmtId)
        val txtGSTPayableAmtId = dialog.findViewById<TextView>(R.id.txtGSTPayableAmtId)
        val txtDeductedGoldId = dialog.findViewById<TextView>(R.id.txtDeductedGoldId)
        if (key.equals("moneyWallet", ignoreCase = true)) {
            txtGSTAmtId.visibility = View.GONE
            txtGSTPayableAmtId.visibility = View.GONE
            txtDeductedGoldId.visibility = View.GONE
            txtSaleRateId.visibility = View.GONE
            txtPayableAmtId.text = ("Payable Amount : " + resources.getString(R.string.rs)
                    + payableAmt)
            txtWalletAmtId.text = ("Money in Wallet : " + resources.getString(R.string.rs)
                    + moneyWalletBal)
            txtBalanceRemainId.text = ("Remaining Money in Wallet : "
                    + resources.getString(R.string.rs) + remainWalletAmt)
        } else {
            if (dataModel != null) {
                txtGSTAmtId.visibility = View.VISIBLE
                txtGSTPayableAmtId.visibility = View.VISIBLE
                txtDeductedGoldId.visibility = View.VISIBLE
                txtSaleRateId.visibility = View.VISIBLE
                txtPayableAmtId.text = ("Payable Amount : " + resources.getString(R.string.rs)
                        + dataModel.amount_to_pay)
                txtWalletAmtId.text =
                    "Gold in Wallet : " + dataModel.gold_in_wallet.toString() + " gm"
                txtSaleRateId.text = ("Today's Gold Sale Rate : " + resources.getString(R.string.rs)
                        + dataModel.today_sale_rate)
                txtGSTAmtId.text =
                    "GST for Today's Gold Sale Rate : " + dataModel.gst_per.toString() + "%"
                txtGSTPayableAmtId.text =
                    ("GST on Payable Amount : " + resources.getString(R.string.rs)
                            + dataModel.amount_to_pay_gst)
                txtDeductedGoldId.text =
                    "Deducted Gold from Wallet : " + dataModel.gold_reduce.toString() + " gm"
                txtBalanceRemainId.text = ("Remaining Gold in Wallet : "
                        + dataModel.reduced_gold_in_wallet) + " gm"
            }
        }
        btnYes.setOnClickListener {
            if (key.equals("moneyWallet", ignoreCase = true)) {
                AttemptToGoldBookingRequest(
                    userId, booking_value!!,
                    down_payment!!,
                    monthly!!, gold_rate!!, quantity!!, tennure!!, pc!!,
                    payment_option!!,
                    edtRtgsBankDetail!!.text.toString(), edtTxnId!!.text.toString(), "",
                    initBookingCharge!!, disc!!, booking_charge!!, "0"
                )
            } else {
                AttemptToGoldBookingRequest(
                    userId, booking_value!!,
                    down_payment!!,
                    monthly!!, gold_rate!!, quantity!!, tennure!!, pc!!,
                    payment_option!!,
                    edtRtgsBankDetail!!.text.toString(), edtTxnId!!.text.toString(), "",
                    initBookingCharge!!, disc!!, booking_charge!!, "1"
                )
            }
            dialog.dismiss()
        }
        btnNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun integrateGpay(amt: Double, goldRate: String, weight: String) {
        var no = "00000"
        if (VGoldApp.onGetNo() != null && !TextUtils.isEmpty(VGoldApp.onGetNo())) {
            no = VGoldApp.onGetNo()!!.substring(0, 5)
        }
        val transNo = userId + "-" + BaseActivity.date
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
        val uri = Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", "9881136531@okbizaxis")
            .appendQueryParameter("pn", "VGold Pvt. Ltd.")
            .appendQueryParameter("mc", "")
            .appendQueryParameter("tr", transNo)
            .appendQueryParameter(
                "tn",
                "GB_" + weight + "_" + goldRate + " " + name + "(" + userId + ")"
            )
            .appendQueryParameter("am", amt.toString())
            .appendQueryParameter(
                "cu",
                "INR"
            ) //                        .appendQueryParameter("url", "your-transaction-url")
            .build()

        /*Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "9881136531@okbizaxis")
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "")//"28-4092-313-2021-00-14")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", transNo)
                .appendQueryParameter("tn", "GB_" + weight + "_" + goldRate + " " + name + "(" + VGoldApp.onGetUerId() + ")")
                .appendQueryParameter("am", String.valueOf(amt))
//                .appendQueryParameter("am", "10.0")
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
                this@BookingDetailsActivity,
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
        Log.e("main ", "response $resultCode")
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
        if (isConnectionAvailable(this@BookingDetailsActivity)) {
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
//                Toast.makeText(BookingDetailActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
//                Log.e("UPI", "payment successfull: "+approvalRefNo);
                AttemptToGoldBookingRequest(
                    userId, booking_value!!,
                    down_payment!!, monthly!!,
                    gold_rate!!, quantity!!, tennure!!, pc!!, payment_option!!,
                    edtRtgsBankDetail!!.text.toString(), approvalRefNo, "",
                    initBookingCharge!!, disc!!, booking_charge!!, "0"
                )
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(
                    this@BookingDetailsActivity,
                    "Payment cancelled by user.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "Cancelled by user: $approvalRefNo")
            } else {
                Toast.makeText(
                    this@BookingDetailsActivity,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "failed payment: $approvalRefNo")
            }
        } else {
            Log.e("UPI", "Internet issue: ")
            Toast.makeText(
                this@BookingDetailsActivity,
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


    private fun AttemptToGoldBookingRequest(
        user_id: String?, booking_value: String,
        down_payment: String, monthly: String, rate: String,
        gold_weight: String, tennure: String, pc: String,
        payment_option: String, bank_details: String,
        tr_id: String, cheque_no: String, initBookingCharge: String,
        disc: String, booking_charge: String, confirmedVal: String
    ) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        /*goldBookingRequestServiceProvider?.getGoldBookingRequest(user_id,
            booking_value, down_payment, monthly, rate, gold_weight, tennure, pc,
            payment_option, bank_details, tr_id, cheque_no, initBookingCharge, disc, booking_charge,
            confirmedVal, object : APICallback {
                @SuppressLint("SetTextI18n")
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? =
                            (serviceResponse as GoldBookingRequestModel).status
                        val message: String? =
                            (serviceResponse as GoldBookingRequestModel).message
                        dataModel = (serviceResponse as GoldBookingRequestModel).data
                        if (status == "200") {
                            if (payment_option.equals("Gold Wallet", ignoreCase = true) &&
                                confirmedVal.equals("0", ignoreCase = true)
                            ) {
                                calculationLayout!!.visibility = View.VISIBLE
                                txtGSTAmtId!!.visibility = View.VISIBLE
                                txtGSTPayableAmtId!!.visibility = View.VISIBLE
                                txtDeductedGoldId!!.visibility = View.VISIBLE
                                txtSaleRateId!!.visibility = View.VISIBLE
                                txtPayableAmtId!!.text =
                                    ("Payable Amount : " + resources.getString(R.string.rs)
                                            + dataModel?.amount_to_pay)
                                txtWalletAmtId!!.text =
                                    "Gold in Wallet : " + dataModel?.gold_in_wallet
                                        .toString() + " gm"
                                txtSaleRateId!!.text =
                                    ("Today's Gold Sale Rate : " + resources.getString(R.string.rs)
                                            + dataModel?.today_sale_rate)
                                txtGSTAmtId!!.text =
                                    "GST for Today's Gold Sale Rate : " + dataModel?.gst_per
                                        .toString() + "%"
                                txtGSTPayableAmtId!!.text =
                                    ("GST on Payable Amount : " + resources.getString(R.string.rs)
                                            + dataModel?.amount_to_pay_gst)
                                txtDeductedGoldId!!.text =
                                    "Deducted Gold from Wallet : " + dataModel?.gold_reduce
                                        .toString() + " gm"
                                txtBalanceRemainId!!.text = ("Remaining Gold in Wallet : "
                                        + dataModel?.reduced_gold_in_wallet) + " gm"

                                //                                    ShowPopupDialog(dataModel, "goldWallet");
                            } else {
                                val intent =
                                    Intent(this@BookingDetailsActivity, SuccessActivity::class.java)
                                intent.putExtra("message", message)
                                startActivity(intent)
                            }


                            //   mAlert.onShowToastNotification(BookingDetailActivity.this, message);
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@BookingDetailsActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )
                            //                        mAlert.onShowToastNotification(BookingDetailActivity.this, message);
                            //                        Intent intent = new Intent(BookingDetailActivity.this, MainActivity.class);
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
                                this@BookingDetailsActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@BookingDetailsActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@BookingDetailsActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })

       // gold_booking*/


        Log.i("BGA", "user_id : " + user_id)
        Log.i("BGA", "booking_value : " + booking_value)
        Log.i("BGA", "down_payment : " + down_payment)
        Log.i("BGA", "monthly : " + monthly)
        Log.i("BGA", "rate : " + rate)
        Log.i("BGA", "gold_weight : " + gold_weight)
        Log.i("BGA", "tennure : " + tennure)
        Log.i("BGA", "pc : " + pc)
        Log.i("BGA", "payment_option : " + payment_option)
        Log.i("BGA", "bank_details : " + bank_details)
        Log.i("BGA", "tr_id : " + tr_id)
        Log.i("BGA", "cheque_no : " + cheque_no)
        Log.i("BGA", "initial_booking_charges : " + initBookingCharge)
        Log.i("BGA", "booking_charges_discount : " + disc)
        Log.i("BGA", "booking_charges : " + booking_charge)
        Log.i("BGA", "confirmed : " + confirmedVal)


        // change in api calling
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("booking_value", booking_value)
            .addFormDataPart("down_payment", down_payment)
            .addFormDataPart("monthly", monthly)
            .addFormDataPart("rate", rate)
            .addFormDataPart("gold_weight", gold_weight)
            .addFormDataPart("tennure", tennure)
            .addFormDataPart("pc", pc)
            .addFormDataPart("payment_option", payment_option)
            .addFormDataPart("bank_details", bank_details)
            .addFormDataPart("tr_id", tr_id)
            .addFormDataPart("cheque_no", cheque_no)
            .addFormDataPart("initial_booking_charges", initBookingCharge)
            .addFormDataPart("booking_charges_discount", disc)
            .addFormDataPart("booking_charges", booking_charge)
            .addFormDataPart("confirmed", confirmedVal)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/gold_booking.php")
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
                    progressDialog!!.hide()
                    var json = JSONObject(resp)
                    var status = json.get("status").toString()
                    var message: String? = json.optString("Message")
                    if (status.equals("200"))
                        runOnUiThread {
                            var data = json.optJSONObject("data")
                            if (payment_option.equals("Gold Wallet", ignoreCase = true) &&
                                confirmedVal.equals("0")
                            ) {
                                calculationLayout!!.visibility = View.VISIBLE
                                txtGSTAmtId!!.visibility = View.VISIBLE
                                txtGSTPayableAmtId!!.visibility = View.VISIBLE
                                txtDeductedGoldId!!.visibility = View.VISIBLE
                                txtSaleRateId!!.visibility = View.VISIBLE
                                txtPayableAmtId!!.text =
                                    ("Payable Amount : " + resources.getString(R.string.rs)
                                            + data.optString("amount_to_pay"))
                                txtWalletAmtId!!.text =
                                    "Gold in Wallet : " + data.optString("gold_in_wallet")
                                        .toString() + " gm"
                                txtSaleRateId!!.text =
                                    ("Today's Gold Sale Rate : " + resources.getString(R.string.rs)
                                            + data.optString("today_sale_rate"))
                                txtGSTAmtId!!.text =
                                    "GST for Today's Gold Sale Rate : " + data.optString("gst_per")
                                        .toString() + "%"
                                txtGSTPayableAmtId!!.text =
                                    ("GST on Payable Amount : " + resources.getString(R.string.rs)
                                            + data.optString("amount_to_pay_gst"))
                                txtDeductedGoldId!!.text =
                                    "Deducted Gold from Wallet : " + data.optString("gold_reduce")
                                        .toString() + " gm"
                                txtBalanceRemainId!!.text = ("Remaining Gold in Wallet : "
                                        + data.optString("reduced_gold_in_wallet")) + " gm"

                                //                                    ShowPopupDialog(dataModel, "goldWallet");
                            } else {
                                val intent =
                                    Intent(this@BookingDetailsActivity, SuccessActivity::class.java)
                                intent.putExtra("message", message)
                                startActivity(intent)
                            }
                        } else {
                        runOnUiThread {
                            AlertDialogs().alertDialogOk(
                                this@BookingDetailsActivity,
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
                val intent = Intent(this@BookingDetailsActivity, MainActivity::class.java)
                startActivity(intent)
            }
            11 -> {
                val LogIntent = Intent(this@BookingDetailsActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}


//        sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
//        supportActionBar?.title = "Gold Booking Details "
//
//        tvBookVal = findViewById(R.id.txtBookingValue)
//        tvGoldRate= findViewById(R.id.txtGoldRate)
//        tvDownPayment = findViewById(R.id.txtDownPayment)
////        tvInitBCharge = findViewById(R.id.txtInitBookingCharge)
////        tvDiscount = findViewById(R.id.txtDisc)
//        tvdiscBookCharge = findViewById(R.id.txtDisBookingCharge)
//        tvMonthInstalment = findViewById(R.id.txtMonthly)
//        spPayOption = findViewById(R.id.spinner_payment_option)
//        edtBankDet = findViewById(R.id.edtBankDetail)
//        edtChqNo = findViewById(R.id.edtChequeNo)
//        btnPayOnline = findViewById(R.id.btnPayOnline)
//
//
//        btnPayOnline.setOnClickListener {
//
//            if (spPayOption.selectedItemPosition == 0) {
//
//                Toast.makeText(this, "Please Select Pay Option !", Toast.LENGTH_SHORT)
//                    .show()
//                spPayOption.requestFocus()
//        }else
//
//            logintoVgold()
//
//        }
//    }
//    private fun logintoVgold() {
//        val client = OkHttpClient().newBuilder().build()
//        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("booking_value", tvBookVal.text.toString())
//            .addFormDataPart("rate", tvGoldRate.text.toString())
//            .addFormDataPart("down_payment", tvDownPayment.text.toString())
//            .addFormDataPart("monthly", tvMonthInstalment.text.toString())
//
//            .addFormDataPart("booking_value", tvBookVal.text.toString())
//
//            .build()
//        val request = okhttp3.Request.Builder()
//            .url("http://vgold.co.in/dashboard/webservices/gold_booking.php?")
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
//                        val intent = Intent(this@BookingDetailsActivity, SuccessActivity::class.java)
//                       // intent.putExtra("email_id", edtEmail.text.toString())
//                        startActivity(intent)
//                        // Toast.makeText(this@LoginActivity, ".$mMessage", Toast.LENGTH_SHORT).show()
//                        finish()
//                    })
//                }
//            }
//        })
//    }
//
//    override fun onDialogOk(resultCode: Int) {
//        TODO("Not yet implemented")
//    }
//
//
//}
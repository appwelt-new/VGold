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
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.cognifygroup.vgold.BuildConfig
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.interfaces.GetBookingIdModel
import com.cognifygroup.vgold.model.*
import com.cognifygroup.vgold.payInstallment.PayInstallmentModel
import com.cognifygroup.vgold.utilities.ColorSpinnerAdapter
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
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

class PayInstallmentActivity : AppCompatActivity(), AlertDialogOkListener {


    var spinner_goldBookingId: Spinner? = null
    var txtAmount: TextView? = null
    var txtRupee: TextView? = null
    var spinner_payment_option: Spinner? = null
    var llCheque: LinearLayout? = null
    var llRTGS: LinearLayout? = null
    var btnProceedToPayment1: Button? = null
    var radioMinAmt: RadioButton? = null
    var radioOtherAmt: RadioButton? = null
    var radioMoneyWallet: RadioButton? = null
    var radioGoldWallet: RadioButton? = null

    var radioGroup: RadioGroup? = null
    var edtBankDetail: EditText? = null
    var edtChequeNo: EditText? = null
    var edtRtgsBankDetail: EditText? = null

    var edtTxnId: EditText? = null
    var txtPayableAmount: EditText? = null
    var txtOtherAmount: EditText? = null
    var txtWalletAmount: TextView? = null
    var txtGoldAmount: TextView? = null
    var api: LinearLayout? = null
    var txtWalletRupee: TextView? = null
    var txtGoldValue: TextView? = null
    var calculationLayout: LinearLayoutCompat? = null
    var txtGSTAmtId: TextView? = null
    var txtGSTPayableAmtId: TextView? = null
    var txtDeductedGoldId: TextView? = null
    var txtSaleRateId: TextView? = null
    var txtPayableAmtId: TextView? = null
    var txtWalletAmtId: TextView? = null
    var txtBalanceRemainId: TextView? = null
    var objbnkdetiv: ImageView? = null
    var shreiv: ImageView? = null
    var bnkdethideiv: ImageView? = null

    private var moneyWalletBal: String? = null
    private var GoldAmt: String? = null
    var bookingId: String? = null


    val UPI_PAYMENT = 0
    var GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
    var GOOGLE_PAY_REQUEST_CODE = 123

    var result = 0.0
    var amount = 0.0
    var goldWeight = "0.0"
    var payment_option = ""
    var todayGoldRate = "0"
    private var msg: String? = null

    var remainWalletAmt = 0.0
    private var dataModel: PayInstallmentModel.Data? = null
    private var progressDialog: TransparentProgressDialog? = null
    private var alertDialogOkListener: AlertDialogOkListener = this

    var mAlert: AlertDialogs? = null
    /*   var getGoldBookingIdServiceProvider: GetGoldBookingIdServiceProvider? = null
       var fetchDownPaymentServiceProvider: FetchDownPaymentServiceProvider? = null
       var payInstallmentServiceProvider: PayInstallmentServiceProvider? = null
       var getAllTransactionMoneyServiceProvider: GetAllTransactionMoneyServiceProvider? = null
       var getAllTransactionGoldServiceProvider: GetAllTransactionGoldServiceProvider? = null
       var getTodayGoldRateServiceProvider: GetTodayGoldRateServiceProvider? = null
       private var loginStatusServiceProvider: LoginStatusServiceProvider? = null*/


    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_installment)
        sharedPreferences =
            this@PayInstallmentActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()



        spinner_goldBookingId = findViewById(R.id.spinner_goldBookingId)
        txtAmount = findViewById(R.id.txtAmount)
        txtRupee = findViewById(R.id.txtRupee)
        spinner_payment_option = findViewById(R.id.spinner_payment_option)
        txtBalanceRemainId = findViewById(R.id.txtBalanceRemainId)
        txtWalletAmtId = findViewById(R.id.txtWalletAmtId)
        txtPayableAmtId = findViewById(R.id.txtPayableAmtId)
        txtSaleRateId = findViewById(R.id.txtSaleRateId)
        txtDeductedGoldId = findViewById(R.id.txtDeductedGoldId)
        txtGSTPayableAmtId = findViewById(R.id.txtGSTPayableAmtId)
        txtGSTAmtId = findViewById(R.id.txtGSTAmtId)
        calculationLayout = findViewById(R.id.calculationLayout)
        txtGoldValue = findViewById(R.id.txtGoldValue)
        txtWalletRupee = findViewById(R.id.txtWalletRupee)
        api = findViewById(R.id.api)
        txtGoldAmount = findViewById(R.id.txtGoldAmount)
        txtWalletAmount = findViewById(R.id.txtWalletAmount)
        txtOtherAmount = findViewById(R.id.txtOtherAmount)
        txtPayableAmount = findViewById(R.id.txtPayableAmount)
        edtTxnId = findViewById(R.id.edtTxnId)
        edtRtgsBankDetail = findViewById(R.id.edtRtgsBankDetail)
        edtChequeNo = findViewById(R.id.edtChequeNo)
        edtBankDetail = findViewById(R.id.edtBankDetail)
        radioGroup = findViewById(R.id.radioGroup)
        radioGoldWallet = findViewById(R.id.radioGoldWallet)
        radioMoneyWallet = findViewById(R.id.radioMoneyWallet)
        radioOtherAmt = findViewById(R.id.radioOtherAmt)
        radioMinAmt = findViewById(R.id.radioMinAmt)
        btnProceedToPayment1 = findViewById(R.id.btnProceedToPayment1)
        llRTGS = findViewById(R.id.llRTGS)
        llCheque = findViewById(R.id.llCheque)
        objbnkdetiv = findViewById(R.id.bnkdetiv)
        shreiv = findViewById(R.id.shreiv)
        //bnkdethideiv = findViewById(R.id.bnkdethideiv)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.title = "Pay Installment"
        progressDialog = TransparentProgressDialog(this@PayInstallmentActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        Glide.with(this)
            .load("https://www.vgold.co.in/dashboard/vgold_rate/bank%20details.png")
            .into(objbnkdetiv!!);
        mAlert = AlertDialogs().getInstance()

        //  getGoldBookingIdServiceProvider = GetGoldBookingIdServiceProvider(this)
        //  fetchDownPaymentServiceProvider = FetchDownPaymentServiceProvider(this)
        //  payInstallmentServiceProvider = PayInstallmentServiceProvider(this)
        // loginStatusServiceProvider = LoginStatusServiceProvider(this)
        //  getAllTransactionMoneyServiceProvider = GetAllTransactionMoneyServiceProvider(this)
        //  getAllTransactionGoldServiceProvider = GetAllTransactionGoldServiceProvider(this)
        //  getTodayGoldRateServiceProvider = GetTodayGoldRateServiceProvider(this)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.payment_option, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        spinner_payment_option!!.setAdapter(adapter)

        spinner_payment_option!!.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val paymentoption = parent.getItemAtPosition(pos) as String
                if (paymentoption == "cheque") {
                    payment_option = "Cheque"
                    llCheque!!.setVisibility(View.VISIBLE)
                    llRTGS!!.setVisibility(View.GONE)
                } else if (paymentoption == "NEFT/RTGS/Online Transfer") {
                    payment_option = "RTGS"
                    llRTGS!!.setVisibility(View.VISIBLE)
                    llCheque!!.setVisibility(View.GONE)
                } /*else if (paymentoption.equals("Gold Wallet")) {
                    payment_option = "Gold Wallet";
                    llCheque.setVisibility(View.GONE);
                    llRTGS.setVisibility(View.GONE);
                } else if (paymentoption.equals("Money Wallet")) {
                    payment_option = "Money Wallet";
                    llCheque.setVisibility(View.GONE);
                    llRTGS.setVisibility(View.GONE);
                }*/ else if (paymentoption == "Credit/Debit/Net Banking(Payment Gateway)") {
                    payment_option = "Credit/Debit/Net Banking(Payment Gateway)"
                    llCheque!!.setVisibility(View.GONE)
                    llRTGS!!.setVisibility(View.GONE)
                } else if (paymentoption == "UPI Payment") {
                    payment_option = "UPI Payment"
                    llCheque!!.setVisibility(View.GONE)
                    llRTGS!!.setVisibility(View.GONE)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })

        btnProceedToPayment1!!.setOnClickListener {
            OnClickOfProceedToPayment1()
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

        radioGroup!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (radioMinAmt!!.isChecked()) {
                calculationLayout!!.setVisibility(View.GONE)
                txtAmount!!.setVisibility(View.VISIBLE)
                txtOtherAmount!!.setVisibility(View.GONE)
                txtPayableAmount!!.setVisibility(View.GONE)
                txtGoldAmount!!.setVisibility(View.GONE)
                txtGoldValue!!.setVisibility(View.GONE)
                txtWalletAmount!!.setVisibility(View.GONE)
                txtWalletRupee!!.setVisibility(View.GONE)
                api!!.setVisibility(View.VISIBLE)
            } else if (radioOtherAmt!!.isChecked()) {
                calculationLayout!!.setVisibility(View.GONE)
                txtOtherAmount!!.setText("")
                txtOtherAmount!!.setVisibility(View.VISIBLE)
                txtPayableAmount!!.setVisibility(View.GONE)
                api!!.setVisibility(View.VISIBLE)
                txtGoldAmount!!.setVisibility(View.GONE)
                txtGoldValue!!.setVisibility(View.GONE)
                txtWalletAmount!!.setVisibility(View.GONE)
                txtWalletRupee!!.setVisibility(View.GONE)
            } else if (radioMoneyWallet!!.isChecked()) {
                AttemptToGetMoneyBalance(userId)
                txtPayableAmount!!.setText("")
                txtPayableAmount!!.setError(null)
                txtOtherAmount!!.setVisibility(View.GONE)
                calculationLayout!!.setVisibility(View.GONE)
                txtPayableAmount!!.setVisibility(View.VISIBLE)
                txtGoldAmount!!.setVisibility(View.GONE)
                txtGoldValue!!.setVisibility(View.GONE)
                txtWalletAmount!!.setVisibility(View.VISIBLE)
                txtWalletRupee!!.setVisibility(View.VISIBLE)
                api!!.setVisibility(View.GONE)
            } else {
                AttemptToGetGoldBalance(userId)
                txtPayableAmount!!.setText("")
                txtPayableAmount!!.setError(null)
                calculationLayout!!.setVisibility(View.GONE)
                txtOtherAmount!!.setVisibility(View.GONE)
                txtPayableAmount!!.setVisibility(View.VISIBLE)
                txtGoldAmount!!.setVisibility(View.VISIBLE)
                txtGoldValue!!.setVisibility(View.VISIBLE)
                txtWalletAmount!!.setText("")
                txtWalletRupee!!.setText("")
                api!!.setVisibility(View.GONE)
            }
        })


        txtPayableAmount!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (radioMoneyWallet!!.isChecked()) {
                    if (s.length > 0) {
                        calculateMoneyAmount(s.toString())
                    } else {
                        txtWalletAmount!!.setText(moneyWalletBal)
                        calculationLayout!!.setVisibility(View.GONE)
                    }
                } else if (radioGoldWallet!!.isChecked()) {
                    if (s.length > 0) {
                        if (bookingId != null) {
                            AttemptToPayInstallment(
                                userId,
                                this@PayInstallmentActivity.bookingId!!,
                                "" + txtAmount!!.getText().toString(),
                                "Gold Wallet",
                                "", "",
                                txtPayableAmount!!.getText().toString(),
                                "", "0"
                            )

                        }

                    } else {
                        txtWalletAmount!!.setText(moneyWalletBal)
                        calculationLayout!!.setVisibility(View.GONE)
                    }
                }
            }
        })

        // checkLoginSession()
        AttemptTogetBookingId(userId)
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


    private fun calculateMoneyAmount(payableAmt: String) {
        if (moneyWalletBal != null && !TextUtils.isEmpty(moneyWalletBal) && !moneyWalletBal.equals(
                "null",
                ignoreCase = true
            )
        ) {
            remainWalletAmt = moneyWalletBal!!.toDouble() - payableAmt.toDouble()
            if (remainWalletAmt < 0) {
                txtPayableAmount!!.error = "Amount Exceed"
                //                txtWalletAmount.setText(String.format("%.2f", remainWalletAmt));
            } else {
            }
        }
        calculationLayout!!.visibility = View.VISIBLE
        txtGSTAmtId!!.visibility = View.GONE
        txtGSTPayableAmtId!!.visibility = View.GONE
        txtDeductedGoldId!!.visibility = View.GONE
        txtSaleRateId!!.visibility = View.GONE
        txtPayableAmtId!!.text = ("Payable Amount : " + resources.getString(R.string.rs)
                + txtPayableAmount!!.text.toString())
        txtWalletAmtId!!.text = ("Money in Wallet : " + resources.getString(R.string.rs)
                + moneyWalletBal)
        txtBalanceRemainId!!.text = ("Remaining Money in Wallet : "
                + resources.getString(R.string.rs) + remainWalletAmt)
    }

    private fun AttemptToGetMoneyBalance(user_id: String?) {
        //  progressDialog!!.show()
        /*   getAllTransactionMoneyServiceProvider?.getAllTransactionMoneyHistory(
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

                           //                    walletAmtId.setText("Money Wallet Balance " + getResources().getString(R.string.rs) + "" + moneyWalletBal);
                           txtWalletRupee!!.text = resources.getString(R.string.rs)
                           txtWalletAmount!!.text = moneyWalletBal
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
                                   this@PayInstallmentActivity,
                                   (apiErrorModel as BaseServiceResponseModel).message
                               )
                           } else {
                               PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                           }
                       } catch (e: Exception) {
                           e.printStackTrace()
                           PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                       } finally {
                           progressDialog!!.hide()
                       }
                   }
               })*/


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

            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                var resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(resp)
                    var status = json.get("status").toString()
                    runOnUiThread {
                        if (status == "200") {
                            var Wallet_Balance = json.optString("Wallet_Balance").toString()
                            progressDialog!!.dismiss()
                            moneyWalletBal = Wallet_Balance
                            txtWalletRupee!!.text = resources.getString(R.string.rs)
                            txtWalletAmount!!.text = moneyWalletBal
                        }
                    }
                }
            }
        })
    }

    private fun AttemptToGetGoldBalance(user_id: String?) {
        //  progressDialog!!.show()
        /*  getAllTransactionGoldServiceProvider?.getAllTransactionGoldHistory(
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
                          //                    walletAmtId.setText("Gold Wallet Balance " + gold + " GM");
                          txtGoldAmount!!.text = "$gold GM"
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
                                  this@PayInstallmentActivity,
                                  (apiErrorModel as BaseServiceResponseModel).message
                              )
                          } else {
                              PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                          }
                      } catch (e: Exception) {
                          e.printStackTrace()
                          PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                      } finally {
                          progressDialog!!.hide()
                      }
                  }
              })*/


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
                progressDialog!!.hide()

                Log.e("failure Response", mMessage)
                runOnUiThread {

                    PrintUtil.showToast(
                        this@PayInstallmentActivity,
                        mMessage.toString()
                    )
                }
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                var resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(resp)
                    val status = json.get("status").toString()
                    runOnUiThread {
                        if (status == "200") {
                            var gold_Balance = json.optString("gold_Balance").toString()
                            progressDialog!!.hide()
                            var balance: String? =
                                gold_Balance
                            var gold = balance?.toDouble()
                            var numberFormat = DecimalFormat("#.000")
                            gold = numberFormat.format(gold).toDouble()
                            //                    walletAmtId.setText("Gold Wallet Balance " + gold + " GM");
                            txtGoldAmount!!.text = "$gold GM"
                            AttemptToGetTodayGoldRate(gold)
                        }
                    }
                }
            }
        })
    }

    private fun AttemptToGetTodayGoldRate(gold: Double) {
        /* getTodayGoldRateServiceProvider?.getTodayGoldRate(VGoldApp.onGetUerId(),
             object : APICallback {
                 override fun <T> onSuccess(serviceResponse: T) {
                     try {
                         val status: String? = (serviceResponse as GetTodayGoldRateModel).status
                         val message: String? =
                             (serviceResponse as GetTodayGoldRateModel).message
                         val todayGoldPurchaseRate: String? =
                             (serviceResponse as GetTodayGoldRateModel).gold_purchase_rate
                         val sellingRate = gold * todayGoldPurchaseRate!!.toDouble()
                         GoldAmt = DecimalFormat("##.##").format(sellingRate)
                         txtGoldValue!!.text = "(Worth ₹ $GoldAmt)"
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
                                 this@PayInstallmentActivity,
                                 (apiErrorModel as BaseServiceResponseModel).message
                             )
                         } else {
                             PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                         }
                     } catch (e: Exception) {
                         e.printStackTrace()
                         PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                     } finally {
                         progressDialog!!.hide()
                     }
                 }
             })*/


        // change api calling function
        val client = OkHttpClient().newBuilder().build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_purchase_rate.php")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                var mMessage = e.message.toString()
                e.printStackTrace()
                progressDialog!!.hide()

                Log.e("failure Response", mMessage)
                PrintUtil.showToast(
                    this@PayInstallmentActivity,
                    mMessage.toString()
                )
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: okhttp3.Response) {
                var resp = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    var json = JSONObject(resp)
                    var status = json.get("status").toString()
                    runOnUiThread {
                        if (status == "200") {
                            var Gold_purchase_rate = json.optString("Gold_purchase_rate").toString()
                            progressDialog!!.hide()
                            var todayGoldPurchaseRate: String? =
                                Gold_purchase_rate
                            var sellingRate = gold * todayGoldPurchaseRate!!.toDouble()
                            GoldAmt = DecimalFormat("##.##").format(sellingRate)
                            txtGoldValue!!.text = "(Worth ₹ $GoldAmt)"
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

    fun OnClickOfProceedToPayment1() {
        if (spinner_goldBookingId!!.selectedItemPosition != 0) {

//            if (radioMinAmt.isChecked()) {
//                txtOtherAmount.setVisibility(View.GONE);
//            } else {
//                txtOtherAmount.setVisibility(View.VISIBLE);
//            }
            if (api!!.visibility == View.VISIBLE) {
                if (spinner_payment_option!!.selectedItemPosition != 0) {
                    if (txtOtherAmount!!.visibility == View.VISIBLE) {
                        if (txtOtherAmount!!.text.toString() != null && !TextUtils.isEmpty(
                                txtOtherAmount!!.text.toString()
                            )
                        ) {
                            val otherAmt =
                                java.lang.Double.valueOf(txtOtherAmount!!.text.toString())

//                    if (!txtAmount.getText().toString().equals("") && txtAmount.getText().toString() != null) {
//                        Double minAmt = Double.valueOf(txtAmount.getText().toString());
                            if (otherAmt > 0) {
                                if (payment_option == "Cheque") {
                                    if (bookingId != null) {
                                        AttemptToPayInstallment(
                                            userId,
                                            bookingId!!,
                                            "" + txtAmount!!.text.toString(),
                                            payment_option,
                                            edtBankDetail!!.text.toString(),
                                            "",
                                            txtOtherAmount!!.text.toString(),
                                            edtChequeNo!!.text.toString(),
                                            "0"
                                        )
                                    }
                                } else if (payment_option == "RTGS") {
                                    if (bookingId != null) {
                                        AttemptToPayInstallment(
                                            userId,
                                            bookingId!!,
                                            "" + txtAmount!!.text.toString(),
                                            payment_option,
                                            edtRtgsBankDetail!!.text.toString(),
                                            edtTxnId!!.text.toString(),
                                            txtOtherAmount!!.text.toString(),
                                            "",
                                            "0"
                                        )
                                    }
                                } /*else if (payment_option.equals("Money Wallet")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        "", "", txtOtherAmount.getText().toString(),
                                        "", "0");

                            } else if (payment_option.equals("Gold Wallet")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        "", "", txtOtherAmount.getText().toString(),
                                        "", "0");

                            }*/ else if (payment_option == "UPI Payment") {
                                    /*integrateGpay(
                                        bookingId!!,
                                        txtOtherAmount!!.text.toString()
                                    )*/
                                } else if (payment_option == "Credit/Debit/Net Banking(Payment Gateway)") {
                                    startActivity(
                                        Intent(
                                            this@PayInstallmentActivity,
                                            PayUMoneyActivity::class.java
                                        )
                                            .putExtra(
                                                "AMOUNT",
                                                "" + txtAmount!!.text.toString()
                                            )
                                            .putExtra(
                                                "OTHERAMOUNT",
                                                "" + txtOtherAmount!!.text.toString()
                                            )
                                            .putExtra("bookingId", bookingId)
                                    )
                                } else {
                                    AlertDialogs().alertDialogOk(
                                        this@PayInstallmentActivity,
                                        "Alert",
                                        "Please select payment option",
                                        resources.getString(R.string.btn_ok),
                                        0,
                                        false,
                                        alertDialogOkListener
                                    )
                                }
                            } else {
                                AlertDialogs().alertDialogOk(
                                    this@PayInstallmentActivity,
                                    "Alert",
                                    "Other amount should be greater than 0",
                                    resources.getString(R.string.btn_ok),
                                    0,
                                    false,
                                    alertDialogOkListener
                                )
                            }
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayInstallmentActivity,
                                "Alert",
                                "Please enter vaild Amount",
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        }
                    } else {
                        if (txtAmount!!.text.toString() != "" && txtAmount!!.text.toString() != null) {
                            if (payment_option == "Cheque") {
                                if (bookingId != null) {

                                    AttemptToPayInstallment(
                                        userId,
                                        bookingId!!,
                                        "" + txtAmount!!.text.toString(),
                                        payment_option,
                                        edtBankDetail!!.text.toString(),
                                        "",
                                        txtOtherAmount!!.text.toString(),
                                        edtChequeNo!!.text.toString(),
                                        "0"
                                    )
                                }
                            } else if (payment_option == "RTGS") {
                                if (bookingId != null) {

                                    AttemptToPayInstallment(
                                        userId,
                                        bookingId!!,
                                        "" + txtAmount!!.text.toString(),
                                        payment_option,
                                        edtRtgsBankDetail!!.text.toString(),
                                        edtTxnId!!.text.toString(),
                                        txtOtherAmount!!.text.toString(),
                                        "",
                                        "0"
                                    )
                                }
                            } /*else if (payment_option.equals("Money Wallet")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        "", "",
                                        txtOtherAmount.getText().toString(), "", "0");

                            } else if (payment_option.equals("Gold Wallet")) {

                                AttemptToPayInstallment(VGoldApp.onGetUerId(), bookingId,
                                        "" + txtAmount.getText().toString(), payment_option,
                                        "", "",
                                        txtOtherAmount.getText().toString(), "", "0");

                            }*/ else if (payment_option == "UPI Payment") {
                                integrateGpay(bookingId!!, txtAmount!!.text.toString())
                            } else if (payment_option == "Credit/Debit/Net Banking(Payment Gateway)") {
                                startActivity(
                                    Intent(
                                        this@PayInstallmentActivity,
                                        PayUMoneyActivity::class.java
                                    )
                                        .putExtra(
                                            "AMOUNT",
                                            "" + txtAmount!!.text.toString()
                                        )
                                        .putExtra(
                                            "OTHERAMOUNT",
                                            "" + txtOtherAmount!!.text.toString()
                                        )
                                        .putExtra("bookingId", bookingId)
                                )
                            } else {
                                AlertDialogs().alertDialogOk(
                                    this@PayInstallmentActivity,
                                    "Alert",
                                    "Please select payment option",
                                    resources.getString(R.string.btn_ok),
                                    0,
                                    false,
                                    alertDialogOkListener
                                )
                            }
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayInstallmentActivity,
                                "Alert",
                                "Please enter vaild Amount",
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        }
                    }
                } else {
                    AlertDialogs().alertDialogOk(
                        this@PayInstallmentActivity,
                        "Alert",
                        "Please Select Payment Option",
                        resources.getString(R.string.btn_ok),
                        0,
                        false,
                        alertDialogOkListener
                    )
                }
            } else if (radioMoneyWallet!!.isChecked) {
                if (txtPayableAmount!!.text.toString() != null && !TextUtils.isEmpty(
                        txtPayableAmount!!.text.toString()
                    )
                ) {
                    val otherAmt =
                        java.lang.Double.valueOf(txtPayableAmount!!.text.toString())
                    if (moneyWalletBal != null && !TextUtils.isEmpty(moneyWalletBal) && !moneyWalletBal.equals(
                            "null",
                            ignoreCase = true
                        )
                    ) {
                        remainWalletAmt = moneyWalletBal!!.toDouble() - otherAmt
                        if (remainWalletAmt > 0) {
                            if (otherAmt > 0) {
                                ShowPopupDialog(null, "moneyWallet")
                            } else {
                                AlertDialogs().alertDialogOk(
                                    this@PayInstallmentActivity,
                                    "Alert",
                                    "Payable amount should be greater than 0",
                                    resources.getString(R.string.btn_ok),
                                    0,
                                    false,
                                    alertDialogOkListener
                                )
                            }
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayInstallmentActivity,
                                "Alert",
                                "Money Wallet balance is less then entered amount",
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        }
                    } else {
                        if (otherAmt > 0) {
                            ShowPopupDialog(null, "moneyWallet")
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayInstallmentActivity,
                                "Alert",
                                "Payable amount should be greater than 0",
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        }
                    }
                } else {
                    AlertDialogs().alertDialogOk(
                        this@PayInstallmentActivity,
                        "Alert",
                        "Please enter Payable Amount",
                        resources.getString(R.string.btn_ok),
                        0,
                        false,
                        alertDialogOkListener
                    )
                }
            } else if (radioGoldWallet!!.isChecked) {
                if (txtPayableAmount!!.text.toString() != null && !TextUtils.isEmpty(
                        txtPayableAmount!!.text.toString()
                    )
                ) {
                    val otherAmt =
                        java.lang.Double.valueOf(txtPayableAmount!!.text.toString())
                    if (GoldAmt != null && !TextUtils.isEmpty(GoldAmt) && !GoldAmt.equals(
                            "null",
                            ignoreCase = true
                        )
                    ) {
                        remainWalletAmt = GoldAmt!!.toDouble() - otherAmt
                        if (remainWalletAmt > 0) {
                            if (otherAmt > 0) {
                                ShowPopupDialog(dataModel, "goldWallet")
                            } else {
                                AlertDialogs().alertDialogOk(
                                    this@PayInstallmentActivity,
                                    "Alert",
                                    "Payable amount should be greater than 0",
                                    resources.getString(R.string.btn_ok),
                                    0,
                                    false,
                                    alertDialogOkListener
                                )
                            }
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayInstallmentActivity,
                                "Alert",
                                "Gold Wallet balance is less then entered amount",
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        }
                    } else {
                        if (otherAmt > 0) {
                            ShowPopupDialog(dataModel, "goldWallet")
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayInstallmentActivity,
                                "Alert",
                                "Payable amount should be greater than 0",
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        }
                    }
                } else {
                    AlertDialogs().alertDialogOk(
                        this@PayInstallmentActivity,
                        "Alert",
                        "Please enter Payable Amount",
                        resources.getString(R.string.btn_ok),
                        0,
                        false,
                        alertDialogOkListener
                    )
                }
            } else {
                AlertDialogs().alertDialogOk(
                    this@PayInstallmentActivity, "Alert", "Please Select Payment Mode",
                    resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                )
            }
        } else {
            AlertDialogs().alertDialogOk(
                this@PayInstallmentActivity, "Alert", "Please Select Booking Id",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
        }
    }

    private fun ShowPopupDialog(dataModel: PayInstallmentModel.Data?, key: String) {
        val dialog = Dialog(this@PayInstallmentActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.wallet_payment_dialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val window = dialog.window!!
        window.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        var btnYes = dialog.findViewById<AppCompatButton>(R.id.btnYes)
        var btnNo = dialog.findViewById<AppCompatButton>(R.id.btnNo)
        var txtBalanceRemainId = dialog.findViewById<TextView>(R.id.txtBalanceRemainId)
        var txtPayableAmtId = dialog.findViewById<TextView>(R.id.txtPayableAmtId)
        var txtSaleRateId = dialog.findViewById<TextView>(R.id.txtSaleRateId)
        var txtGSTAmtId = dialog.findViewById<TextView>(R.id.txtGSTAmtId)
        var txtWalletAmtId = dialog.findViewById<TextView>(R.id.txtWalletAmtId)
        var txtGSTPayableAmtId = dialog.findViewById<TextView>(R.id.txtGSTPayableAmtId)
        var txtDeductedGoldId = dialog.findViewById<TextView>(R.id.txtDeductedGoldId)
        if (key.equals("moneyWallet", ignoreCase = true)) {
            txtGSTAmtId.visibility = View.GONE
            txtGSTPayableAmtId.visibility = View.GONE
            txtDeductedGoldId.visibility = View.GONE
            txtSaleRateId.visibility = View.GONE
            txtPayableAmtId.text = ("Payable Amount : " + resources.getString(R.string.rs)
                    + txtPayableAmount!!.text.toString())
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
                txtPayableAmtId.text =
                    ("Payable Amount : " + resources.getString(R.string.rs)
                            + dataModel.amount_to_pay)
                txtWalletAmtId.text =
                    "Gold in Wallet : " + dataModel.gold_in_wallet.toString() + " gm"
                txtSaleRateId.text =
                    ("Today's Gold Sale Rate : " + resources.getString(R.string.rs)
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
                if (bookingId != null) {

                    AttemptToPayInstallment(
                        userId, bookingId!!,
                        "" + txtAmount!!.text.toString(), "Money Wallet",
                        "", "", txtPayableAmount!!.text.toString(),
                        "", "0"
                    )
                }
            } else {
                if (bookingId != null) {

                    AttemptToPayInstallment(
                        userId, bookingId!!,
                        "" + txtAmount!!.text.toString(), "Gold Wallet",
                        "", "",
                        txtPayableAmount!!.text.toString(), "", "1"
                    )
                }
            }
            dialog.dismiss()
        }
        btnNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    private fun integrateGpay(bookingId: String, amount: String) {
        var no = "00000"
        if (VGoldApp.onGetNo() != null && !TextUtils.isEmpty(VGoldApp.onGetNo())) {
            no = VGoldApp.onGetNo()!!.substring(0, 5)
        }
        val transNo = userId + "-" + BaseActivity.date
        val name: String?
        name =
            if (VGoldApp.onGetFirst() != null && !TextUtils.isEmpty(VGoldApp.onGetFirst())) {
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
            .authority("pay") //                        .appendQueryParameter("pa", "vgold@hdfcbank")
            .appendQueryParameter("pa", "vgold@hdfcbank")
            .appendQueryParameter("pn", "VGold Pvt. Ltd.")
            .appendQueryParameter("mc", "BCR2DN6TV633B7LW")
            .appendQueryParameter("tr", transNo)
            .appendQueryParameter("tn", "Inst $name($bookingId)")
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            /* .appendQueryParameter("url", "https://test.merchant.website")*/
            .build()

        /* Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
    //                        .appendQueryParameter("pa", "9881136531@okbizaxis")
                        .appendQueryParameter("pa", "vgold@hdfcbank")
                        .appendQueryParameter("pn", "VGold Pvt. Ltd.")
    //                        .appendQueryParameter("mc", "101222")
                        .appendQueryParameter("mc", "1012")
                        .appendQueryParameter("tr", transNo)
                        .appendQueryParameter("tn", "Inst " + name + "(" + bookingId + ")")
    //                        .appendQueryParameter("am", amount)
                        .appendQueryParameter("am", "10.00")
                        .appendQueryParameter("cu", "INR")
    //                        .appendQueryParameter("url", "your-transaction-url")
                        .build();*/


        /*Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "9881136531@okbizaxis")
                .appendQueryParameter("pn", name)
                .appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", transNo)
                .appendQueryParameter("tn", "Inst " + name + "(" + bookingId + ")")
                .appendQueryParameter("am", amount)
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
                this@PayInstallmentActivity,
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("main ", "response $resultCode")
        /*when (requestCode) {
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
        }*/
    }

    private fun upiPaymentDataOperation(data: ArrayList<String?>) {
        if (isConnectionAvailable(this@PayInstallmentActivity)) {
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
                    if (equalStr[0].lowercase(Locale.getDefault()) == "Status".lowercase(
                            Locale.getDefault()
                        )
                    ) {
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
//                Toast.makeText(PayInstallmentActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
//                Log.e("UPI", "payment successfull: "+approvalRefNo);
                if (bookingId != null) {

                    AttemptToPayInstallment(
                        userId, bookingId!!,
                        "" + txtAmount!!.text.toString(), payment_option,
                        "", approvalRefNo, txtOtherAmount!!.text.toString(),
                        "", "0"
                    )
                }
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(
                    this@PayInstallmentActivity,
                    "Payment cancelled by user.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "Cancelled by user: $approvalRefNo")
            } else {
                Toast.makeText(
                    this@PayInstallmentActivity,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "failed payment: $approvalRefNo")
            }
        } else {
            Log.e("UPI", "Internet issue: ")
            Toast.makeText(
                this@PayInstallmentActivity,
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


    private fun AttemptTogetDownPayment(gbid: String) {
        // mAlert.onShowProgressDialog(SignUpActivity.this, true);
        /* fetchDownPaymentServiceProvider?.getAddBankDetails(gbid, object : APICallback {
             override fun <T> onSuccess(serviceResponse: T) {
                 try {
                     val status: String? = (serviceResponse as FetchDownPaymentModel).getStatus()
                     val message: String? = (serviceResponse as FetchDownPaymentModel).getMessage()
                     val amount: String? =
                         (serviceResponse as FetchDownPaymentModel).getMonthly_installment()
                     if (status == "200") {
                         txtAmount!!.text = amount
                         txtRupee!!.text = resources.getString(R.string.rs)
                         txtAmount!!.visibility = View.VISIBLE

                         //                        if (radioOtherAmt.isChecked()) {
                         //                            txtOtherAmount.setVisibility(View.GONE);
                         //                        } else {
                         //                            txtOtherAmount.setVisibility(View.VISIBLE);
                         //                        }
                     } else {
                         txtAmount!!.text = ""
                         // mAlert.onShowToastNotification(PayInstallmentActivity.this, message);
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
                             this@PayInstallmentActivity,
                             (apiErrorModel as BaseServiceResponseModel).message
                         )
                     } else {
                         PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                 } finally {
                     progressDialog!!.hide()
                 }
             }
         })*/


        // change in api calling
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("gbid", gbid)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/fetch_down_payment.php")
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
                    var json = JSONObject(resp)
                    var status = json.get("status").toString()
                    var monthly_installment = json.optString("monthly_installment").toString()

                    runOnUiThread {
                        if (status == "200") {
                            txtAmount!!.text = monthly_installment
                            txtRupee!!.text = resources.getString(R.string.rs)
                            txtAmount!!.visibility = View.VISIBLE
                        } else {
                            txtAmount!!.text = ""
                        }
                    }
                }
            }
        })

    }

    private fun AttemptTogetBookingId(user_id: String?) {
        /*     getGoldBookingIdServiceProvider!!.getGoldBookingId(user_id, object : APICallback {
                 override fun <T> onSuccess(serviceResponse: T) {
                     try {
                         val status: String? = (serviceResponse as GetBookingIdModel).status
                         val message: String? = (serviceResponse as GetBookingIdModel).message
                         val mArrCity: ArrayList<GetBookingIdModel.Data>? =
                             (serviceResponse as GetBookingIdModel).data
                         if (status == "200") {
                             val maritalStatusSpinnerAdapter = ColorSpinnerAdapter(
                                 this@PayInstallmentActivity,
                                 R.layout.support_simple_spinner_dropdown_item, mArrCity!!
                             )
                             maritalStatusSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item)
                             maritalStatusSpinnerAdapter.notifyDataSetChanged()
                             spinner_goldBookingId!!.adapter = maritalStatusSpinnerAdapter
                             spinner_goldBookingId!!.onItemSelectedListener =
                                 object : AdapterView.OnItemSelectedListener {
                                     override fun onItemSelected(
                                         parent: AdapterView<*>?,
                                         view: View,
                                         position: Int,
                                         id: Long
                                     ) {
                                         bookingId =
                                             java.lang.String.valueOf(mArrCity?.get(position)!!.id)
                                         AttemptTogetDownPayment(bookingId!!)
                                     }

                                     override fun onNothingSelected(parent: AdapterView<*>?) {}
                                 }
                         } else {
                             AlertDialogs().alertDialogOk(
                                 this@PayInstallmentActivity, "Alert", message,
                                 resources.getString(R.string.btn_ok), 4, false, alertDialogOkListener
                             )
                             //                        mAlert.onShowToastNotification(PayInstallmentActivity.this, message);
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
                                 this@PayInstallmentActivity,
                                 (apiErrorModel as BaseServiceResponseModel).message
                             )
                         } else {
                             PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                         }
                     } catch (e: Exception) {
                         e.printStackTrace()
                         PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
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
            .url("https://www.vgold.co.in/dashboard/webservices/installment_booking_id.php")
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

                    val status: String? =
                        json.getString("status")
                    val message: String? =
                        json.getString("Message")

                    if (status.equals("200")) {
                        Log.e(" Response", resp)

                        runOnUiThread {

                            var prdds = json.getJSONArray("Data")
                            var cndList = ArrayList<GetBookingIdModel.Data>()
                            for (i in 0 until prdds.length()) {
                                var item = GetBookingIdModel.Data(
                                    prdds.getJSONObject(i).optString("id"),
                                    prdds.getJSONObject(i).optInt("is_paid")
                                )
                                cndList += item
                            }


                            var mArrCity: ArrayList<GetBookingIdModel.Data> =
                                cndList

                            var maritalStatusSpinnerAdapter = ColorSpinnerAdapter(
                                this@PayInstallmentActivity,
                                R.layout.support_simple_spinner_dropdown_item, mArrCity
                            )
                            maritalStatusSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item)
                            maritalStatusSpinnerAdapter.notifyDataSetChanged()
                            spinner_goldBookingId!!.adapter = maritalStatusSpinnerAdapter
                            spinner_goldBookingId!!.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View,
                                        position: Int,
                                        id: Long
                                    ) {
                                        bookingId =
                                            java.lang.String.valueOf(mArrCity.get(position).id)
                                        AttemptTogetDownPayment(bookingId!!)
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                                }
                        }

                    } else {
                        runOnUiThread(Runnable {
                            AlertDialogs().alertDialogOk(
                                this@PayInstallmentActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        })

                    }
                }
            }
        })

    }

    private fun AttemptToPayInstallment(
        user_id: String?, gbid: String, amountr: String,
        payment_option: String, bank_details: String,
        tr_id: String, otherAmount: String, cheque_no: String,
        confirmVal: String
    ) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        /*  payInstallmentServiceProvider?.payInstallment(user_id, gbid, amountr, payment_option,
              bank_details, tr_id, otherAmount, cheque_no, confirmVal, object : APICallback {
                  override fun <T> onSuccess(serviceResponse: T) {
                      try {
                          val status: String? = (serviceResponse as PayInstallmentModel).getStatus()
                          val message: String? = (serviceResponse as PayInstallmentModel).getMessage()
                          dataModel = (serviceResponse as PayInstallmentModel).getData()
                          if (status == "200") {
                              msg = message
                              if (payment_option.equals("Gold Wallet", ignoreCase = true) &&
                                  confirmVal.equals("0", ignoreCase = true)
                              ) {
                                  calculationLayout!!.visibility = View.VISIBLE
                                  txtGSTAmtId!!.visibility = View.VISIBLE
                                  txtGSTPayableAmtId!!.visibility = View.VISIBLE
                                  txtDeductedGoldId!!.visibility = View.VISIBLE
                                  txtSaleRateId!!.visibility = View.VISIBLE
                                  txtPayableAmtId!!.text =
                                      ("Payable Amount : " + resources.getString(R.string.rs)
                                              + dataModel!!.amount_to_pay)
                                  txtWalletAmtId!!.text =
                                      "Gold in Wallet : " + dataModel!!.gold_in_wallet
                                          .toString() + " gm"
                                  txtSaleRateId!!.text =
                                      ("Today's Gold Sale Rate : " + resources.getString(R.string.rs)
                                              + dataModel!!.today_sale_rate)
                                  txtGSTAmtId!!.text =
                                      "GST for Today's Gold Sale Rate : " + dataModel!!.gst_per
                                          .toString() + "%"
                                  txtGSTPayableAmtId!!.text =
                                      ("GST on Payable Amount : " + resources.getString(R.string.rs)
                                              + dataModel!!.amount_to_pay_gst)
                                  txtDeductedGoldId!!.text =
                                      "Deducted Gold from Wallet : " + dataModel!!.gold_reduce
                                          .toString() + " gm"
                                  txtBalanceRemainId!!.text = ("Remaining Gold in Wallet : "
                                          + dataModel!!.reduced_gold_in_wallet) + " gm"

                                  //                                    ShowPopupDialog(dataModel, "goldWallet");
                              } else {
                                  val intent =
                                      Intent(this@PayInstallmentActivity, SuccessActivity::class.java)
                                  intent.putExtra("message", message)
                                  startActivity(intent)
                              }
                          } else {
                              AlertDialogs().alertDialogOk(
                                  this@PayInstallmentActivity,
                                  "Alert",
                                  message,
                                  resources.getString(R.string.btn_ok),
                                  0,
                                  false,
                                  alertDialogOkListener
                              )

                              //                        mAlert.onShowToastNotification(PayInstallmentActivity.this, message);
                              //                        Intent intent=new Intent(PayInstallmentActivity.this,MainActivity.class);
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
                                  this@PayInstallmentActivity,
                                  (apiErrorModel as BaseServiceResponseModel).message
                              )
                          } else {
                              PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                          }
                      } catch (e: Exception) {
                          e.printStackTrace()
                          PrintUtil.showNetworkAvailableToast(this@PayInstallmentActivity)
                      } finally {
                          progressDialog!!.hide()
                      }
                  }
              })
    */

        // change in api calling
        var client = OkHttpClient().newBuilder().build()
        var requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("gbid", gbid)
            .addFormDataPart("amountr", amountr)
            .addFormDataPart("payment_option", payment_option)
            .addFormDataPart("bank_details", bank_details)
            .addFormDataPart("tr_id", tr_id)
            .addFormDataPart("amount_other", otherAmount)
            .addFormDataPart("cheque_no", cheque_no)
            .addFormDataPart("confirmed", confirmVal)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/installment.php")
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
                var json = JSONObject(resp)
                // Stuff that updates the UI
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    // val status = json.get("status").toString()
                    var status: String? =
                        json.getString("status")
                    var message: String? =
                        json.getString("Message")
                    var data: JSONObject? = json.optJSONObject("data")


                    var newData: PayInstallmentModel.Data? = PayInstallmentModel.Data()
                    newData?.today_sale_rate = data?.optString("today_sale_rate")
                    newData?.amount_to_pay = data?.optString("amount_to_pay")
                    newData?.amount_to_pay_gst = data?.optString("amount_to_pay_gst")
                    newData?.gst_per = data?.optString("gst_per")
                    newData?.gold_in_wallet = data?.optString("gold_in_wallet")
                    newData?.gold_reduce = data?.optString("gold_reduce")
                    newData?.reduced_gold_in_wallet = data?.optString("reduced_gold_in_wallet")


                    runOnUiThread {
                        if (status == "200") {
                            dataModel = newData
                            msg = message
                            if (payment_option.equals("Gold Wallet", ignoreCase = true) &&
                                confirmVal.equals("0", ignoreCase = true)
                            ) {

                                calculationLayout!!.visibility = View.VISIBLE
                                txtGSTAmtId!!.visibility = View.VISIBLE
                                txtGSTPayableAmtId!!.visibility = View.VISIBLE
                                txtDeductedGoldId!!.visibility = View.VISIBLE
                                txtSaleRateId!!.visibility = View.VISIBLE
                                txtPayableAmtId!!.text =
                                    ("Payable Amount : " + resources.getString(R.string.rs)
                                            + dataModel!!.amount_to_pay)
                                txtWalletAmtId!!.text =
                                    "Gold in Wallet : " + dataModel!!.gold_in_wallet
                                        .toString() + " gm"
                                txtSaleRateId!!.text =
                                    ("Today's Gold Sale Rate : " + resources.getString(R.string.rs)
                                            + dataModel!!.today_sale_rate)
                                txtGSTAmtId!!.text =
                                    "GST for Today's Gold Sale Rate : " + dataModel!!.gst_per
                                        .toString() + "%"
                                txtGSTPayableAmtId!!.text =
                                    ("GST on Payable Amount : " + resources.getString(R.string.rs)
                                            + dataModel!!.amount_to_pay_gst)
                                txtDeductedGoldId!!.text =
                                    "Deducted Gold from Wallet : " + dataModel!!.gold_reduce
                                        .toString() + " gm"
                                txtBalanceRemainId!!.text = ("Remaining Gold in Wallet : "
                                        + dataModel!!.reduced_gold_in_wallet) + " gm"

                                //                                    ShowPopupDialog(dataModel, "goldWallet");
                            } else {
                                val intent =
                                    Intent(
                                        this@PayInstallmentActivity,
                                        SuccessActivity::class.java
                                    )
                                intent.putExtra("message", message)
                                startActivity(intent)
                            }
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayInstallmentActivity,
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
                val intent =
                    Intent(this@PayInstallmentActivity, SuccessActivity::class.java)
                intent.putExtra("message", msg)
                startActivity(intent)
            }
            4 -> {
                val intentHome =
                    Intent(this@PayInstallmentActivity, MainActivity::class.java)
                startActivity(intentHome)
            }
            11 -> {
                val LogIntent =
                    Intent(this@PayInstallmentActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

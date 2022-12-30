package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.addmoney.AddMoneyModel
import com.cognifygroup.vgold.activities.addmoney.AddMoneyServiceProvider
import com.cognifygroup.vgold.addgold.AddGoldModel
import com.cognifygroup.vgold.addgold.AddGoldServiceProvider
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.payInstallment.PayInstallmentModel
import com.cognifygroup.vgold.payInstallment.PayInstallmentServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class PayUMoneyActivity : AppCompatActivity(),AlertDialogOkListener {
    //    AddWalletMoneyServiceProvider addWalletMoneyServiceProvider;
    var webView: WebView? = null

    /*String merchant_key = "0dCMZ3lW";
    String salt = "VMeDRpi6aH";*/

    /*String merchant_key = "0dCMZ3lW";
    String salt = "VMeDRpi6aH";*/
    var merchant_key = "SzsTlQ"
    var salt = "Qc3n63C6"

    var action1 = ""
    var base_url = "https://www.secure.payu.in"

    // String base_url = "https://test.payu.in";
    //int error = 0;
    // String hashString = "";
    // Map<String, String> params;
    var txnid: String? = null
    var amount = ""
    var otherAmount = ""
    var productInfo = ""
    var firstName = ""
    var emailId = ""

    private val SUCCESS_URL = "https://www.payumoney.com/payments/guestcheckout/#/success"
    private val FAILED_URL = "https://www.PayUmoney.com/mobileapp/PayUmoney/failure.php"
    private var phone = ""
    private val serviceProvider = "payu_paisa"
    private var hash = ""

//    private var mHandler = Handler()
    private var mAlert: AlertDialogs? = null
    private val mBookingId: String? = null

    var addMoneyServiceProvider: AddMoneyServiceProvider? = null
    var addGoldServiceProvider: AddGoldServiceProvider? = null
    var payInstallmentServiceProvider: PayInstallmentServiceProvider? = null
    var whichActivity = ""
    var goldWeight = ""
    var bookingId = ""
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this

    @SuppressLint("AddJavascriptInterface", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_umoney)

        // getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Pay Money "

        webView = findViewById(R.id.webview_payment)

        init()
    }

    @SuppressLint("JavascriptInterface")
    private fun init() {
        progressDialog = TransparentProgressDialog(this@PayUMoneyActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        progressDialog!!.show()
        addMoneyServiceProvider = AddMoneyServiceProvider(this)
        addGoldServiceProvider = AddGoldServiceProvider(this)
        payInstallmentServiceProvider = PayInstallmentServiceProvider(this)
        val intent = intent
        if (getIntent() != null && getIntent().hasExtra("AMOUNT")) {
            amount = intent.getStringExtra("AMOUNT")!!
            otherAmount = intent.getStringExtra("OTHERAMOUNT")!!
            whichActivity = intent.getStringExtra("whichActivity")!!
            // Toast.makeText(PayUMoneyActivity.this,whichActivity,Toast.LENGTH_LONG).show();
            goldWeight = intent.getStringExtra("goldweight")!!
            bookingId = intent.getStringExtra("bookingId")!!
        }
        emailId = "" + VGoldApp.onGetEmail()
        phone = "" + VGoldApp.onGetNo()
        //amount = MiniXeroxApp.onGetPdfCost();
        // mBookingId = MiniXeroxApp.onGetPdfId();
        /* Bundle lBundle = getIntent().getExtras();
        amount = lBundle.getString("AMOUNT");
        mBookingId = lBundle.getString("BOOKINGID");*/
        // phone = MedoApplication.onGetUser();
        //emailId=MedoApplication.onGetEmail();
        txnid = "TXN" + System.currentTimeMillis()
        val productInfoObj = JSONObject()
        val productPartsArr = JSONArray()
        val productPartsObj1 = JSONObject()
        val paymentIdenfierParent = JSONObject()
        val paymentIdentifiersArr = JSONArray()
        val paymentPartsObj1 = JSONObject()
        val paymentPartsObj2 = JSONObject()
        try {
            // Payment Parts
            productPartsObj1.put("name", "abc")
            productPartsObj1.put("description", "abcd")
            productPartsObj1.put("value", "1000")
            productPartsObj1.put("isRequired", "true")
            productPartsObj1.put("settlementEvent", "EmailConfirmation")
            productPartsArr.put(productPartsObj1)
            productInfoObj.put("paymentParts", productPartsArr)

            // Payment Identifiers
            paymentPartsObj1.put("field", "CompletionDate")
            paymentPartsObj1.put("value", "31/10/2012")
            paymentIdentifiersArr.put(paymentPartsObj1)
            paymentPartsObj2.put("field", "TxnId")
            paymentPartsObj2.put("value", txnid)
            paymentIdentifiersArr.put(paymentPartsObj2)
            paymentIdenfierParent.put(
                "paymentIdentifiers",
                paymentIdentifiersArr
            )
            productInfoObj.put("", paymentIdenfierParent)
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        productInfo = productInfoObj.toString()

//        Log.e("TAG", productInfoObj.toString());
        val rand = Random()
        val rndm = (Integer.toString(rand.nextInt())
                + System.currentTimeMillis() / 1000L)
        txnid = hashCal("SHA-256", rndm).substring(0, 20)
        hash = hashCal(
            "SHA-512", merchant_key + "|" + txnid + "|" + amount
                    + "|" + productInfo + "|" + firstName + "|" + emailId
                    + "|||||||||||" + salt
        )
        action1 = "$base_url/_payment"
        webView!!.webViewClient = object : WebViewClient() {
            private var webViewPreviousState = 0
            private val PAGE_STARTED = 0x1
            private val PAGE_REDIRECTED = 0x2
            override fun shouldOverrideUrlLoading(view: WebView, urlNewString: String): Boolean {
                webViewPreviousState = PAGE_REDIRECTED
                webView!!.loadUrl(urlNewString)

                /*  if (urlNewString.startsWith(SUCCESS_URL)) {
                        Log.e("Url", "" + urlNewString);
                        AttemptAddPaymentInformation(MiniXeroxApp.onGetPdfId(),MiniXeroxApp.onGetUploadedPdfUserId(),MiniXeroxApp.onGetUserId(),txnid,amount,"Success");
                       // AlertDialogs.getInstance().onShowToastNotification(PayUMoneyActivity.this, "Payment Successful!");
                        // Toast.makeText(PayUMoneyActivity.this, "Payment Successful " + urlNewString, Toast.LENGTH_LONG).show();
                      //  RequestModel lRequest = new RequestModel();
                       // lRequest.setAPImethod("payment_status_of_booking?booking_id=" + mBookingId + "&&payment_status=Success");
                       // new JsonRequestClass(PayUMoneyActivity.this).onJsonObjReq(PayUMoneyActivity.this, lRequest);
                        Intent lIntent=new Intent(PayUMoneyActivity.this,MainActivity.class);
                       // lIntent.putExtras(getIntent().getExtras());
                        startActivity(lIntent);
                        finish();
                    } else if (urlNewString.startsWith(FAILED_URL)) {
                        AttemptAddPaymentInformation(MiniXeroxApp.onGetPdfId(),MiniXeroxApp.onGetUploadedPdfUserId(),MiniXeroxApp.onGetUserId(),txnid,amount,"failure");
                        AlertDialogs.getInstance().onShowToastNotification(PayUMoneyActivity.this, "Payment failure!");

                    }*/return true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                webViewPreviousState = PAGE_STARTED
            }

            override fun onPageFinished(view: WebView, url: String) {
                var url = url
                val index = url.lastIndexOf('/')
                url = url.substring(0, index)
                //                Log.e("URL", url);
                if (webViewPreviousState == PAGE_STARTED) {
                    progressDialog!!.hide()
                }
                if (url == SUCCESS_URL) {
                    // AttemptAddPaymentInformation(MiniXeroxApp.onGetPdfId(),MiniXeroxApp.onGetUploadedPdfUserId(),MiniXeroxApp.onGetUserId(),txnid,amount,"Success");
                    if (whichActivity == "money") {
                        AttemptToAddMoney(
                            VGoldApp.onGetUerId(),
                            amount,
                            "Payumoney",
                            "",
                            txnid!!,
                            ""
                        )
                    } else if (whichActivity == "gold") {
                        AttemptToAddGold(
                            VGoldApp.onGetUerId(), goldWeight, "" + amount, "Payumoney", "",
                            txnid!!, ""
                        )
                    } else if (whichActivity == "installment") {
                        AttemptToPayInstallment(
                            VGoldApp.onGetUerId(), bookingId,
                            "" + amount, "Payumoney",
                            "", txnid!!, otherAmount, ""
                        )
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@PayUMoneyActivity, "Alert", "Something went wrong",
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )

                        //                        mAlert.onShowToastNotification(PayUMoneyActivity.this, "Something went wrong");
                    }
                } else if (url == FAILED_URL) {
                    //  AttemptAddPaymentInformation(MiniXeroxApp.onGetPdfId(),MiniXeroxApp.onGetUploadedPdfUserId(),MiniXeroxApp.onGetUserId(),txnid,amount,"failure");
                    AlertDialogs().alertDialogOk(
                        this@PayUMoneyActivity, "Alert", "Payment failure!",
                        resources.getString(R.string.btn_ok), 1, false, alertDialogOkListener
                    )

                    //                    AlertDialogs.getInstance().onShowToastNotification(PayUMoneyActivity.this, "Payment failure!");
                    /* Intent lIntent=new Intent(PayUMoneyActivity.this,MainActivity.class);
                        startActivity(lIntent);
                        finish();*/
                }
            }
        }
        webView!!.visibility = View.VISIBLE
        webView!!.settings.builtInZoomControls = true
        webView!!.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView!!.settings.domStorageEnabled = true
        webView!!.clearHistory()
        webView!!.clearCache(true)
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.setSupportZoom(true)
        webView!!.settings.useWideViewPort = false
        webView!!.settings.loadWithOverviewMode = false
        webView!!.addJavascriptInterface(PayUJavaScriptInterface(this), "PayUMoney")
        val mapParams: MutableMap<String, String> = HashMap()
        mapParams["key"] = merchant_key
        mapParams["hash"] = hash
        mapParams["txnid"] = txnid!!
        mapParams["service_provider"] = "payu_paisa"
        mapParams["amount"] = amount
        mapParams["firstname"] = firstName
        mapParams["email"] = emailId
        mapParams["phone"] = phone
        mapParams["productinfo"] = productInfo
        // mapParams.put("surl", SUCCESS_URL);
        mapParams["furl"] = FAILED_URL
        mapParams["udf1"] = ""
        mapParams["udf2"] = ""
        mapParams["udf3"] = ""
        mapParams["udf4"] = ""
        mapParams["udf5"] = ""
        webview_ClientPost(webView!!, action1, mapParams.entries)
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            1 -> {
                val lIntent = Intent(this@PayUMoneyActivity, MainActivity::class.java)
                startActivity(lIntent)
                finish()
            }
        }
    }
/*
    @Override
    public void onGetResponse(String res) {
        ResponseModel lResponse = new ResponseModel();
        lResponse = new Gson().fromJson(res, lResponse.getClass());

        if (lResponse.status == 200) {
          //  MedoApplication.isGetAPI = true;
           // RequestModel lRequest = new RequestModel();
            //lRequest.setAPImethod("sendhttp.php?authkey=126333Ad7Wu8GR9jt57e76845&mobiles="+phone+"&sender=CONFRM&route=4&country=0&message=Your Booking Successfully");
            ///new JsonRequestClass((ResponseListner) PayUMoneyActivity.this).onJsonObjReq(PayUMoneyActivity.this, lRequest);


        } else {

            AlertDialogs.getInstance().onShowToastNotification(this, lResponse.message);
        }
    }*/

    /*
    @Override
    public void onGetResponse(String res) {
        ResponseModel lResponse = new ResponseModel();
        lResponse = new Gson().fromJson(res, lResponse.getClass());

        if (lResponse.status == 200) {
          //  MedoApplication.isGetAPI = true;
           // RequestModel lRequest = new RequestModel();
            //lRequest.setAPImethod("sendhttp.php?authkey=126333Ad7Wu8GR9jt57e76845&mobiles="+phone+"&sender=CONFRM&route=4&country=0&message=Your Booking Successfully");
            ///new JsonRequestClass((ResponseListner) PayUMoneyActivity.this).onJsonObjReq(PayUMoneyActivity.this, lRequest);


        } else {

            AlertDialogs.getInstance().onShowToastNotification(this, lResponse.message);
        }
    }*/
    class PayUJavaScriptInterface
    /**
     * Instantiate the interface and set the context
     */ internal constructor(var mContext: Context) {
        fun success(id: Long, paymentId: String?) {

            Handler().post(Runnable {
//                mHandler = null
                val alertDialogOkListener = null
                AlertDialogs().alertDialogOk(
                    mContext, "Alert", "Payment Successfull.",
                    mContext.getResources().getString(R.string.btn_ok), 0, false, alertDialogOkListener
                )
                //                    mAlert.onShowToastNotification(PayUMoneyActivity.this, "Payment Successfully.");
            })
        }
    }

    fun webview_ClientPost(
        webView: WebView, url: String?,
        postData: Collection<Map.Entry<String, String>>
    ) {
        val sb = StringBuilder()
        sb.append("<html><head></head>")
        sb.append("<body onload='form1.submit()'>")
        sb.append(
            String.format(
                "<form id='form1' action='%s' method='%s'>",
                url, "post"
            )
        )
//        for ((key, value) : Map.Entry<String, String> in postData) {
//            sb.append(
//                String.format(
//                    "<input name='%s' type='hidden' value='%s' />",
//                    key, value
//                )
//            )
//        }
        sb.append("</form></body></html>")
        //        Log.d("Test", "webview_ClientPost called");
        webView.loadData(sb.toString(), "text/html", "utf-8")
    }

    fun empty(s: String?): Boolean {
        return if (s == null || s.trim { it <= ' ' } == "") true else false
    }


    private fun AttemptToAddMoney(
        user_id: String?,
        amount: String,
        payment_option: String,
        bank_details: String,
        tr_id: String,
        cheque_no: String
    ) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        addMoneyServiceProvider!!.getAddBankDetails(
            user_id!!,
            amount,
            payment_option,
            bank_details,
            tr_id,
            cheque_no,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status = (serviceResponse as AddMoneyModel).getStatus()
                        val message = (serviceResponse as AddMoneyModel).getMessage()
                        if (status == "200") {
                            AlertDialogs().alertDialogOk(
                                this@PayUMoneyActivity,
                                "Alert",
                                "Payment Successfull.",
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

//                          mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent=new Intent(PayUMoneyActivity.this,MainActivity.class);
//                        startActivity(intent);
//                        finish();
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayUMoneyActivity,
                                "Alert",
                                "Payment Successfull.",
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent=new Intent(PayUMoneyActivity.this,MainActivity.class);
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
                                this@PayUMoneyActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@PayUMoneyActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@PayUMoneyActivity)
                    } finally {
                        progressDialog!!.hide()
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
        addGoldServiceProvider!!.getAddBankDetails(user_id, gold, amount, payment_option,
            bank_details, tr_id, cheque_no, object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status = (serviceResponse as AddGoldModel).getStatus()
                        val message = (serviceResponse as AddGoldModel).getMessage()
                        if (status == "200") {
                            AlertDialogs().alertDialogOk(
                                this@PayUMoneyActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent = new Intent(PayUMoneyActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayUMoneyActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent = new Intent(PayUMoneyActivity.this, MainActivity.class);
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
                                this@PayUMoneyActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@PayUMoneyActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@PayUMoneyActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
    }


    private fun AttemptToPayInstallment(
        user_id: String?, gbid: String, amountr: String,
        payment_option: String, bank_details: String,
        tr_id: String, otherAmount: String, cheque_no: String
    ) {
        // mAlert.onShowProgressDialog(AddBankActivity.this, true);
        payInstallmentServiceProvider!!.payInstallment(user_id, gbid, amountr, payment_option,
            bank_details, tr_id, otherAmount, cheque_no, "0", object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? = (serviceResponse as PayInstallmentModel).getStatus()
                        val message: String? = (serviceResponse as PayInstallmentModel).getMessage()
                        if (status == "200") {
                            AlertDialogs().alertDialogOk(
                                this@PayUMoneyActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

//                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent = new Intent(PayUMoneyActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PayUMoneyActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )
                            //                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
//                        Intent intent = new Intent(PayUMoneyActivity.this, MainActivity.class);
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
                                this@PayUMoneyActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@PayUMoneyActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@PayUMoneyActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
    }

    /*  private void addMoneyToWalletApi(String pay_bye,String rupees,String transaction_id,String user_id) {

        mAlert.onShowProgressDialog(PayUMoneyActivity.this, true);
        addWalletMoneyServiceProvider.addWalletMoney(pay_bye,rupees,transaction_id,user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    int Status = ((AddWalletMoneyModel) serviceResponse).getStatus();

                    String message = ((AddWalletMoneyModel) serviceResponse).getMessage();
                    String balance = String.valueOf(((AddWalletMoneyModel) serviceResponse).getBalance());
                    if (Status == 200) {
                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
                        startActivity(new Intent(PayUMoneyActivity.this,MainActivity.class));
                        finish();

                    } else {
                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mAlert.onShowProgressDialog(PayUMoneyActivity.this, false);
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {
                try {

                    if (apiErrorModel != null) {
                        PrintUtil.showToast(PayUMoneyActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                } finally {
                    mAlert.onShowProgressDialog(PayUMoneyActivity.this, false);
                }
            }
        });
    }*/

    /*  private void addMoneyToWalletApi(String pay_bye,String rupees,String transaction_id,String user_id) {

        mAlert.onShowProgressDialog(PayUMoneyActivity.this, true);
        addWalletMoneyServiceProvider.addWalletMoney(pay_bye,rupees,transaction_id,user_id, new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    int Status = ((AddWalletMoneyModel) serviceResponse).getStatus();

                    String message = ((AddWalletMoneyModel) serviceResponse).getMessage();
                    String balance = String.valueOf(((AddWalletMoneyModel) serviceResponse).getBalance());
                    if (Status == 200) {
                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
                        startActivity(new Intent(PayUMoneyActivity.this,MainActivity.class));
                        finish();

                    } else {
                        mAlert.onShowToastNotification(PayUMoneyActivity.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mAlert.onShowProgressDialog(PayUMoneyActivity.this, false);
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {
                try {

                    if (apiErrorModel != null) {
                        PrintUtil.showToast(PayUMoneyActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMessage());
                    } else {
                        PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(PayUMoneyActivity.this);
                } finally {
                    mAlert.onShowProgressDialog(PayUMoneyActivity.this, false);
                }
            }
        });
    }*/
    fun hashCal(type: String?, str: String): String {
        val hashseq = str.toByteArray()
        val hexString = StringBuffer()
        try {
            val algorithm = MessageDigest.getInstance(type)
            algorithm.reset()
            algorithm.update(hashseq)
            val messageDigest = algorithm.digest()
            for (i in messageDigest.indices) {
                val hex = Integer.toHexString(0xFF and messageDigest[i].toInt())
                if (hex.length == 1) hexString.append("0")
                hexString.append(hex)
            }
        } catch (nsae: NoSuchAlgorithmException) {
        }
        return hexString.toString()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this@PayUMoneyActivity, MainActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
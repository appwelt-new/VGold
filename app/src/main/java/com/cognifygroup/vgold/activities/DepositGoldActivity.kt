package com.cognifygroup.vgold.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.cognifygroup.vgold.BuildConfig
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.getmaturityweight.MaturityWeightServiceProvider
import com.cognifygroup.vgold.golddepositrequest.DepositeRequestServiceProvider
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.cognifygroup.vgold.vendorfordeposit.VendorForDepositeModel
import com.cognifygroup.vgold.vendorfordeposit.VendorForDepositeServiceProvider
import com.bumptech.glide.Glide
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class DepositGoldActivity : AppCompatActivity(), AlertDialogOkListener {

    var edtgoldWeight: EditText? = null
    var spinner_tennure_deposite: Spinner? = null
    var txtMaturityWeight: TextView? = null
    var txtDepositeCharge: TextView? = null
    var spinner_willingToDeposite: Spinner? = null
    var btnSendDepositeRequest: Button? = null
    var edtRemark: EditText? = null
    var objbnkdetiv: ImageView? = null
    var btnShareBankDetails: Button? = null
    var shreiv: ImageView? = null
    //var bnkdethideiv: ImageView? = null

    var edtPurity: EditText? = null

    var tennure: String? = null
    var bankGurantee: kotlin.String? = null
    var vendor_id: kotlin.String? = null
    var firmName: kotlin.String? = null
    private var msg: String? = null

    var mAlert: AlertDialogs? = null
    var maturityWeightServiceProvider: MaturityWeightServiceProvider? = null
    //lateinit var vendorForDepositeServiceProvider: VendorForDepositeServiceProvider
    lateinit var depositeRequestServiceProvider: DepositeRequestServiceProvider
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this

    var timer = Timer()
    val DELAY: Long = 1000 // milliseconds

    //  private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_gold)
        supportActionBar?.title = "Gold Deposit"

        spinner_tennure_deposite = findViewById(R.id.spinner_tennure_deposite)
        edtgoldWeight = findViewById(R.id.edtgoldWeight)
        txtDepositeCharge = findViewById(R.id.txtDepositeCharge)
        spinner_willingToDeposite = findViewById(R.id.spinner_willingToDeposite)
        btnSendDepositeRequest = findViewById(R.id.btnSendDepositeRequest)
        btnShareBankDetails = findViewById(R.id.btnShareBankDetails)
        edtPurity = findViewById(R.id.edtPurity)
        edtRemark = findViewById(R.id.edtRemark)
        txtMaturityWeight = findViewById(R.id.txtMaturityWeight)
        objbnkdetiv = findViewById(R.id.bnkdetiv)
        shreiv = findViewById(R.id.shreiv)
//        bnkdethideiv = findViewById(R.id.bnkdethideiv)

        Glide.with(this)
            .load("https://vgold.co.in/dashboard/vgold_rate/bank%20details.png")
            .into(objbnkdetiv!!);

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        init()

        btnSendDepositeRequest!!.setOnClickListener {
            onClickOfBtnSendRequest()
        }

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


        edtgoldWeight!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length != 0) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                // TODO: do what you need here (refresh list)
                                // you will probably need to use runOnUiThread(Runnable action) for some specific actions (e.g. manipulating views)
                                AttemptToGetMaturityWeight(s.toString(), tennure!!, "no")
                                AttemptToGetDepositeCharges(s.toString())
                            }
                        },
                        DELAY
                    )
                }
            }
        })

    }

    fun init() {

        progressDialog = TransparentProgressDialog(this@DepositGoldActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        // maturityWeightServiceProvider = MaturityWeightServiceProvider()
      //  vendorForDepositeServiceProvider = VendorForDepositeServiceProvider(this)
        // depositeRequestServiceProvider = DepositeRequestServiceProvider(this)
        AttemptToGetVendorForDeposite()
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.Tennure_array, android.R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        // Apply the adapter to the spinner
        spinner_tennure_deposite!!.adapter = adapter
        spinner_tennure_deposite!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    val paymentoption = parent.getItemAtPosition(pos) as String
                    tennure = if (paymentoption == "12 month") {
                        "12"
                    } else if (paymentoption == "24 month") {
                        "24"
                    } else if (paymentoption == "36 month") {
                        "36"
                    } else if (paymentoption == "48 month") {
                        "48"
                    } else if (paymentoption == "60 month") {
                        "60"
                    } else if (paymentoption == "72 month") {
                        "72"
                    } else if (paymentoption == "84 month") {
                        "84"
                    } else if (paymentoption == "96 month") {
                        "96"
                    } else if (paymentoption == "108 month") {
                        "108"
                    } else if (paymentoption == "120 month") {
                        "120"
                    } else {
                        ""
                    }
                    AttemptToGetMaturityWeight(edtgoldWeight!!.text.toString(), tennure!!, "no")

                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        // loginStatusServiceProvider = LoginStatusServiceProvider(this)
        //   checkLoginSession()
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
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClickOfBtnSendRequest() {
        AttemptToGetDepositeRequest(
            VGoldApp.onGetUerId(),
            edtgoldWeight!!.text.toString(),
            tennure!!,
            txtMaturityWeight!!.text.toString(),
            txtDepositeCharge!!.text.toString(),
            vendor_id!!,
            edtPurity!!.text.toString().trim { it <= ' ' },
            edtRemark!!.text.toString().trim { it <= ' ' },
            "no"
        )
    }


    fun onRadioButtonClicked(view: View) {
        // Is the button now checked?
        val checked = (view as RadioButton).isChecked
        when (view.getId()) {
            R.id.radio_withBank -> {
                if (checked) bankGurantee = "yes"
                // Pirates are the best
                bankGurantee?.let {
                    AttemptToGetMaturityWeight(
                        edtgoldWeight!!.text.toString(),
                        tennure!!, it
                    )
                }
            }
            R.id.radio_withoutBank -> {
                if (checked) bankGurantee = "no"
                bankGurantee?.let {
                    AttemptToGetMaturityWeight(
                        edtgoldWeight!!.text.toString(),
                        tennure!!, it
                    )
                }
            }
        }
    }


    private fun AttemptToGetVendorForDeposite() {
        // mAlert.onShowProgressDialog(SignUpActivity.this, true);
        /*vendorForDepositeServiceProvider?.getVendorForDeposite(object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    val status: String? = (serviceResponse as VendorForDepositeModel).status
                    val message: String? = (serviceResponse as VendorForDepositeModel).message
                    val mArrCity: ArrayList<VendorForDepositeModel.Data>? =
                        (serviceResponse as VendorForDepositeModel).data
                    if (status == "200") {
                        val adapter: ArrayAdapter<VendorForDepositeModel.Data> =
                            ArrayAdapter<VendorForDepositeModel.Data>(
                                this@DepositGoldActivity,
                                R.layout.support_simple_spinner_dropdown_item,
                                mArrCity!!
                            )
                        //                        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                        spinner_willingToDeposite!!.adapter = adapter
                        spinner_willingToDeposite!!.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    vendor_id =
                                        java.lang.String.valueOf(mArrCity[position].vendor_id)
                                    firmName =
                                        java.lang.String.valueOf(mArrCity[position].firm_name)
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@DepositGoldActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                        mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
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
                            this@DepositGoldActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@DepositGoldActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@DepositGoldActivity)
                } finally {
                    progressDialog?.hide()
                }
            }
        })*/

        // change in api
        val client = OkHttpClient().newBuilder().build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/vendor_for_deposite.php")
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
                    var json = JSONObject(resp)
                    var status = json.get("status").toString()
                    var message = json.get("Message").toString()
                    runOnUiThread {
                        if (status == "200") {
                            var data = json.getJSONArray("Data")
                            var dataList = ArrayList<VendorForDepositeModel.Data>()
                            for (i in 0 until data.length()) {
                                val item = VendorForDepositeModel.Data()
                                item.firm_name = data.optJSONObject(i).optString("firm_name")
                                item.vendor_id = data.optJSONObject(i).optString("vendor_id")
                                dataList += item
                            }


                            var mArrCity: ArrayList<VendorForDepositeModel.Data>? = dataList
                            var adapter: ArrayAdapter<VendorForDepositeModel.Data> =
                                ArrayAdapter<VendorForDepositeModel.Data>(
                                    this@DepositGoldActivity,
                                    R.layout.support_simple_spinner_dropdown_item,
                                    mArrCity!!
                                )
                            //                        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            adapter.setDropDownViewResource(R.layout.custom_spinner_item)
                            //  adapter.notifyDataSetChanged()
                            spinner_willingToDeposite!!.adapter = adapter
                            spinner_willingToDeposite!!.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View,
                                        position: Int,
                                        id: Long
                                    ) {
                                        vendor_id =
                                            java.lang.String.valueOf(mArrCity.get(position).vendor_id)
                                        firmName =
                                            java.lang.String.valueOf(mArrCity.get(position).firm_name)
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                                }
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@DepositGoldActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                            //                        mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
                        }
                    }
                }
            }
        })
    }

    private fun AttemptToGetMaturityWeight(
        gold_weight: String,
        tennure: String,
        guarantee: String
    ) {
        //  progressDialog.show();
        /* maturityWeightServiceProvider?.getMaturityWeight(
             gold_weight,
             tennure,
             guarantee,
             object : APICallback {
                 override fun <T> onSuccess(serviceResponse: T) {
                     try {
                         val status: String? = (serviceResponse as MaturityWeightModel).getStatus()
                         val message: String? = (serviceResponse as MaturityWeightModel).getMessage()
                         val maturityWeight: String? =
                             (serviceResponse as MaturityWeightModel).getData()
                         txtMaturityWeight!!.text = maturityWeight
                         if (status == "200") {

                                mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
                         Intent intent=new Intent(GoldDepositeActivity.this,MainActivity.class);
                         startActivity(intent);
                     } else {
                         AlertDialogs().alertDialogOk(
                             this@DepositGoldActivity,
                             "Alert",
                             message,
                             resources.getString(R.string.btn_ok),
                             0,
                             false,
                             alertDialogOkListener
                         )

                         //                        mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
                          Intent intent=new Intent(GoldDepositeActivity.this,MainActivity.class);
                         startActivity(intent);
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
                             this@DepositGoldActivity,
                             (apiErrorModel as BaseServiceResponseModel).message
                         )
                     } else {
                         PrintUtil.showNetworkAvailableToast(this@DepositGoldActivity)
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     PrintUtil.showNetworkAvailableToast(this@DepositGoldActivity)
                 } finally {
                     //  progressDialog.hide();
                 }
             }
         })
*/

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("gold_weight", gold_weight)
            .addFormDataPart("tennure", tennure)
            .addFormDataPart("guarantee", guarantee)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/calculate_gold_mature_weight.php")
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
                    runOnUiThread {
                        var json = JSONObject(resp)
                        var status = json.get("status").toString()
                        var message = json.get("Message").toString()
                        var Data = json.optString("Data").toString()
                        if (status == "200") {
                            Log.e(" Response", resp)

                            var maturityWeight: String = Data
                            txtMaturityWeight!!.text = maturityWeight

                        } else {
                            AlertDialogs().alertDialogOk(
                                this@DepositGoldActivity,
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

    private fun AttemptToGetDepositeCharges(gold_weight: String) {
        //  progressDialog.show();
        /* maturityWeightServiceProvider?.getDepositeCharges(gold_weight, object : APICallback {
             override fun <T> onSuccess(serviceResponse: T) {
                 try {
                     val status: String? = (serviceResponse as DepositeChargesModel).getStatus()
                     val message: String? = (serviceResponse as DepositeChargesModel).getMessage()
                     val DepositeCharge: String? =
                         (serviceResponse as DepositeChargesModel).getData()
                     txtDepositeCharge!!.text = DepositeCharge
                     if (status == "200") {
                     } else {
                         AlertDialogs().alertDialogOk(
                             this@DepositGoldActivity, "Alert", message,
                             resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                         )
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
                             this@DepositGoldActivity,
                             (apiErrorModel as BaseServiceResponseModel).message
                         )
                     } else {
                         PrintUtil.showNetworkAvailableToast(this@DepositGoldActivity)
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                     PrintUtil.showNetworkAvailableToast(this@DepositGoldActivity)
                 } finally {
                     //  progressDialog.hide();
                 }
             }
         })*/

        //  change in api call
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("gw", gold_weight)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/gold_deposite_charges.php")
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
                    runOnUiThread {
                        if (status == "200") {
                            Log.e(" Response", resp)
                            var charges = json.get("charges").toString()
                            txtDepositeCharge!!.text = charges
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@DepositGoldActivity,
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

    private fun AttemptToGetDepositeRequest(
        user_id: String?, gw: String, tennure: String,
        cmw: String, dCharges: String, vendor_id: String,
        add_purity: String, remark: String,
        guarantee: String
    ) {
        /* progressDialog?.show()
         depositeRequestServiceProvider?.getDepositeRequest(user_id!!,
             gw,
             tennure,
             cmw,
             dCharges,
             vendor_id,
             add_purity,
             remark,
             guarantee,
             object : APICallback {
                 override fun <T> onSuccess(serviceResponse: T) {
                     try {
                         val status: String? = (serviceResponse as DepositeRequestModel).getStatus()
                         val message: String? =
                             (serviceResponse as DepositeRequestModel).getMessage()
                         msg = message
                         if (status == "200") {
                             AlertDialogs().alertDialogOk(
                                 this@DepositGoldActivity,
                                 "Alert",
                                 message,
                                 resources.getString(R.string.btn_ok),
                                 1,
                                 false,
                                 alertDialogOkListener
                             )

                             *//* mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
                            Intent intent=new Intent(GoldDepositeActivity.this,MainActivity.class);
                            startActivity(intent);*//*
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@DepositGoldActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                2,
                                false,
                                alertDialogOkListener
                            )

                            //                        mAlert.onShowToastNotification(GoldDepositeActivity.this, message);
                            *//*Intent intent=new Intent(GoldDepositeActivity.this,SuccessActivity.class);
                            intent.putExtra("message",message);
                            startActivity(intent);*//*
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
                                this@DepositGoldActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@DepositGoldActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@DepositGoldActivity)
                    } finally {
                        progressDialog?.hide()
                    }
                }
            })
        */

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", user_id)
            .addFormDataPart("gw", gw)
            .addFormDataPart("tennure", tennure)
            .addFormDataPart("cmw", cmw)
            .addFormDataPart("deposite_charges", dCharges)
            .addFormDataPart("vendor_id", vendor_id)
            .addFormDataPart("addpurity", add_purity)
            .addFormDataPart("remark", remark)
            .addFormDataPart("guarantee", guarantee)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/gold_deposite.php")
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
                    runOnUiThread {
                        if (status == "200") {
                            Log.e(" Response", resp)
                            AlertDialogs().alertDialogOk(
                                this@DepositGoldActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@DepositGoldActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                2,
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
                val intent = Intent(this@DepositGoldActivity, MainActivity::class.java)
                startActivity(intent)
            }
            2 -> {
                val failIntent =
                    Intent(this@DepositGoldActivity, SuccessActivity::class.java)
                failIntent.putExtra("message", msg)
                startActivity(failIntent)
            }
            11 -> {
                val LogIntent = Intent(this@DepositGoldActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}
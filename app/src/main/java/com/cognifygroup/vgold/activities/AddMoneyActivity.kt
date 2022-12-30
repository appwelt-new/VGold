package com.cognifygroup.vgold.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.cognifygroup.vgold.BuildConfig
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.addmoney.AddMoneyModel
import com.cognifygroup.vgold.activities.addmoney.AddMoneyServiceProvider
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddMoneyActivity : AppCompatActivity(), AlertDialogOkListener {

    var edtAddMoney: EditText? = null
    var btnAddMoney: Button? = null
    var spinner_payment_option1: Spinner? = null
    var llCheque: LinearLayout? = null
    var llRTGS: LinearLayout? = null

    var payment_option: String? = null
    var edtBankDetail1: EditText? = null
    var edtChequeNo1: EditText? = null
    var edtRtgsBankDetail1: EditText? = null
    var edtTxnId1: EditText? = null
    var objbnkdetiv: ImageView? = null
    var btnShareBankDetails: Button? = null
    var shreiv: ImageView? = null

    //var shreiv: ImageView? = null
    //var bnkdethideiv: ImageView? = null

    var mAlert: AlertDialogs? = null
    var addMoneyServiceProvider: AddMoneyServiceProvider? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_money)

        supportActionBar?.title = "Add Money"
        edtAddMoney = findViewById(R.id.edtAddMoney)
        btnAddMoney = findViewById(R.id.btnAddMoney)
        spinner_payment_option1 = findViewById(R.id.spinner_payment_option1)
        llCheque = findViewById(R.id.llCheque)
        llRTGS = findViewById(R.id.llRTGS)
        edtBankDetail1 = findViewById(R.id.edtBankDetail1)
        edtChequeNo1 = findViewById(R.id.edtChequeNo1)
        edtRtgsBankDetail1 = findViewById(R.id.edtRtgsBankDetail1)
        edtRtgsBankDetail1 = findViewById(R.id.edtRtgsBankDetail1)
        edtTxnId1 = findViewById(R.id.edtTxnId1)
        objbnkdetiv = findViewById(R.id.bnkdetiv)
        shreiv = findViewById(R.id.shreiv)
        //bnkdethideiv = findViewById(R.id.bnkdethideiv)
        btnShareBankDetails = findViewById(R.id.btnShareBankDetails)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        progressDialog = TransparentProgressDialog(this@AddMoneyActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        mAlert = AlertDialogs().getInstance()

        addMoneyServiceProvider = AddMoneyServiceProvider(this)
        loginStatusServiceProvider = LoginStatusServiceProvider(this)

        Glide.with(this)
            .load("https://www.vgold.co.in/dashboard/vgold_rate/bank%20details.png")
            .into(objbnkdetiv!!);

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.payment_option1, android.R.layout.simple_spinner_item
        )
// Specify the layout to use when the list of choices appears
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
// Apply the adapter to the spinner
        // Apply the adapter to the spinner
        spinner_payment_option1!!.setAdapter(adapter)
        spinner_payment_option1!!.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val paymentoption = parent.getItemAtPosition(pos) as String
                if (paymentoption == "cheque") {
                    payment_option = "Cheque"
                    llCheque!!.setVisibility(View.VISIBLE)
                    llRTGS!!.setVisibility(View.GONE)
                } else if (paymentoption == "NEFT/RTGS/Online Transfer") {
                    payment_option = "Online"
                    llRTGS!!.setVisibility(View.VISIBLE)
                    llCheque!!.setVisibility(View.GONE)
                } else if (paymentoption == "Credit/Debit/Net Banking(Payment Gateway)") {
                    payment_option = "Payu"
                    llCheque!!.setVisibility(View.GONE)
                    llRTGS!!.setVisibility(View.GONE)
                } else {
                    payment_option = ""
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        })

        //    checkLoginSession()

        btnAddMoney!!.setOnClickListener {
            onClickOfBtnAddMoney()
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


    private fun checkLoginSession() {
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
                                this@AddMoneyActivity,
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
                            this@AddMoneyActivity, "Alert", message,
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
                            this@AddMoneyActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@AddMoneyActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@AddMoneyActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    fun onClickOfBtnAddMoney() {
        if (payment_option == "Cheque") {
            AttemptToAddMoney(
                VGoldApp.onGetUerId(),
                "" + edtAddMoney!!.text.toString(),
                payment_option!!,
                edtBankDetail1!!.text.toString(),
                "",
                edtChequeNo1!!.text.toString()
            )
        } else if (payment_option == "Online") {
            AttemptToAddMoney(
                VGoldApp.onGetUerId(),
                "" + edtAddMoney!!.text.toString(),
                payment_option!!,
                edtRtgsBankDetail1!!.text.toString(),
                edtTxnId1!!.text.toString(),
                ""
            )
        } else if (payment_option == "Payu") {
            val intent = Intent(this@AddMoneyActivity, PayUMoneyActivity::class.java)
            intent.putExtra("AMOUNT", edtAddMoney!!.text.toString())
                .putExtra("whichActivity", "money")
            startActivity(intent)
        } else {
            AlertDialogs().alertDialogOk(
                this@AddMoneyActivity, "Alert", "Please select payment option",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
            //            mAlert.onShowToastNotification(AddMoneyActivity.this, "Please select payment option");
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
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
            user_id,
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

                            //  mAlert.onShowToastNotification(AddMoneyActivity.this, message);
                            val intent = Intent(this@AddMoneyActivity, SuccessActivity::class.java)
                            intent.putExtra("message", message)
                            startActivity(intent)
                            finish()
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@AddMoneyActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                            //                        mAlert.onShowToastNotification(AddMoneyActivity.this, message);
//                        Intent intent=new Intent(AddMoneyActivity.this,MainActivity.class);
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
//                            PrintUtil.showToast(
//                                this@AddMoneyActivity,
//                                (apiErrorModel as BaseServiceResponseModel).message
//                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@AddMoneyActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@AddMoneyActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@AddMoneyActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}
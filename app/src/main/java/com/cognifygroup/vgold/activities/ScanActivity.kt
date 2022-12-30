package com.cognifygroup.vgold.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.android.gms.vision.barcode.Barcode
import info.androidhive.barcode.BarcodeReader

class ScanActivity : AppCompatActivity(),BarcodeReader.BarcodeReaderListener,AlertDialogOkListener {
    var barcodeReader: BarcodeReader? = null

    var txtMobile: TextView? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    private var progressDialog: TransparentProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        supportActionBar?.title = "Scan"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        txtMobile = findViewById(R.id.txtMobile)


        progressDialog = TransparentProgressDialog(this@ScanActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        // get the barcode reader instance

        // get the barcode reader instance
        barcodeReader =
            supportFragmentManager.findFragmentById(R.id.barcode_scanner) as BarcodeReader?

        loginStatusServiceProvider = LoginStatusServiceProvider(this)
        checkLoginSession()

    txtMobile!!.setOnClickListener{
        onClickOfTxtMobile()
    }
    }

    private fun checkLoginSession() {
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
                                this@ScanActivity,
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
                            this@ScanActivity, "Alert", message,
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
                            this@ScanActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@ScanActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@ScanActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    fun onClickOfTxtMobile() {
        barcodeReader!!.pauseScanning();
        val intent = Intent(this@ScanActivity, ContactListActivityForMoney::class.java)
        startActivity(intent)

        /*Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(intent, 1);*/

        //startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }


/*    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String mobile = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        Toast.makeText(ScanActivity.this,name+""+mobile,Toast.LENGTH_LONG).show();
                        // TODO Whatever you want to do with the selected contact name.
                    }
                }
                break;
        }
    }*/

    /*    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String mobile = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        Toast.makeText(ScanActivity.this,name+""+mobile,Toast.LENGTH_LONG).show();
                        // TODO Whatever you want to do with the selected contact name.
                    }
                }
                break;
        }
    }*/
    override fun onScanned(barcode: Barcode) {

        // playing barcode reader beep sound
        barcodeReader!!.playBeep()

        barcodeReader!!.pauseScanning();

        // ticket details activity by passing barcode
        val intent = Intent(this@ScanActivity, PayActivity::class.java)
        intent.putExtra("code", barcode.displayValue)
        intent.putExtra("whichactivity", "2")
        startActivity(intent)
    }

    override fun onScannedMultiple(list: List<Barcode?>?) {}

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode?>?) {}

    override fun onScanError(s: String) {
        AlertDialogs().alertDialogOk(
            this@ScanActivity, "Alert", "Error occurred while scanning $s",
            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
        )
//        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_LONG).show();
    }

    override fun onCameraPermissionDenied() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            barcodeReader!!.pauseScanning();
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {

                val LogIntent = Intent(this@ScanActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.SparseArray
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.android.gms.vision.barcode.Barcode
import info.androidhive.barcode.BarcodeReader
import info.androidhive.barcode.BarcodeReader.BarcodeReaderListener


class ScanActivityForMoney : AppCompatActivity(), BarcodeReaderListener {
    var barcodeReader: BarcodeReader? = null
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var progressDialog: TransparentProgressDialog
    private lateinit var txtMobile: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_for_money)

        // get the barcode reader instance
        barcodeReader = supportFragmentManager.findFragmentById(R.id.barcode_scanner_money) as BarcodeReader?

        progressDialog = TransparentProgressDialog(this@ScanActivityForMoney)
        progressDialog.setCancelable(false)
        setFinishOnTouchOutside(false)

//        loginStatusServiceProvider = LoginStatusServiceProvider(this)
//        checkLoginSession()
        sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        supportActionBar?.title = "Scan For Money "
        txtMobile = findViewById(R.id.txtMobile)

        txtMobile.setOnClickListener {
            val intent = Intent(this@ScanActivityForMoney, ContactListActivityForMoney::class.java)
            startActivity(intent)
        }
    }

    override fun onScanned(barcode: Barcode?) {

        // playing barcode reader beep sound
        barcodeReader!!.playBeep()
        barcodeReader!!.pauseScanning()
        // ticket details activity by passing barcode

        // ticket details activity by passing barcode
        val intent = Intent(this@ScanActivityForMoney, PayActivityForMoney::class.java)
        intent.putExtra("code", barcode!!.displayValue)
        intent.putExtra("whichactivity", "2")
        startActivity(intent)
    }

    override fun onScannedMultiple(barcodes: MutableList<Barcode>?) {

    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) {

    }

    override fun onScanError(errorMessage: String?) {
        Toast.makeText(applicationContext, "Error occurred while scanning $errorMessage", Toast.LENGTH_LONG)
            .show()
    }

    override fun onCameraPermissionDenied() {
        finish()
    }


}
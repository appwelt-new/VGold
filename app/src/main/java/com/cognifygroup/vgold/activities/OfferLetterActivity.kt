package com.cognifygroup.vgold.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import butterknife.OnClick
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.deleteVenderAdv.VendorAdvDeleteServiceProvider
import com.cognifygroup.vgold.deleteVenderAdv.VendorAdvModel
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.messaging.Constants.MessageNotificationKeys.IMAGE_URL
import com.squareup.picasso.Picasso


import java.io.ByteArrayOutputStream
import java.io.IOException

class OfferLetterActivity : AppCompatActivity(), AlertDialogOkListener {

    var imgLetter: ImageView? = null
    var imgDelete: TextView? = null
    var txtUpdateAdv: TextView? = null

    var bottom_layout: LinearLayout? = null
    var fab: FloatingActionButton? = null
    var addAdv: TextView? = null
    var offerImg: String? =
        null
    var offerImg1: kotlin.String? = null
    var BaseUrl: kotlin.String? = null
    var flag = false
    var mAlert: AlertDialogs? = null
    private var vendorDeleteAdvServiceProvider: VendorAdvDeleteServiceProvider? = null
    private var venderId: String? = null
    private var logo_path: String? = null
    val PICK_IMAGE = 1
    private var flagVal: String? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    // zoom function
    var lastEvent: FloatArray? = null
    var d = 0f
    var newRot = 0f
    private var isZoomAndRotate = false
    private var isOutSide = false
    private val NONE = 0
    private val DRAG = 1
    private val ZOOM = 2
    private var mode = NONE
    private val start = PointF()
    private val mid = PointF()
    var oldDist = 1f
    private var xCoOrdinate = 0f
    private var yCoOrdinate: kotlin.Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_letter)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Our Vendors"

        sharedPreferences =
            this@OfferLetterActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()


        imgLetter = findViewById(R.id.imgLetter)
        addAdv = findViewById(R.id.addAdv)
        fab = findViewById(R.id.fab)
        imgDelete = findViewById(R.id.imgDelete)
        bottom_layout = findViewById(R.id.bottom_layout)
        txtUpdateAdv = findViewById(R.id.txtUpdateAdv)


        init()
        imgDelete!!.setOnClickListener {
            onClickDelete()
        }
        addAdv!!.setOnClickListener {
            onClickAddAdv()
        }
        txtUpdateAdv!!.setOnClickListener {
            onClickUpdateAdv()
        }
    }

    fun init() {
        progressDialog = TransparentProgressDialog(this@OfferLetterActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        vendorDeleteAdvServiceProvider = VendorAdvDeleteServiceProvider(this)
        if (intent.hasExtra("offer")) {
            offerImg = intent.getStringExtra("offer")
            offerImg1 = intent.getStringExtra("offer1")
            venderId = intent.getStringExtra("venderId")
            logo_path = intent.getStringExtra("logo_path")
        }

        /*if (offerImg1 == null || TextUtils.isEmpty(offerImg1)) {
            addAdv.setVisibility(View.VISIBLE);
            imgLetter.setVisibility(View.GONE);
        }else{
            addAdv.setVisibility(View.GONE);
            imgLetter.setVisibility(View.VISIBLE);
        }*/BaseUrl = "https://www.vgold.co.in" + logo_path
        Picasso.with(this@OfferLetterActivity)
            .load(BaseUrl).placeholder(R.mipmap.ic_launcher)
            .into(imgLetter)


        imgLetter?.setOnTouchListener(View.OnTouchListener { v, event ->
            val view = v as ImageView
            view.bringToFront()
            viewTransformation(view, event)
            true
        })

        /*  Picasso.with(this@OfferLetterActivity).load(BaseUrl)
              .placeholder(R.mipmap.ic_launcher)
              .error(R.mipmap.ic_launcher)
              .into(imgLetter)
  */

        fab!!.setOnClickListener {
            if (!flag) {

//                    bottom_layout.setVisibility(View.VISIBLE);
                if (VGoldApp.onGetUserRole().equals("admin") || VGoldApp.onGetUserRole()
                        .equals("manager")
                ) {
                    if (offerImg1 == null || TextUtils.isEmpty(offerImg1)) {
                        addAdv!!.visibility = View.VISIBLE
                        imgLetter!!.visibility = View.GONE
                        bottom_layout!!.visibility = View.GONE
                    } else {
                        bottom_layout!!.visibility = View.VISIBLE
                    }
                } else {
                    imgLetter!!.visibility = View.VISIBLE
                    addAdv!!.visibility = View.GONE
                    bottom_layout!!.visibility = View.GONE
                }
                BaseUrl = IMAGE_URL + offerImg1
                Picasso.with(this@OfferLetterActivity)
                    .load(BaseUrl).placeholder(R.mipmap.ic_launcher)
                    .into(imgLetter)
                flag = true
            } else {
                bottom_layout!!.visibility = View.GONE
                imgLetter!!.visibility = View.VISIBLE
                addAdv!!.visibility = View.GONE
                BaseUrl = IMAGE_URL + offerImg
                Picasso.with(this@OfferLetterActivity)
                    .load(BaseUrl)
                    .placeholder(R.mipmap.ic_launcher) //                            .fit()
                    .into(imgLetter)
                flag = false
            }
        }
       // loginStatusServiceProvider = LoginStatusServiceProvider(this)
       // checkLoginSession()
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
                                this@OfferLetterActivity,
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
                            this@OfferLetterActivity, "Alert", message,
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
                            this@OfferLetterActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@OfferLetterActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@OfferLetterActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    @OnClick(R.id.imgDelete)
    fun onClickDelete() {
        DeleteAlert()
    }

    @OnClick(R.id.addAdv)
    fun onClickAddAdv() {
        flagVal = "add"
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE
        )
    }

    fun onClickUpdateAdv() {
        flagVal = "update"
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE
        )
    }

    private fun DeleteAlert() {
        val alertDialog = AlertDialog.Builder(this)
        // Setting Dialog Title
        alertDialog.setTitle("")
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to delete?")

        // On pressing Settings button
        alertDialog.setPositiveButton(
            "Yes"
        ) { dialog, which -> DeleteVenderAdv() }

        // on pressing cancel button
        alertDialog.setNegativeButton(
            "No"
        ) { dialog, which -> dialog.cancel() }
        // Showing Alert Message
        alertDialog.show()
    }

    private fun DeleteVenderAdv() {
        progressDialog!!.show()
        vendorDeleteAdvServiceProvider?.getDeleteVenderAdv(venderId, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    val status: String? = (serviceResponse as VendorAdvModel).status
                    val message: String? = (serviceResponse as VendorAdvModel).message
                    if (status == "200") {
                        AlertDialogs().alertDialogOk(
                            this@OfferLetterActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 1, false, alertDialogOkListener
                        )
                        //                        mAlert.onShowToastNotification(OfferLetterActivity.this, message);


                        /* Intent intent = new Intent(OfferLetterActivity.this, OurVendersActivity.class);
                            startActivity(intent);
                            finish();*/
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@OfferLetterActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                        mAlert.onShowToastNotification(OfferLetterActivity.this, message);
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
                            this@OfferLetterActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@OfferLetterActivity)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@OfferLetterActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    private fun AddVenderAdv(advPath: String) {
        progressDialog!!.show()
        vendorDeleteAdvServiceProvider?.addVenderAdv(
            "add",
            venderId,
            advPath,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? = (serviceResponse as VendorAdvModel).status
                        val message: String? = (serviceResponse as VendorAdvModel).message
                        //                    ArrayList<VendorAdvModel.Data> mArrSaloonServices = ((VendorAdvModel) serviceResponse).getData();
                        if (status == "200") {
                            AlertDialogs().alertDialogOk(
                                this@OfferLetterActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

                            /*  mAlert.onShowToastNotification(OfferLetterActivity.this, message);
                            Intent intent = new Intent(OfferLetterActivity.this, OurVendersActivity.class);
                            startActivity(intent);
                            finish();*/
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@OfferLetterActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                            //                        mAlert.onShowToastNotification(OfferLetterActivity.this, message);
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
                                this@OfferLetterActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@OfferLetterActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@OfferLetterActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
    }

    private fun UpdateVenderAdv(advPath: String) {
        progressDialog!!.show()
        vendorDeleteAdvServiceProvider?.addVenderAdv(
            "add",
            venderId,
            advPath,
            object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val status: String? = (serviceResponse as VendorAdvModel).status
                        val message: String? = (serviceResponse as VendorAdvModel).message
                        //                    ArrayList<VendorAdvModel.Data> mArrSaloonServices = ((VendorAdvModel) serviceResponse).getData();
                        if (status == "200") {
                            AlertDialogs().alertDialogOk(
                                this@OfferLetterActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

                            /*  mAlert.onShowToastNotification(OfferLetterActivity.this, message);

                            Intent intent = new Intent(OfferLetterActivity.this, OurVendersActivity.class);
                            startActivity(intent);
                            finish();*/
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@OfferLetterActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )

                            //                        mAlert.onShowToastNotification(OfferLetterActivity.this, message);
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
                                this@OfferLetterActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@OfferLetterActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@OfferLetterActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            useImage(uri)
        }
    }

    fun useImage(uri: Uri?) {
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            if (bitmap != null) {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imageBytes = baos.toByteArray()
                val advPath = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
                if (advPath != null && !TextUtils.isEmpty(advPath)) {
                    if (flagVal != null && !TextUtils.isEmpty(flagVal)) {
                        if (flagVal.equals(
                                "add",
                                ignoreCase = true
                            )
                        ) AddVenderAdv(advPath) else if (flagVal.equals(
                                "update",
                                ignoreCase = true
                            )
                        ) UpdateVenderAdv(advPath)
                    }
                } else {
                    AlertDialogs().alertDialogOk(
                        this@OfferLetterActivity, "Alert", "Please select Image",
                        resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                    )
                    //                    mAlert.onShowToastNotification(OfferLetterActivity.this, "Please select Image");
                }
            }
            //            Log.d("TAG", String.valueOf(bitmap));
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@OfferLetterActivity, OurVendersActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            1 -> {
                val intent = Intent(this@OfferLetterActivity, OurVendersActivity::class.java)
                startActivity(intent)
                finish()
            }
            11 -> {
                val LogIntent = Intent(this@OfferLetterActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }


    private fun viewTransformation(view: View, event: MotionEvent) {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                xCoOrdinate = view.getX() - event.rawX
                yCoOrdinate = view.getY() - event.rawY
                start[event.x] = event.y
                isOutSide = false
                mode = DRAG
                lastEvent = null
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    midPoint(mid, event)
                    mode = ZOOM
                }
                lastEvent = FloatArray(4)
                lastEvent!![0] = event.getX(0)
                lastEvent!![1] = event.getX(1)
                lastEvent!![2] = event.getY(0)
                lastEvent!![3] = event.getY(1)
                d = rotation(event)
            }
            MotionEvent.ACTION_UP -> {
                isZoomAndRotate = false
                if (mode === DRAG) {
                    val x = event.x
                    val y = event.y
                }
                isOutSide = true
                mode = NONE
                lastEvent = null
                mode = NONE
                lastEvent = null
            }
            MotionEvent.ACTION_OUTSIDE -> {
                isOutSide = true
                mode = NONE
                lastEvent = null
                mode = NONE
                lastEvent = null
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
                lastEvent = null
            }
            MotionEvent.ACTION_MOVE -> if (!isOutSide) {
                if (mode === DRAG) {
                    isZoomAndRotate = false
                    view.animate().x(event.rawX + xCoOrdinate).y(event.rawY + yCoOrdinate)
                        .setDuration(0).start()
                }
                if (mode === ZOOM && event.pointerCount == 2) {
                    val newDist1 = spacing(event)
                    if (newDist1 > 10f) {
                        val scale: Float = newDist1 / oldDist * view.getScaleX()
                        view.setScaleX(scale)
                        view.setScaleY(scale)
                    }
                    if (lastEvent != null) {
                        newRot = rotation(event)
                        view.setRotation((view.getRotation() + (newRot - d)) as Float)
                    }
                }
            }
        }
    }

    private fun rotation(event: MotionEvent): Float {
        /* val delta_x = (event.getX(0) - event.getX(1)).toDouble()
         val delta_y = (event.getY(0) - event.getY(1)).toDouble()
         val radians = Math.atan2(delta_y, delta_x)
         return Math.toDegrees(radians).toFloat()*/
        return 1.0f
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toInt().toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}

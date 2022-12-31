package com.cognifygroup.vgold.activities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import com.android.volley.RequestQueue
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.Constants.Companion.VUSER_ID
import com.cognifygroup.vgold.utilities.Constants.Companion.prfaddr
import com.cognifygroup.vgold.utilities.Constants.Companion.prfcity
import com.cognifygroup.vgold.utilities.Constants.Companion.prfemail
import com.cognifygroup.vgold.utilities.Constants.Companion.prfmobno
import com.cognifygroup.vgold.utilities.Constants.Companion.prfstate
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import com.github.dhaval2404.imagepicker.ImagePicker
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class UpdateProfileActivity : AppCompatActivity() {
    private var userId = ""
    private var prfemailsh = ""
    private var prfmobnosh = ""
    private var prfaddrsh = ""
    private var prfcitysh = ""
    private var prfstatesh = ""
    private var encodedImage = ""
    private var encFile = ""
    private var coDFile: File? = null
    private var path = ""
    private var imageBytes: ByteArray? = null
    private var aboutCompQue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var camBtn: ImageView
    private lateinit var profPic: CircleImageView
    private lateinit var updatePrfBtn: Button
    private lateinit var edtemail: EditText
    private lateinit var edtmobno: EditText
    private lateinit var edtaddr: EditText
    private lateinit var edtcity: EditText
    private lateinit var edtstate: EditText
    private lateinit var edtpanno: EditText
    private lateinit var edtadharno: EditText
    private lateinit var pancard: ImageView
    private lateinit var adharfront: ImageView
    private lateinit var adharback: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(VUSER_ID, null).toString()
        profPic = findViewById(R.id.userImg)
        camBtn = findViewById(R.id.imgUserProfile)
        edtemail = findViewById(R.id.edtEmail)
        edtmobno = findViewById(R.id.edtMobileNumber)
        edtaddr = findViewById(R.id.edtAddress)
        edtcity = findViewById(R.id.edtCity)
        edtstate = findViewById(R.id.edtState)
        edtadharno = findViewById(R.id.edtAadhar)
        edtpanno = findViewById(R.id.edtPan)
        pancard = findViewById(R.id.iv_pancard)
        adharfront = findViewById(R.id.iv_aadharFront)
        adharback = findViewById(R.id.iv_aadharBack)
        updatePrfBtn = findViewById(R.id.btn_submit_sign_up)

        prfemailsh = sharedPreferences.getString(prfemail, "").toString()
        prfmobnosh = sharedPreferences.getString(prfmobno, "").toString()
        prfaddrsh = sharedPreferences.getString(prfaddr, "").toString()
        prfcitysh = sharedPreferences.getString(prfcity, "").toString()
        prfstatesh = sharedPreferences.getString(prfstate, "").toString()

        edtemail.setText(prfemailsh)
        edtmobno.setText(prfmobnosh)
        edtaddr.setText(prfaddrsh)
        edtcity.setText(prfcitysh)
        edtstate.setText(prfstatesh)

        camBtn.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                //.start()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
            //changeProfPic(chImg + candIdd)
        }

        updatePrfBtn.setOnClickListener {
            if (edtemail.text.isEmpty()) {
                edtemail.error = "Please Enter the Email  !"
                edtemail.requestFocus()
            } else if (edtmobno.text.isEmpty()) {
                edtmobno.error = " Please Enter the Mobile Number  !"
                edtmobno.requestFocus()
            } else if (edtaddr.text.isEmpty()) {
                edtaddr.error = " Please Enter the Address  !"
                edtaddr.requestFocus()
            } else if (edtcity.text.isEmpty()) {
                edtcity.error = " Please Enter the City   !"
                edtcity.requestFocus()
            } else if (edtstate.text.isEmpty()) {
                edtstate.error = " Please Enter the State   !"
                edtstate.requestFocus()
            } else if (edtadharno.text.isEmpty()) {
                edtadharno.error = " Please Enter the  Aadhar Number !"
                edtadharno.requestFocus()
            } else if (edtpanno.text.isEmpty()) {
                edtpanno.error = " Please Enter the Pancard Number  !"
                edtpanno.requestFocus()
            } else
                updateProfileFunction()
        }
    }

    private fun updateProfileFunction() {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart(
                "email",
                edtemail.text.toString()
            ) // "email", emailEdText.text.toString()
            .addFormDataPart("no", edtmobno.text.toString())
            .addFormDataPart("address", edtaddr.text.toString())
            .addFormDataPart("city", edtcity.text.toString())
            .addFormDataPart("state", edtstate.text.toString())
            .build()
        val request = okhttp3.Request.Builder()
            .url("http://vgold.co.in/dashboard/webservices/update_profile.php")
//            .header("AUTHORIZATION", "Bearer $token")
            .header("Accept", "application/json")
//            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                e.printStackTrace()
                Log.e("failure Response", mMessage)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val mMessage = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
//                    Log.d("Upload Status", "Image Uploaded Successfully !")
                    runOnUiThread(Runnable {

                        val editor = sharedPreferences.edit()
                        editor.putString(Constants.prfemail, edtemail.text.toString())
                        editor.putString(Constants.prfmobno, edtmobno.text.toString())
                        editor.putString(Constants.prfaddr, edtaddr.text.toString())
                        editor.putString(Constants.prfcity, edtcity.text.toString())
                        editor.putString(Constants.prfstate, edtstate.text.toString())
                        editor.apply()
                        Toast.makeText(this@UpdateProfileActivity, mMessage, Toast.LENGTH_SHORT)
                            .show()
//                        startActivity(Intent(this@UpdateProfileActivity, MainActivity::class.java))
                        finish()
                    })
                }
            }
        })
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                coDFile = fileUri.toFile()
                val coFpath = coDFile!!.absolutePath
                encFile = fileUri.toFile().name
                profPic.setImageURI(fileUri)
                val inputStream = this.contentResolver.openInputStream(fileUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream!!.close()
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                imageBytes = outputStream.toByteArray()
                encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                outputStream.close()

                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString())
                val mime =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase())
                val client = OkHttpClient().newBuilder().build()
                val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", userId)
                    .addFormDataPart(
                        "image",
                        encFile,
                        RequestBody.create(
                            MediaType.parse(mime!!),
                            coDFile!!
                        )
                    )
                    .build()
                val request = okhttp3.Request.Builder()
                    .url("http://vgold.co.in/dashboard/webservices/update_profile_photo.php")
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
                        val mMessage = response.body()!!.string()
                        if (!response.isSuccessful) {
                            throw IOException("Unexpected code" + response)
                        } else {
                            Log.d("Upload Status", "Image Uploaded Successfully !")
                            runOnUiThread(Runnable {
                                Toast.makeText(
                                    this@UpdateProfileActivity,
                                    "Selfi Uploaded" + mMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            })
                        }
                    }
                })
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
}
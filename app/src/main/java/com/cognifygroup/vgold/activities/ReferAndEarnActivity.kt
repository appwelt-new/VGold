package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.utilities.Constants
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.IOException

class ReferAndEarnActivity : AppCompatActivity() {
    var mAlert: AlertDialogs? = null
    private lateinit var edtNameR: EditText
    private lateinit var edtEmailR: EditText
    private lateinit var edtMobileR: EditText
    private lateinit var btnSubmitR: Button
    private lateinit var imgContact: ImageView
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_and_earn)
        sharedPreferences =
            this@ReferAndEarnActivity.getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()
        supportActionBar?.title = "Refer And Earn"
         init()


        edtEmailR = findViewById(R.id.edtEmailR)
        btnSubmitR = findViewById(R.id.btnSubmitR)
        edtNameR = findViewById(R.id.edtNameR)
        edtMobileR = findViewById(R.id.edtMobileR)


        btnSubmitR.setOnClickListener {
            if (edtNameR.text.isEmpty()) {
                edtNameR.error = "Please Enter the   Refer Name !"
                edtNameR.requestFocus()

            } else if (edtEmailR.text.isEmpty()) {
                edtEmailR.error = "Please Enter the Email !"
                edtEmailR.requestFocus()
            } else if (edtMobileR.text.isEmpty()) {
                edtMobileR.error = "Please Enter the Mobile Number !"
                edtMobileR.requestFocus()
            } else {
                refer()
            }
        }

    }

    private fun refer() {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("name", edtNameR.text.toString())
            .addFormDataPart("email", edtEmailR.text.toString())
            .addFormDataPart("mobile_no", edtMobileR.text.toString())


            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/send_refer_link.php?")
      //      .header("AUTHORIZATION", "Bearer $token")
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
                    //runOnUiThread(Runnable {
                    runOnUiThread(Runnable {
                        Toast.makeText(
                            this@ReferAndEarnActivity,
                            "Refer Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@ReferAndEarnActivity, MainActivity::class.java)
//                    intent.putExtra("email_id", edtEmail.text.toString())
                        startActivity(intent)
                        // Toast.makeText(this@LoginActivity, ".$mMessage", Toast.LENGTH_SHORT).show()

                    })
                }
            }
        })
    }
}
    private fun init() {
//        mAlert = AlertDialogs.getInstance()
//        progressDialog = TransparentProgressDialog(this@ReferActivity)
//        progressDialog.setCancelable(false)
//        setFinishOnTouchOutside(false)
//        referServiceProvider = ReferServiceProvider(this)
//        if (intent.hasExtra("no")) {
//           val no = intent.getStringExtra("no")
//        }
//        edtMobileR.setText(no)
//        loginStatusServiceProvider = LoginStatusServiceProvider(this)
//        checkLoginSession()
    }

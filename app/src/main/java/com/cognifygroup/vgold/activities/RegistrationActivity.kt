package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.utilities.Constants
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.IOException

class RegistrationActivity : AppCompatActivity() {
    private lateinit var firstName: EditText
    private lateinit var lastName : EditText
    private lateinit var email : EditText
    private lateinit var mobNo: EditText
    private lateinit var aadharNo: EditText
    private lateinit var panNo: EditText
    private lateinit var referCode: EditText
    private lateinit var register: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.title = "New Registration"

        sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)

        firstName = findViewById(R.id.edtfirst)
        lastName = findViewById(R.id.edtlast1)
        email = findViewById(R.id.edtmail)
        mobNo = findViewById(R.id.edtno)
        panNo = findViewById(R.id.edtPancard)
        aadharNo = findViewById(R.id.edtAadarCard)
        referCode = findViewById(R.id.edtReferCode)
        register = findViewById(R.id.btnRegister)

        register.setOnClickListener {
            if (firstName.text.isEmpty()) {
                firstName.error = "Please Enter the First Name !"
                firstName.requestFocus()
            }else if (lastName.text.isEmpty()){
                      lastName.error = " Please Enter the Last Name !"
                      lastName.requestFocus()
            }else if (email.text.isEmpty()){
                      email.error = " Please Enter the Email  !"
                      email.requestFocus()
            }else if (mobNo.text.isEmpty()){
                      mobNo.error = " Please Enter the Mobile Number  !"
                      mobNo.requestFocus()
            }else if (panNo.text.isEmpty()){
                      panNo.error = " Please Enter the Pan Number  !"
                      panNo.requestFocus()
            }else if (aadharNo.text.isEmpty()){
                      aadharNo.error = " Please Enter the  Aadhar Number !"
                      aadharNo.requestFocus()
            }else if (referCode.text.isEmpty()){
                      referCode.error = " Please Enter the  Refer Code !"
                      referCode.requestFocus()
        }else
                logintoVgold()

        }


    }

    private fun logintoVgold() {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("first", firstName.text.toString())
            .addFormDataPart("last", lastName.text.toString())
            .addFormDataPart("email", email.text.toString())
            .addFormDataPart("no", mobNo.text.toString())
            .addFormDataPart("pancard", panNo.text.toString())
            .addFormDataPart("pass", aadharNo.text.toString())
            .addFormDataPart("refer_code", referCode.text.toString())

            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/register.php?")
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
                        Toast.makeText(
                            this@RegistrationActivity,
                            "New Registration Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                        startActivity(intent)
                        // Toast.makeText(this@LoginActivity, ".$mMessage", Toast.LENGTH_SHORT).show()
                    })
                }
            }
        })
    }
}
package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.Constants.Companion.VGOLD_DB
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.IOException

//import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var txt_signup: TextView
    private lateinit var txt_frgtpswd: TextView
    private lateinit var txt_Login: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var LoginPrgBar: ProgressBar
    private var isRemembered = false
    private val mAlert: AlertDialogs? = null
    private var logDQue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txt_signup = findViewById(R.id.tv_SignUp)
        txt_frgtpswd = findViewById(R.id.tv_forgotpassword)
        txt_Login = findViewById(R.id.BtnLogin)

        sharedPreferences = getSharedPreferences(VGOLD_DB, Context.MODE_PRIVATE)

        isRemembered = sharedPreferences.getBoolean(Constants.REMEMBER, false)

        edtEmail = findViewById(R.id.edtEmail)
        LoginPrgBar = findViewById(R.id.lgPrBar)

        txt_frgtpswd.setOnClickListener {

            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))

        }
//        if (isRemembered) {
//            val intent = Intent(applicationContext, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finish()
//        }

        txt_signup.setOnClickListener {

            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }

        txt_Login.setOnClickListener {
            if (edtEmail.text.isEmpty()) {
                edtEmail.error = "Please Enter the Details !"
                edtEmail.requestFocus()
            } else
                logintoVgold()

        }
    }

    private fun logintoVgold() {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("email", edtEmail.text.toString())
            .build()
        val token = ""
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/login.php?")
            .header("AUTHORIZATION", "Bearer $token")
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
//                    Log.d("Upload Status", "Image Uploaded Successfully !")
                    runOnUiThread(Runnable {
                        val intent = Intent(this@LoginActivity, OtpVerificationActivity::class.java)
                        intent.putExtra("email_id", edtEmail.text.toString())
                        startActivity(intent)
                        // Toast.makeText(this@LoginActivity, ".$mMessage", Toast.LENGTH_SHORT).show()
                        finish()
                    })
                }
            }
        })
    }
}

//        private fun logintoVgold(){
//            var jsonobj = JSONObject()
//            jsonobj.put("email", edtEmail.text.trim())
//            logDQue = Volley.newRequestQueue(this@LoginActivity)
//            val req = JsonObjectRequest(Request.Method.POST, LOGIN_API, jsonobj, { response ->
//          //  val req = StringRequest(Request.Method.POST, LOGIN_API +"email="+edtEmail.text, { response ->
////                que.cancelAll(this)
//              // val obj  = JSONObject(response)
//                jsonobj = response
//                val prdds = jsonobj.getString("Message")
//                val checked = true
//                val editor = sharedPreferences.edit()
//
//                editor.putString(USERNAME, edtEmail.text.trim().toString())
//                editor.apply()
//                Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, OtpVerificationActivity::class.java)
//                intent.putExtra("email_id", edtEmail.text.toString())
//                startActivity(intent)
//                finish()
//
//            }, {
//
//                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
//                    .show()
//
//            })
//            req.setRetryPolicy(DefaultRetryPolicy(20 * 1000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
//            logDQue!!.add(req)
//        }
//
//    override fun onPause() {
//        if (logDQue != null)
//            logDQue!!.cancelAll { true }
//        super.onPause()
//    }
//}



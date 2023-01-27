package com.cognifygroup.vgold.activities

//import com.android.volley.DefaultRetryPolicy
//import com.android.volley.Request
//import com.android.volley.VolleyError
//import com.android.volley.toolbox.JsonObjectRequest
//import com.android.volley.toolbox.Volley
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.android.gms.tasks.Task
//import com.google.firebase.FirebaseException
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.model.GenericTextWatcher
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.Constants.Companion.VGOLD_DB
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*


class OtpVerificationActivity : AppCompatActivity() {


    private lateinit var sharedPreferences: SharedPreferences
    private var eMail = ""
    private var mobileOtp = ""
    private lateinit var dialog: Dialog
    private lateinit var emIdOtp1: EditText
    private lateinit var emIdOtp2: EditText
    private lateinit var emIdOtp3: EditText
    private lateinit var emIdOtp4: EditText
    private lateinit var mbDisp: TextView
    private var intmob = 0

    private var isRemembered = false

    private lateinit var verifyOtpBtn: Button
    private lateinit var resndOtpBtn: LinearLayout
    private var verificationId = ""
    private var cFirebaseOtp = ""

    // private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)

        sharedPreferences = getSharedPreferences(VGOLD_DB, Context.MODE_PRIVATE)
        isRemembered = sharedPreferences.getBoolean(Constants.REMEMBER, false)
        emIdOtp1 = findViewById(R.id.et_otp_var1)
        emIdOtp2 = findViewById(R.id.et_otp_var2)
        emIdOtp3 = findViewById(R.id.et_otp_var3)
        emIdOtp4 = findViewById(R.id.et_otp_var4)
        verifyOtpBtn = findViewById(R.id.BtnVerify)
        resndOtpBtn = findViewById(R.id.resend)
        mbDisp = findViewById(R.id.tv_No)

        eMail = intent.getStringExtra("email_id").toString()
        //   firebaseAuth = FirebaseAuth.getInstance()

        // sendVerificationCode("+91"+eMail)
//For Skipping Otp Screen
//       if (isRemembered) {
//            val intent = Intent(applicationContext, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//           startActivity(intent)
//            finish()
//        }


        dialog = Dialog(this)
        emIdOtp1.addTextChangedListener(GenericTextWatcher(emIdOtp1, emIdOtp2))
        emIdOtp2.addTextChangedListener(GenericTextWatcher(emIdOtp2, emIdOtp3))
        emIdOtp3.addTextChangedListener(GenericTextWatcher(emIdOtp3, emIdOtp4))

        mbDisp.text = "+91" + eMail


        emIdOtp1.requestFocus()
        verifyOtpBtn.setOnClickListener {
            if (emIdOtp1.text.isEmpty()) {
                emIdOtp1.error = "This Field is Blank !"
                emIdOtp1.requestFocus()
            } else if (emIdOtp2.text.isEmpty()) {
                emIdOtp2.error = "This Field is Blank !"
                emIdOtp2.requestFocus()
            } else if (emIdOtp3.text.isEmpty()) {
                emIdOtp3.error = "This Field is Blank !"
                emIdOtp3.requestFocus()
            } else if (emIdOtp4.text.isEmpty()) {
                emIdOtp4.error = "This Field is Blank !"
                emIdOtp4.requestFocus()
            } else {

                mobileOtp =
                    emIdOtp1.text.toString() + emIdOtp2.text.toString() + emIdOtp3.text.toString() + emIdOtp4.text.toString()

//                intmob = mobileOtp.toInt()
                okhttpReq()

            }
            //   eMail.setOnClickListener { onBackPressed() }
            mbDisp.setOnClickListener { onBackPressed() }
        }
    }
//
//        private fun otpMsg(){
//            dialog.setContentView(R.layout.successfully_registered_dialog)
//            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            val okBt = dialog.findViewById<Button>(R.id.okBt)
//            okBt.setOnClickListener {
//                val intent = Intent(this, MainActivity::class.java)
//                //val intent = Intent(this, SocialRegistrationForm::class.java)
//                intent.putExtra("companyname", firstName)
//                //intent.putExtra("midnm", midname)
//                intent.putExtra("lastname", lastName)
//                intent.putExtra("emlid", candiEmail)
//                intent.putExtra("mobno", candiMobile)
//                //intent.putExtra("mobileotp", mobileOtp)
//                //intent.putExtra("emailotp", emailOtp)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//                finish()
//
//
//            }
//            dialog.show()
//            var counter = 0
//            object : CountDownTimer(2000, 500){
//                override fun onTick(p0: Long) {
//                    counter++
//                }
//
//                override fun onFinish() {
//                    okBt.performClick()
//                }
//            }.start()
//        }

    private fun okhttpReq() {


        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("email", eMail)
            .addFormDataPart("otp", mobileOtp)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/login.php")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                //e.printStackTrace()
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this@OtpVerificationActivity,
                        mMessage,
                        Toast.LENGTH_LONG
                    ).show()
                })
                //Log.e("failure Response", mMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                try {

                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code" + response)
                    } else {

                        var mMessage = response.body()!!.string()
//                    this@OtpVerificationActivity.runOnUiThread(Runnable {
//                        Toast.makeText(
//                            this@OtpVerificationActivity,
//                            mMessage,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    })

                        //mMessage = '[' + mMessage + ']';
                        val json = JSONObject(mMessage)
                        Log.i("TAGjnjn", "onResponse: " + json)
                        if (json != null) {

                            val code = json.getInt("status")
                            val msg = json.getString("Message")
                            val data = json.getJSONArray("data")

//                        val gson = Gson()
//                        val loginModel = gson.fromJson(json.toString(), LoginModel::class.java)
//                        val loginModelArrayList: ArrayList<LoginModel.Data> =
//                            loginModel.data!!
//                        val rscode = loginModel.status

                            this@OtpVerificationActivity.runOnUiThread(Runnable {
                                Toast.makeText(
                                    this@OtpVerificationActivity,
                                    msg,
                                    Toast.LENGTH_LONG
                                ).show()
                            })
                            if (code == 200) {
                                for (i in 0 until data.length()) {
                                    val jsonObject = data.getJSONObject(i)
//                                val id = jsonObject.optString("ID").toInt()
//                                val name = jsonObject.optString("Name")
//                                val salary = jsonObject.optString("Salary").toFloat()


                                    VGoldApp.onSetUserDetails(
                                        jsonObject.getString("User_ID"),
                                        jsonObject.getString("First_Name"),
                                        jsonObject.getString("Last_Name"),
                                        jsonObject.getString("Email"),
                                        jsonObject.getString("Mobile_no"),
                                        "",
                                        jsonObject.getString("pan_no"),
                                        jsonObject.getString("Address"),
                                        jsonObject.getString("City"),
                                        jsonObject.getString("State"),
                                        "",
                                        jsonObject.getInt("is_cp"),
                                        "",
                                        "",
                                        "",
                                        jsonObject.getString("aadhar_no")
                                    )
//jsonObject.getString("qrcode")
//                        loginModelArrayList[0].profile_photo
//                        loginModelArrayList[0].identity_photo
//                        loginModelArrayList[0].address_photo
//                        loginModelArrayList[0].address_photo_back

                                    VGoldApp.onSetUserRole(
                                        jsonObject.getString("User_role"),
                                        jsonObject.getString("validity_date")
                                    )

                                    val checked = true
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean(Constants.REMEMBER, checked)
                                    editor.apply()

                                    runOnUiThread(Runnable {
                                        try {
                                            var status: Boolean = false
                                            val verson_code = jsonObject.getString("Version_code")
                                            //  val verson_code = "1.2.30"
                                            val pInfo: PackageInfo =
                                                VGoldApp.context!!.getPackageManager()
                                                    .getPackageInfo(
                                                        VGoldApp.context!!.getPackageName(),
                                                        0
                                                    )
                                            val version = pInfo.versionName
                                            //  Toast.makeText(this@LoginActivity, "" + version, Toast.LENGTH_SHORT).show()


                                            val verson_code_split: MutableList<String> =
                                                verson_code.split(".").toMutableList()
                                            val verson_split: MutableList<String> =
                                                version.split(".").toMutableList()


                                            if (
                                                verson_code_split[2].length == 1
                                            ) {
                                                verson_code_split[2] = "0" + verson_code_split[2];
                                            }


                                            if (
                                                verson_split[2].length == 1
                                            ) {
                                                verson_split[2] = "0" + verson_split[2];
                                            }


                                            if (verson_code_split[0].toInt() > verson_split[0].toInt()) {
                                                status = true
                                            } else if (verson_code_split[1].toInt() > verson_split[1].toInt()) {
                                                status = true
                                            } else if (verson_code_split[2].toInt() > verson_split[2].toInt()) {
                                                status = true
                                            }

                                            if (status) {

                                                Alert()


                                            } else {
                                                val intent = Intent(
                                                    this@OtpVerificationActivity,
                                                    MainActivity::class.java
                                                )
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                startActivity(intent)
                                                finish()


                                            }

                                        } catch (e: PackageManager.NameNotFoundException) {
                                            e.printStackTrace()
                                        }


                                        /* Toast.makeText(
                                             this@OtpVerificationActivity,
                                             "OTP Verification Done",
                                             Toast.LENGTH_LONG
                                         ).show()*/
                                    })
                                }
                            } else {
                                runOnUiThread(Runnable {
                                    Toast.makeText(
                                        this@OtpVerificationActivity,
                                        "OTP Verification failed!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                })
                            }


//                    val firstname = data.getJSONObject(0).get("First_N(ame").toString()
//                    val userId = data.getJSONObject(0).get("User_ID").toString()
//                        val editor = sharedPreferences.edit()
//                        editor.putBoolean(Constants.REMEMBER, checked)
//                        editor.putString(fName,firstname)
//                        editor.putString(VUSER_ID, userId)
//                        editor.apply()


//                    Log.d("Upload Status", "Image Uploaded Successfully !")
//                    runOnUiThread(Runnable {
//                        Toast.makeText(
//                            this@OtpVerificationActivity,
//                            "OTP Verified Successfully",
//                            Toast.LENGTH_LONG
//                        ).show()


                            //})
                        } else {
                            runOnUiThread(Runnable {
                                Toast.makeText(
                                    this@OtpVerificationActivity,
                                    "Verification data read failed!",
                                    Toast.LENGTH_LONG
                                ).show()
                            })
                        }
                    }
                } catch (ex: Exception) {
                    this@OtpVerificationActivity.runOnUiThread(Runnable {
                        Toast.makeText(
                            this@OtpVerificationActivity,
                            ex.message,
                            Toast.LENGTH_LONG
                        ).show()
                    });
                }
            }
        })
    }


    private fun Alert() {
        val alertDialog = AlertDialog.Builder(this)

        // Setting Dialog Title
        alertDialog.setTitle("")

        // Setting Dialog Message
        alertDialog.setMessage("New app version available , Update now")

        // On pressing Settings button
        alertDialog.setPositiveButton(
            "Yes"
        ) { dialog, which ->
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.cognifygroup.vgold")
                    )
                )

            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.cognifygroup.vgold")
                    )
                )
            }
        }

        // on pressing cancel button
        alertDialog.setNegativeButton(
            "No"
        ) { dialog, which ->
            dialog.cancel()

            val intent = Intent(
                this@OtpVerificationActivity,
                MainActivity::class.java
            )
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()

        }
        // Showing Alert Message
        alertDialog.show()
    }


//    private fun signInWithCredential(credential: PhoneAuthCredential) {
//        // inside this method we are checking if
//        // the code entered is correct or not.
//        firebaseAuth.signInWithCredential(credential)
//            .addOnCompleteListener(object : OnCompleteListener<AuthResult?> {
//                override fun onComplete(task: Task<AuthResult?>) {
//                    if (task.isSuccessful) {
//                        // if the code is correct and the task is successful
//                        // we are sending our user to new activity.
//                        okhttpReq()
//                    } else {
//                        // if the code is not correct then we are
//                        // displaying an error message to the user.
//                        Toast.makeText(
//                            this@OtpVerificationActivity,
//                            task.exception!!.message,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//            })
//    }

//    private fun sendVerificationCode(number: String) {
//        // this method is used for getting
//        // OTP on user phone number.
//        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
//            .setPhoneNumber(number) // Phone number to verify
//            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//            .setActivity(this@OtpVerificationActivity) // Activity (for callback binding)
//            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    // callback method is called on Phone auth provider.
//    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
//        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            // below method is used when
//            // OTP is sent from Firebase
//            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
//                super.onCodeSent(s, forceResendingToken)
//                // when we receive the OTP it
//                // contains a unique id which
//                // we are storing in our string
//                // which we have already created.
//                verificationId = s
//            }
//
//            // this method is called when user
//            // receive OTP from Firebase.
//            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//                // below line is used for getting OTP code
//                // which is sent in phone auth credentials.
//                val code = phoneAuthCredential.smsCode
//
//                // checking if the code
//                // is null or not.
//                if (code != null) {
//                    // if the code is not null then
//                    // we are setting that code to
//                    // our OTP edittext field.
////                    edtOTP.setText(code)
//                    cFirebaseOtp = code
////                    verifyCode(code)
//                }
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                // displaying error message with firebase exception.
//                Toast.makeText(this@OtpVerificationActivity, e.message, Toast.LENGTH_LONG).show()
//            }
//        }
//
//    // below method is use to verify code from Firebase.
//    private fun verifyCode(code: String) {
//        val credential = PhoneAuthProvider.getCredential(verificationId, code)
//
//        signInWithCredential(credential)
//    }


}

//    private fun doRegistration(){
//        var jsonobjt = JSONObject()
//        jsonobjt.put("email", eMail)
//        jsonobjt.put("otp", mobileOtp)
//        val que = Volley.newRequestQueue(this@OtpVerificationActivity)
//        val req = JsonObjectRequest(
//            Request.Method.POST, OTP_VERIFICATION, jsonobjt,
//            { response ->
//                try {
//                    que.cancelAll(this@OtpVerificationActivity)
//                    jsonobjt = response
//
//                    val code = jsonobjt.getInt("status")
//                    val msg = jsonobjt.getString("Message")
//                    val data = jsonobjt.getJSONArray("data")
//                    val checked = true
//
//
//
//                    Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
//                    if (data.length()>0){
//                        val firstname = data.getJSONObject(0).get("First_Name").toString()
//
//                        val editor = sharedPreferences.edit()
//                        editor.putBoolean(Constants.REMEMBER, checked)
//                        editor.putString(fName,firstname)
//                        editor.apply()
//
//
//
//
////                                for ( i in 0 until data.length() ){
////
////
////                                }
//                    }
//                    Toast.makeText(this, "Verify Successfully", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity ::class.java)
//                    startActivity(intent)
////                            if (code == 1000)
////                               // otpMsg()
////                            else
////                                Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }, {
//                //cProgressBarr.visibility = View.GONE
//                que.cancelAll(this@OtpVerificationActivity)
//                parseErrorMsg(it)
////                        Toast.makeText(
////                            this,
////                            "Check Your Internet Connection !",
////                            Toast.LENGTH_SHORT
////                        ).show()
//            })
//        req.setRetryPolicy(
//            DefaultRetryPolicy(
//                20 * 1000,
//                3,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            )
//        )
//        que.add(req)
//    }
//
//        override fun onDestroy() {
//            dialog.dismiss()
//            super.onDestroy()
//        }
//
//        private fun parseErrorMsg(error: VolleyError){
//            try{
//                val responseBody = String(error.networkResponse.data)
//                val errdata = JSONObject(responseBody)
//                val errMsg = errdata.getString("message_text")
//                Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show()
//            } catch (e: JSONException){
//                e.printStackTrace()
//            } catch (errorr: UnsupportedEncodingException){
//                errorr.printStackTrace()
//            }
//        }
//    }


//class OtpVerificationActivity : AppCompatActivity(){
//
//
//    private lateinit var sharedPreferences: SharedPreferences
//    private var eMail = ""
//    private var mobileOtp = ""
//    private lateinit var dialog: Dialog
//    private lateinit var emIdOtp1: EditText
//    private lateinit var emIdOtp2: EditText
//    private lateinit var emIdOtp3: EditText
//    private lateinit var emIdOtp4: EditText
//    private lateinit var mbDisp: TextView
//    private var intmob = 0
//
//    private var isRemembered = false
//
//    private lateinit var verifyOtpBtn: Button
//    private lateinit var resndOtpBtn: LinearLayout
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_otp_verification)
//
//
//
//        sharedPreferences = getSharedPreferences(VGOLD_DB, Context.MODE_PRIVATE)
//        isRemembered = sharedPreferences.getBoolean(Constants.REMEMBER, false)
//        emIdOtp1 = findViewById(R.id.et_otp_var1)
//        emIdOtp2 = findViewById(R.id.et_otp_var2)
//        emIdOtp3 = findViewById(R.id.et_otp_var3)
//        emIdOtp4 = findViewById(R.id.et_otp_var4)
//        verifyOtpBtn = findViewById(R.id.BtnVerify)
//        resndOtpBtn = findViewById(R.id.resend)
//        mbDisp = findViewById(R.id.tv_No)
//
//        eMail = intent.getStringExtra("email_id").toString()
//
//
////For Skipping Otp Screen
////       if (isRemembered) {
////            val intent = Intent(applicationContext, MainActivity::class.java)
////            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
////            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
////           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////           startActivity(intent)
////            finish()
////        }
//
//
//        dialog = Dialog(this)
//        emIdOtp1.addTextChangedListener(GenericTextWatcher(emIdOtp1, emIdOtp2))
//        emIdOtp2.addTextChangedListener(GenericTextWatcher(emIdOtp2, emIdOtp3))
//        emIdOtp3.addTextChangedListener(GenericTextWatcher(emIdOtp3, emIdOtp4))
//
//        mbDisp.text = "+91"+eMail
//
//
//        emIdOtp1.requestFocus()
//        verifyOtpBtn.setOnClickListener {
//            if (emIdOtp1.text.isEmpty()) {
//                emIdOtp1.error = "This Field is Blank !"
//                emIdOtp1.requestFocus()
//            } else if (emIdOtp2.text.isEmpty()) {
//                emIdOtp2.error = "This Field is Blank !"
//                emIdOtp2.requestFocus()
//            } else if (emIdOtp3.text.isEmpty()) {
//                emIdOtp3.error = "This Field is Blank !"
//                emIdOtp3.requestFocus()
//            } else if (emIdOtp4.text.isEmpty()) {
//                emIdOtp4.error = "This Field is Blank !"
//                emIdOtp4.requestFocus()
//            } else {
//
//                mobileOtp =
//                    emIdOtp1.text.toString() + emIdOtp2.text.toString() + emIdOtp3.text.toString() + emIdOtp4.text.toString()
//
////                intmob = mobileOtp.toInt()
//                okhttpReq()
//
//            }
//            //   eMail.setOnClickListener { onBackPressed() }
//            mbDisp.setOnClickListener {  onBackPressed() }
//        }
//    }
////
////        private fun otpMsg(){
////            dialog.setContentView(R.layout.successfully_registered_dialog)
////            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
////            val okBt = dialog.findViewById<Button>(R.id.okBt)
////            okBt.setOnClickListener {
////                val intent = Intent(this, MainActivity::class.java)
////                //val intent = Intent(this, SocialRegistrationForm::class.java)
////                intent.putExtra("companyname", firstName)
////                //intent.putExtra("midnm", midname)
////                intent.putExtra("lastname", lastName)
////                intent.putExtra("emlid", candiEmail)
////                intent.putExtra("mobno", candiMobile)
////                //intent.putExtra("mobileotp", mobileOtp)
////                //intent.putExtra("emailotp", emailOtp)
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                startActivity(intent)
////                finish()
////
////
////            }
////            dialog.show()
////            var counter = 0
////            object : CountDownTimer(2000, 500){
////                override fun onTick(p0: Long) {
////                    counter++
////                }
////
////                override fun onFinish() {
////                    okBt.performClick()
////                }
////            }.start()
////        }
//
//    private fun okhttpReq() {
//        val client = OkHttpClient().newBuilder().build()
//        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("email", eMail)
//            .addFormDataPart("otp", mobileOtp)
//            .build()
//        val request = okhttp3.Request.Builder()
//            .url("https://www.vgold.co.in/dashboard/webservices/login.php")
//            .header("Accept", "application/json")
//            .header("Content-Type", "application/json")
//            .post(requestBody)
//            .build()
//        client.newCall(request).enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                val mMessage = e.message.toString()
//                e.printStackTrace()
//                Log.e("failure Response", mMessage)
//            }
//
//            override fun onResponse(call: Call, response: okhttp3.Response) {
//                val mMessage = response.body()!!.string()
//                if (!response.isSuccessful) {
//                    throw IOException("Unexpected code" + response)
//                } else {
//                    val json = JSONObject(mMessage)
//                    val gson = Gson()
//                    val loginModel = gson.fromJson(json.toString(), LoginModel::class.java)
//                    val loginModelArrayList: ArrayList<LoginModel.Data> =
//                        loginModel.data!!
////                        (json as LoginModel).data!!
//                    val code = json.getInt("status")
//                    val msg = json.getString("Message")
//                    val data = json.getJSONArray("data")
//
//
//                    if (code == 200){
//                        VGoldApp.onSetUserDetails(
//                            loginModelArrayList[0].user_ID,
//                            loginModelArrayList[0].first_Name,
//                            loginModelArrayList[0].last_Name,
//                            loginModelArrayList[0].email,
//                            loginModelArrayList[0].mobile_no,
//                            loginModelArrayList[0].qrcode,
//                            loginModelArrayList[0].pan_no,
//                            loginModelArrayList[0].address,
//                            loginModelArrayList[0].city,
//                            loginModelArrayList[0].state,
//                            loginModelArrayList[0].profile_photo,
//                            loginModelArrayList[0].is_cp,
//                            loginModelArrayList[0].identity_photo,
//                            loginModelArrayList[0].address_photo,
//                            loginModelArrayList[0].address_photo_back,
//                            loginModelArrayList[0].aadhar_no
//                        )
//
//
//                        VGoldApp.onSetUserRole(
//                            loginModelArrayList[0].user_role,
//                            loginModelArrayList[0].validity_date
//                        )
//
//                        val checked = true
//                        val editor = sharedPreferences.edit()
//                        editor.putBoolean(Constants.REMEMBER, checked)
//                        editor.apply()
//                    }
//
////                    val firstname = data.getJSONObject(0).get("First_N(ame").toString()
////                    val userId = data.getJSONObject(0).get("User_ID").toString()
////                        val editor = sharedPreferences.edit()
////                        editor.putBoolean(Constants.REMEMBER, checked)
////                        editor.putString(fName,firstname)
////                        editor.putString(VUSER_ID, userId)
////                        editor.apply()
//
//
////                    Log.d("Upload Status", "Image Uploaded Successfully !")
//                    runOnUiThread(Runnable {
//                        Toast.makeText(
//                            this@OtpVerificationActivity,
//                            "OTP Verified Successfully",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                        val intent = Intent(this@OtpVerificationActivity, MainActivity::class.java)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        startActivity(intent)
//                        finish()
//
//                    })
//                }
//            }
//        })
//    }
//}

//    private fun doRegistration(){
//        var jsonobjt = JSONObject()
//        jsonobjt.put("email", eMail)
//        jsonobjt.put("otp", mobileOtp)
//        val que = Volley.newRequestQueue(this@OtpVerificationActivity)
//        val req = JsonObjectRequest(
//            Request.Method.POST, OTP_VERIFICATION, jsonobjt,
//            { response ->
//                try {
//                    que.cancelAll(this@OtpVerificationActivity)
//                    jsonobjt = response
//
//                    val code = jsonobjt.getInt("status")
//                    val msg = jsonobjt.getString("Message")
//                    val data = jsonobjt.getJSONArray("data")
//                    val checked = true
//
//
//
//                    Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
//                    if (data.length()>0){
//                        val firstname = data.getJSONObject(0).get("First_Name").toString()
//
//                        val editor = sharedPreferences.edit()
//                        editor.putBoolean(Constants.REMEMBER, checked)
//                        editor.putString(fName,firstname)
//                        editor.apply()
//
//
//
//
////                                for ( i in 0 until data.length() ){
////
////
////                                }
//                    }
//                    Toast.makeText(this, "Verify Successfully", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, MainActivity ::class.java)
//                    startActivity(intent)
////                            if (code == 1000)
////                               // otpMsg()
////                            else
////                                Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }, {
//                //cProgressBarr.visibility = View.GONE
//                que.cancelAll(this@OtpVerificationActivity)
//                parseErrorMsg(it)
////                        Toast.makeText(
////                            this,
////                            "Check Your Internet Connection !",
////                            Toast.LENGTH_SHORT
////                        ).show()
//            })
//        req.setRetryPolicy(
//            DefaultRetryPolicy(
//                20 * 1000,
//                3,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            )
//        )
//        que.add(req)
//    }
//
//        override fun onDestroy() {
//            dialog.dismiss()
//            super.onDestroy()
//        }
//
//        private fun parseErrorMsg(error: VolleyError){
//            try{
//                val responseBody = String(error.networkResponse.data)
//                val errdata = JSONObject(responseBody)
//                val errMsg = errdata.getString("message_text")
//                Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show()
//            } catch (e: JSONException){
//                e.printStackTrace()
//            } catch (errorr: UnsupportedEncodingException){
//                errorr.printStackTrace()
//            }
//        }
//    }

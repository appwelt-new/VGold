package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.getrefercode.ReferModel
import com.cognifygroup.vgold.getrefercode.ReferServiceProvider
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.dynamiclinks.DynamicLink.AndroidParameters
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class ReferActivity : AppCompatActivity(), AlertDialogOkListener {
    var mAlert: AlertDialogs? = null
    var referServiceProvider: ReferServiceProvider? = null

    var edtNameR: EditText? = null
    var edtEmailR: EditText? = null
    var edtMobileR: EditText? = null

    var btnSubmitR: Button? = null
    var imgContact: ImageView? = null

    private var contactID: String? = null

    private val REQUEST_CODE_PICK_CONTACTS = 1
    private var uriContact: Uri? = null
    var contactNumber1 = ""
    var flag = false

    var no: String? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer)

        FirebaseApp.initializeApp(this)

        edtNameR = findViewById(R.id.edtNameR)
        edtEmailR = findViewById(R.id.edtEmailR)
        edtMobileR = findViewById(R.id.edtMobileR)
        imgContact = findViewById(R.id.imgContact)
        btnSubmitR = findViewById(R.id.btnSubmitR)

        supportActionBar?.title = "Refer To Friend "
        sharedPreferences =
            this@ReferActivity.getSharedPreferences(
                Constants.VGOLD_DB,
                Context.MODE_PRIVATE
            )
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()

        btnSubmitR!!.setOnClickListener {
            onClickOfBtnRefer()
        }
        imgContact!!.setOnClickListener {
            onClickContactBook()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {

        mAlert = AlertDialogs().getInstance()
        progressDialog = TransparentProgressDialog(this@ReferActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        referServiceProvider = ReferServiceProvider(this)
        if (intent.hasExtra("no")) {
            no = intent.getStringExtra("no")
        }
        edtMobileR!!.setText(no)

    }


    fun onClickOfBtnRefer() {
        val email: String = edtEmailR!!.text.toString()
        val name: String = edtNameR!!.text.toString()
        val mobile: String = edtMobileR!!.text.toString()
        if (email != "" && email != null && name != "" && name != null && mobile != "" && mobile != null) {
            getReferCode(userId)
        } else {
            AlertDialogs().alertDialogOk(
                this@ReferActivity, "Alert", "All Data required",
                resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
            )
            //            Toast.makeText(ReferActivity.this, "All Data required", Toast.LENGTH_LONG).show();
        }
    }

    fun onClickContactBook() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
            REQUEST_CODE_PICK_CONTACTS
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
//            Log.d(TAG, "Response: " + data.toString());
//            uriContact = data.getData();
            var cursor: Cursor? = null
            val cursorMobile: Cursor? = null
            var contactNumber: String? = ""
            var email = ""
            var name: String? = ""
            val mobile = ""
            try {
                if (data != null) {
                    uriContact = data.data
                }
                contactNumber = retrieveContactNumber()
                edtMobileR!!.setText(contactNumber)

                // get the contact id from the Uri
                val id = uriContact!!.lastPathSegment

                // query for everything email
                cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
                    arrayOf(id),
                    null
                )
                val nameId = cursor!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)

                // let's just get the first email
                if (cursor.moveToFirst()) {
                    email = cursor.getString(emailIdx)
                    name = cursor.getString(nameId)
                    edtEmailR!!.setText(email)
                    edtNameR!!.setText(name)
                    Log.e("TAG", "Got email: $email")
                } else {
                    val contactName = retrieveContactName()
                    edtNameR!!.setText(contactName)
                    Log.e("TAG", "No results")
                }
            } catch (e: java.lang.Exception) {
                Log.e("TAG", "Failed to get email data", e)
            } finally {
                cursor?.close()
            }


//            String contactName = retrieveContactName();
//                 contactNumber = retrieveContactNumber(cursor);
//            String contactEmail = retrieveContactEmail();
//
//            edtMobileR.setText(contactNumber);
//            edtNameR.setText(contactName);
//            if (contactEmail != null) {
//                edtEmailR.setText(contactEmail);
//            }
        }
    }

    @SuppressLint("Range")
    private fun retrieveContactNumber(): String? {
        var contactNumber: String? = null

        // getting contacts ID
        val cursorID = contentResolver.query(
            uriContact!!, arrayOf(ContactsContract.Contacts._ID),
            null, null, null
        )
        if (cursorID!!.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID))
        }
        cursorID.close()

        // Using the contact ID now we will get contact phone number
        val cursorPhone = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                    ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
            arrayOf<String>(contactID!!),
            null
        )
        if (cursorPhone!!.moveToFirst()) {
            contactNumber =
                cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
        }
        cursorPhone.close()

        // Toast.makeText(ContactListActivity.this,contactNumber,Toast.LENGTH_LONG).show();

//        Log.d(TAG, "Contact Phone Number: " + contactNumber);
        contactNumber = contactNumber!!.replace("\\s+".toRegex(), "")
        if (contactNumber.startsWith("+")) {
            contactNumber1 = contactNumber.substring(3)
            flag = true
            //contactNumber = contactNumber.replace("91", "");
            return contactNumber1
        } else {
            flag = false
            return contactNumber
        }
    }

    @SuppressLint("Range")
    private fun retrieveContactName(): String? {
        var contactName: String? = null
        // querying contact data store
        val cursor = contentResolver.query((uriContact)!!, null, null, null, null)
        if (cursor!!.moveToFirst()) {
            contactName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
        }
        cursor.close()
        return contactName
    }

    private fun getReferCode(user_id: String?) {
        //   progressDialog!!.show()
        /*referServiceProvider!!.getReferenceCode(user_id, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    progressDialog!!.hide()
                    val status = (serviceResponse as ReferModel).getStatus()
                    val message = (serviceResponse as ReferModel).getMessage()
                    val data = (serviceResponse as ReferModel).getData()
                    if ((status == "200")) {
                        if (data != null && !TextUtils.isEmpty(data)) {
                            createDynamicLink(data)
                        }
                    } else {
//                        mAlert.onShowToastNotification(ReferActivity.this, message);
                        AlertDialogs().alertDialogOk(
                            this@ReferActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    progressDialog!!.hide()
                } finally {
                    progressDialog!!.hide()
                }
            }

            override fun <T> onFailure(apiErrorModel: T, extras: T) {

                try {
                    progressDialog!!.hide()
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@ReferActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@ReferActivity)
                    }
                } catch (e: java.lang.Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@ReferActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })*/


        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_referal_code.php")
//            .header("AUTHORIZATION", "Bearer $token")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                e.printStackTrace()
                progressDialog!!.hide()

                Log.e("failure Response", mMessage)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val mMessage = response.body()!!.string()
                val jsonString: String = mMessage //http request
                val json2 = JSONObject(mMessage)
                val message = json2.get("Message").toString()
                progressDialog!!.hide()
              //  var dataValue = ReferModel()
               // val gson = Gson()
              //  dataValue = gson.fromJson(jsonString, ReferModel::class.java)
                var data = json2.optString("data").toString()

                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    val json = JSONObject(mMessage)
                    val status = json.get("status").toString()
                    if ((status == "200")) {
                        if (!TextUtils.isEmpty(data)) {
                            runOnUiThread {
                                createDynamicLink(data)
                            }
                        }
                    } else {
//                        mAlert.onShowToastNotification(ReferActivity.this, message);
                        runOnUiThread {
                            AlertDialogs().alertDialogOk(
                                this@ReferActivity,
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

    private fun createDynamicLink(data: String) {

        val link =
            "https://play.google.com/store/apps/details?id=com.cognifygroup.vgold&hl=en_IN&gl=US" + "&referrer=" + data

        Log.i("TAG", "createDynamicLink: " + link)

        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(link))
            .setDomainUriPrefix("https://cognifygroup.page.link")
            .setAndroidParameters(
                AndroidParameters.Builder("com.cognifygroup.vgold")
                    .setMinimumVersion(1)
                    .build()
            )
            /*.setIosParameters(
                IosParameters.Builder("com.cognifygroup.vgold")
                    .setAppStoreId("123456789")
                    .setMinimumVersion("1.0.1")
                    .build()
            )*/
            .buildShortDynamicLink()
            .addOnSuccessListener { shortDynamicLink ->
                val mInvitationUrl: String = shortDynamicLink.shortLink.toString()
                if (mInvitationUrl != null && !TextUtils.isEmpty(mInvitationUrl)) {

//                            Log.d("TAG", mInvitationUrl);
                    AttemptToRefer(
                        userId,
                        edtNameR!!.text.toString(),
                        edtEmailR!!.text.toString(),
                        edtMobileR!!.text.toString(),
                        mInvitationUrl
                    )
                }
            }
    }

    private fun AttemptToRefer(
        user_id: String?,
        name: String,
        email: String,
        mobile_no: String,
        refLink: String
    ) {
        // Toast.makeText(this@ReferActivity, "" + refLink, Toast.LENGTH_SHORT).show()
        /* progressDialog!!.show()
         referServiceProvider!!.getAddBankDetails(
             user_id,
             name,
             email,
             mobile_no,
             refLink,
             object : APICallback {
                 override fun <T> onSuccess(serviceResponse: T) {
                     try {
                         val status = (serviceResponse as ReferModel).getStatus()
                         val message = (serviceResponse as ReferModel).getMessage()
                         if ((status == "200")) {
                             AlertDialogs().alertDialogOk(
                                 this@ReferActivity,
                                 "Alert",
                                 message,
                                 resources.getString(R.string.btn_ok),
                                 1,
                                 false,
                                 alertDialogOkListener
                             )

*//*     //                        mAlert.onShowToastNotification(ReferActivity.this, message);
                                   Intent intent = new Intent(ReferActivity.this, MainActivity.class);
                        startActivity(intent);*//*
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@ReferActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )

//                        mAlert.onShowToastNotification(ReferActivity.this, message);
//                        Intent intent = new Intent(ReferActivity.this, MainActivity.class);
//                        startActivity(intent);
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    } finally {
                        progressDialog!!.hide()
                    }
                }

                override fun <T> onFailure(apiErrorModel: T, extras: T) {

                    try {
                        if (apiErrorModel != null) {
                            PrintUtil.showToast(
                                this@ReferActivity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(this@ReferActivity)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(this@ReferActivity)
                    } finally {
                        progressDialog!!.hide()
                    }
                }
            })
*/

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("name", name)
            .addFormDataPart("email", email)
            .addFormDataPart("mobile_no", mobile_no)
            .addFormDataPart("refer_link", refLink)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/send_refer_link.php")
//            .header("AUTHORIZATION", "Bearer $token")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                e.printStackTrace()
                progressDialog!!.hide()

                Log.e("failure Response", mMessage)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val mMessage = response.body()!!.string()
                val json2 = JSONObject(mMessage)
                val message = json2.get("Message").toString()
                progressDialog!!.hide()

                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
                    val json = JSONObject(mMessage)
                    val status = json.get("status").toString()
                    runOnUiThread {

                        if ((status == "200")) {
                            AlertDialogs().alertDialogOk(
                                this@ReferActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )

                        } else {
                            AlertDialogs().alertDialogOk(
                                this@ReferActivity,
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

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            1 -> {
                val intent = Intent(this@ReferActivity, MainActivity::class.java)
                startActivity(intent)
            }
            11 -> {
                val LogIntent = Intent(this@ReferActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

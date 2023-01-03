package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class ContactListActivity : AppCompatActivity() ,AlertDialogOkListener{
    private val TAG = ContactListActivity::class.java.simpleName
    private val REQUEST_CODE_PICK_CONTACTS = 1
    private var uriContact: Uri? = null
    private var contactID: String? = null

    var imgContact: ImageView? = null
    var edtMobileNumber: EditText? = null
    var btnProceed: Button? = null
    var contactNumber1 = ""
    var flag = false

    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        imgContact = findViewById(R.id.imgContact)
        edtMobileNumber = findViewById(R.id.edtMobileNumber)
        btnProceed = findViewById(R.id.btnProceed)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        progressDialog = TransparentProgressDialog(this@ContactListActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)

        loginStatusServiceProvider = LoginStatusServiceProvider(this)

//        checkLoginSession()

        imgContact!!.setOnClickListener {
            onImgContactClick()
        }
        btnProceed!!.setOnClickListener {
            onbtnProceedClick()
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
                                this@ContactListActivity,
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
                            this@ContactListActivity, "Alert", message,
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
                            this@ContactListActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@ContactListActivity)
                    }
                } catch (e: Exception) {
                    progressDialog!!.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@ContactListActivity)
                } finally {
                    progressDialog!!.hide()
                }
            }
        })
    }

    fun onImgContactClick() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
           REQUEST_CODE_PICK_CONTACTS
        )
    }

    fun onbtnProceedClick() {
        val intent = Intent(this@ContactListActivity, PayActivity::class.java)
        intent.putExtra("mobileno", edtMobileNumber!!.text.toString())
        intent.putExtra("whichactivity", "3")
        startActivity(intent)
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
//            Log.d(TAG, "Response: " + data.toString());
            if (data != null) {
                uriContact = data.data
            }
            val contactName = retrieveContactName()
            val contactNumber = retrieveContactNumber()
            val intent = Intent(this@ContactListActivity, PayActivity::class.java)
            intent.putExtra("name", contactName)
            if (flag) {
                intent.putExtra("number", contactNumber1)
            } else {
                intent.putExtra("number", contactNumber)
            }
            intent.putExtra("whichactivity", "1")
            startActivity(intent)
        }
    }

    @SuppressLint("Range")
    private fun retrieveContactNumber(): String {
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

//        Log.d(TAG, "Contact ID: " + contactID);

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

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
            contactName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
        }
        cursor.close()
        // Toast.makeText(ContactListActivity.this,contactName,Toast.LENGTH_LONG).show();
//        Log.d(TAG, "Contact Name: " + contactName);
        return contactName
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@ContactListActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }

}
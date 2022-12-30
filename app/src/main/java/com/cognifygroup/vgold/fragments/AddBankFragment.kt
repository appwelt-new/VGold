package com.cognifygroup.vgold.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.Success1
import com.cognifygroup.vgold.utilities.Constants
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.IOException

class AddBankFragment : Fragment() {

    private var userId = ""
    private lateinit var edtBrName: EditText
    private lateinit var edtBankName: EditText
    private lateinit var edtAcNo: EditText
    private lateinit var edtAcType: EditText
    private lateinit var edtIfscCode: EditText
    private lateinit var edtBHolderName: EditText
    private lateinit var btnSubmit: Button
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "Add Bank"

        sharedPreferences =
            requireContext().getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()
        val v: View = inflater.inflate(R.layout.fragment_add_bank, container, false)

        edtBrName = v.findViewById(R.id.edtBranch)
        edtBankName = v.findViewById(R.id.edtBankName)
        edtAcNo = v.findViewById(R.id.edtAcc_no)
        edtAcType = v.findViewById(R.id.edtAcc_type)
        edtIfscCode = v.findViewById(R.id.edtIfsc)
        edtBHolderName = v.findViewById(R.id.edtAcc_holder_name)
        btnSubmit = v.findViewById(R.id.btnSubmit)


        btnSubmit.setOnClickListener {
            if (edtBankName.text.isEmpty()) {
                edtBankName.error = "Please Enter the  Bank Name !"
                edtBankName.requestFocus()
            } else if (edtBrName.text.isEmpty()) {
                edtBrName.error = "Please Enter the  Branch Name !"
                edtBrName.requestFocus()
            } else if (edtAcNo.text.isEmpty()) {
                edtAcNo.error = "Please Enter the account Number !"
                edtAcNo.requestFocus()
            } else if (edtAcType.text.isEmpty()) {
                edtAcType.error = "Please Enter the Account Type !"
                edtAcType.requestFocus()
            } else if (edtIfscCode.text.isEmpty()) {
                edtIfscCode.error = "Please Enter the IFSC Code !"
                edtIfscCode.requestFocus()
            } else if (edtBHolderName.text.isEmpty()) {
                edtBHolderName.error = "Please Enter the Bank Holder Name !"
                edtBHolderName.requestFocus()

            } else {
                AddBank()
            }
        }
        return v
    }

    private fun AddBank() {

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("name", edtBHolderName.text.toString())
            .addFormDataPart("bank_name", edtBankName.text.toString())
            .addFormDataPart("acc_no", edtAcNo.text.toString())

            .addFormDataPart("ifsc", edtIfscCode.text.toString())
            .addFormDataPart("acc_type", edtAcType.text.toString())
            .addFormDataPart("branch", edtBrName.text.toString())




            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/bank_details.php")
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
                    //runOnUiThread(Runnable {
                        val intent = Intent(requireContext(), Success1::class.java)
                         intent.putExtra("title", "Add Bank")
                        startActivity(intent)
                        // Toast.makeText(this@LoginActivity, ".$mMessage", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }
}
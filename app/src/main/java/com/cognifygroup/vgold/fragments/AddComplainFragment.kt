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


class AddComplainFragment : Fragment() {

    private var userId = ""
    private lateinit var edt_complain: EditText
    private lateinit var btnSubmitComplain: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "Complaint"
        sharedPreferences =
            requireContext().getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()
        val v: View = inflater.inflate(R.layout.fragment_add_complain, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Complaint"

        edt_complain = v.findViewById(R.id.edt_complain)
        btnSubmitComplain = v.findViewById(R.id.btnSubmitComplain)


        btnSubmitComplain.setOnClickListener {
            if (edt_complain.text.isEmpty()) {
                edt_complain.error = "Please Enter the   Complaint !"
                edt_complain.requestFocus()

            } else {
                AddComplaint()
            }
        }

        return v
    }
    private fun AddComplaint() {

        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("complaint", edt_complain.text.toString())


            .build()
        val request = okhttp3.Request.Builder()
            .url("http://vgold.co.in/dashboard/webservices/process_complaints.php?")
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
                  //  runOnUiThread(Runnable {

//                        Toast.makeText(
//                            requireContext(),
//                            "Add Complaint Successfully",
//                            Toast.LENGTH_LONG
//                        ).show()
                    val intent = Intent(requireContext(), Success1::class.java)
                    intent.putExtra("title", "Add Complaint")
                    startActivity(intent)
                    // Toast.makeText(this@LoginActivity, ".$mMessage", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

}
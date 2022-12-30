package com.cognifygroup.vgold.fragments

import android.annotation.SuppressLint
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
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.GoldBookingActivity
import com.cognifygroup.vgold.adapters.GoldDepositeHistoryAdapter
import com.cognifygroup.vgold.model.GoldDepositeHistoryItem
import com.cognifygroup.vgold.utilities.Constants
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException


class GoldDepositFragment : Fragment() {
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gPrgBar: ProgressBar
    private lateinit var gRecyclerView: RecyclerView
    private lateinit var depositGoldBtn: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = "Gold Deposite History"
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_gold_deposit, container, false)

        sharedPreferences = requireContext().getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()
        //  gPrgBar = v.findViewById(R.id.prgsbar)
        gRecyclerView = v.findViewById(R.id.rvGoldDepositeHistory)
        depositGoldBtn = v.findViewById(R.id.btnDepositeGold)
        depositGoldBtn.isEnabled = false
        gPrgBar = v.findViewById(R.id.prgsbar)

        getVGoldDepositeHistory()

        depositGoldBtn.setOnClickListener {
            val intent = Intent(requireContext(), GoldBookingActivity::class.java)
            startActivity(intent)
        }

        return v
    }

    private fun getVGoldDepositeHistory() {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/gold_deposite_history.php?")
//            .header("AUTHORIZATION", "Bearer $token")
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
                    val json = JSONObject(mMessage)
                    val prdds = json.getJSONArray("Data")


//                    Log.d("Upload Status", "Image Uploaded Successfully !")
                    requireActivity().runOnUiThread(Runnable {

                        val cndList = ArrayList<GoldDepositeHistoryItem>()

                        if (prdds.length() > 0){
                            gPrgBar.visibility = View.GONE
                            depositGoldBtn.isEnabled = true
                            gRecyclerView.visibility = View.VISIBLE
                            for (i in 0 until prdds.length()){
                                val item = GoldDepositeHistoryItem(
                                    prdds.getJSONObject(i).getString("account_status"),
                                    prdds.getJSONObject(i).getString("transaction_id"),
                                    prdds.getJSONObject(i).getString("admin_status"),
                                    prdds.getJSONObject(i).getString("gold_quality"),
                                    prdds.getJSONObject(i).getString("amount"),
                                    prdds.getJSONObject(i).getString("vendor_status"),
                                    prdds.getJSONObject(i).getString("added_date"),
                                    prdds.getJSONObject(i).getString("bank_guarantee"),
                                    prdds.getJSONObject(i).getString("maturity_weight"),
                                    prdds.getJSONObject(i).getString("rate"),
                                    prdds.getJSONObject(i).getString("tennure"),
                                    prdds.getJSONObject(i).getString("gold"),
                                    prdds.getJSONObject(i).getString("vendor_id"),
                                    prdds.getJSONObject(i).getString("gold_deposite_id"),
                                    prdds.getJSONObject(i).getString("current_value_amount"),
                                    prdds.getJSONObject(i).getString("status_name"),
                                    prdds.getJSONObject(i).getString("addpurity"),
                                    prdds.getJSONObject(i).getString("remark"),
                                    prdds.getJSONObject(i).getString("closing_date")
                                )
                                cndList += item
                            }
                            gRecyclerView.adapter= GoldDepositeHistoryAdapter(cndList)
                            gRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                            gRecyclerView.setHasFixedSize(false)
                        }

                    })
                }
            }
        })
    }

}
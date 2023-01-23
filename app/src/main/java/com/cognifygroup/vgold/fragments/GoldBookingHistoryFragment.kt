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
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.GoldBookingActivity
import com.cognifygroup.vgold.adapters.GoldBookingHistoryAdapter
import com.cognifygroup.vgold.model.GoldBookingHistoryItem
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.Constants.Companion.VUSER_ID
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class GoldBookingHistoryFragment : Fragment() {
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gPrgBar: ProgressBar
    private lateinit var gRecyclerView: RecyclerView
    private lateinit var gbookGoldBtn: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_gold_booking_history, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Gold Booking History"
        sharedPreferences =
            requireContext().getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(VUSER_ID, null).toString()
      //  gPrgBar = v.findViewById(R.id.prgsbar)
        gRecyclerView = v.findViewById(R.id.rvGoldBookingHistory)
        gbookGoldBtn = v.findViewById(R.id.btnBookGold)
        gbookGoldBtn.isEnabled = false
        gPrgBar = v.findViewById(R.id.prgsbar)

        getVGoldBookingHistory()

        gbookGoldBtn.setOnClickListener {
            val intent = Intent(requireContext(), GoldBookingActivity::class.java)
            startActivity(intent)
        }
        return v
    }



    private fun getVGoldBookingHistory() {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/gold_booking_history.php")
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

                        val cndList = ArrayList<GoldBookingHistoryItem>()

                        if (prdds.length() > 0){
                            gPrgBar.visibility = View.GONE
                            gbookGoldBtn.isEnabled = true
                            gRecyclerView.visibility = View.VISIBLE
                            for (i in 0 until prdds.length()){
                                val item = GoldBookingHistoryItem(
                                    prdds.getJSONObject(i).getString( "added_date"),
                                    prdds.getJSONObject(i).getString("gold_booking_id"),
                                    prdds.getJSONObject(i).getString("gold")+" gm",
                                    resources.getString(R.string.rs)+prdds.getJSONObject(i).getString("rate"),
                                    resources.getString(R.string.rs)+ prdds.getJSONObject(i).getString("booking_amount"),
                                    resources.getString(R.string.rs)+prdds.getJSONObject(i).getString("booking_charge"),
                                    prdds.getJSONObject(i).getString("down_payment"),
                                    prdds.getJSONObject(i).getString("monthly_installment"),
                                    prdds.getJSONObject(i).getString("tennure"),
                                    prdds.getJSONObject(i).getString("total_paid_amount"),
                                    resources.getString(R.string.rs)+prdds.getJSONObject(i).getString("total_balance_amount"),
                                    prdds.getJSONObject(i).getString("todays_gain"),
                                    prdds.getJSONObject(i).getString("totalPaidInstallment"),
                                    prdds.getJSONObject(i).getString("closing_date"),
                                    prdds.getJSONObject(i).getString("account_status"),
                                )
                                cndList += item
                            }
                            gRecyclerView.adapter= GoldBookingHistoryAdapter(cndList)
                            gRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                            gRecyclerView.setHasFixedSize(false)
                        }

                    })
                }
            }
        })
    }
}
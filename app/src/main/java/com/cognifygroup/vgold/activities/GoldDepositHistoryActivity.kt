package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.adapters.GoldDepositeHistoryAdapter
import com.cognifygroup.vgold.model.GoldDepositeHistoryItem
import com.cognifygroup.vgold.utilities.Constants
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class GoldDepositHistoryActivity : AppCompatActivity() {
    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gPrgBar: ProgressBar
    private lateinit var gRecyclerView: RecyclerView
    private lateinit var depositGoldBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold_deposit_history)

        supportActionBar?.title = "Gold Deposite History"


        sharedPreferences = this@GoldDepositHistoryActivity.getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()
        //  gPrgBar = v.findViewById(R.id.prgsbar)
        gRecyclerView = findViewById(R.id.rvGoldDepositeHistory)
        depositGoldBtn = findViewById(R.id.btnDepositeGold)
        depositGoldBtn.isEnabled = false
        gPrgBar = findViewById(R.id.prgsbar)

        getVGoldDepositeHistory()

        depositGoldBtn.setOnClickListener {
            val intent = Intent(this@GoldDepositHistoryActivity, DepositGoldActivity::class.java)
            startActivity(intent)
        }

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
                gPrgBar.visibility = View.GONE
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
                    this@GoldDepositHistoryActivity.runOnUiThread(Runnable {

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
                                    resources.getString(R.string.rs)+prdds.getJSONObject(i).getString("amount"),
                                    prdds.getJSONObject(i).getString("vendor_status"),
                                    prdds.getJSONObject(i).getString("added_date"),
                                    prdds.getJSONObject(i).getString("bank_guarantee"),
                                    prdds.getJSONObject(i).getString("maturity_weight")+" gm",
                                    resources.getString(R.string.rs)+ prdds.getJSONObject(i).getString("rate"),
                                    prdds.getJSONObject(i).getString("tennure"),
                                    prdds.getJSONObject(i).getString("gold")+" gm",
                                    prdds.getJSONObject(i).getString("vendor_id"),
                                    prdds.getJSONObject(i).getString("gold_deposite_id"),
                                    resources.getString(R.string.rs)+prdds.getJSONObject(i).getString("current_value_amount"),
                                    prdds.getJSONObject(i).getString("status_name"),
                                    prdds.getJSONObject(i).getString("addpurity"),
                                    prdds.getJSONObject(i).getString("remark"),
                                    prdds.getJSONObject(i).getString("closing_date")
                                )
                                cndList += item
                            }
                            gRecyclerView.adapter= GoldDepositeHistoryAdapter(cndList)
                            gRecyclerView.layoutManager = LinearLayoutManager(this@GoldDepositHistoryActivity)
                            gRecyclerView.setHasFixedSize(false)
                        }else{
                            gPrgBar.visibility = View.GONE
                        }

                    })
                }
            }
        })
    }

}
package com.cognifygroup.vgold.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.cognifygroup.vgold.BuildConfig
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.*
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.utilities.Constants
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.security.cert.Certificate

class MainFragment : Fragment(), AlertDialogOkListener {

    //private lateinit var gbookGoldBtn: Button
    private lateinit var btncancel: Button
    private lateinit var imgrefer: ImageView
    private lateinit var imgassociate: ImageView
    private lateinit var imgGWallet: ImageView

    private lateinit var imgAddGold: ImageView
    private lateinit var trnsferGold: ImageView
    private lateinit var saleGold: ImageView
    private lateinit var moneyWallet: ImageView
    private lateinit var addMoney: ImageView
    private lateinit var trnsfrMoney: ImageView

    private lateinit var withdrwMoney: ImageView
    private lateinit var goldBooking: ImageView
    private lateinit var imgPay: ImageView
    private lateinit var goldDeposite: ImageView
    private lateinit var imgPlan: ImageView
    private lateinit var imgLoan: ImageView
    private lateinit var layoutgoldrate: LinearLayout
    private lateinit var total_gain: TextView
    private lateinit var loanAmt: TextView
    private lateinit var certificate: TextView
    private lateinit var bepartner: TextView
    private lateinit var imgshare: ImageView
    private lateinit var imgbookgold: ImageView
    private lateinit var oldrteimg: ImageView
    private lateinit var imggoldRate: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gainLayout: LinearLayout
    private lateinit var partnerLayout: LinearLayout
    private lateinit var loanLayout: LinearLayout
    private lateinit var certificateLayout: LinearLayout
    private val alertDialogOkListener: AlertDialogOkListener = this

    private var gain = ""
    private var loanVal = ""
    private var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_main, container, false)
        sharedPreferences =
            requireContext().getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()


        total_gain = v.findViewById(R.id.total_gain)
        loanAmt = v.findViewById(R.id.loanAmt)
        certificate = v.findViewById(R.id.certificate)
        bepartner = v.findViewById(R.id.be_part)

        gainLayout = v.findViewById(R.id.gainLayout)
        loanLayout = v.findViewById(R.id.loanLayout)
        partnerLayout = v.findViewById(R.id.partnerLayout)
        certificateLayout = v.findViewById(R.id.certificateLayout)
        //gbookGoldBtn = v.findViewById(R.id.btnBookGold)
        imgrefer = v.findViewById(R.id.imgRefer)
        imgassociate = v.findViewById(R.id.imgVender)
        imgGWallet = v.findViewById(R.id.imgGoldWallet)


        imgAddGold = v.findViewById(R.id.imgAddGold)
        addMoney = v.findViewById(R.id.imgAddMoney)
        trnsferGold = v.findViewById(R.id.imgTransferGold)
        imggoldRate = v.findViewById(R.id.imgTodayGoldRate)
        imgshare = v.findViewById(R.id.btn_share)
        imgbookgold = v.findViewById(R.id.btn_bookgold)
        oldrteimg = v.findViewById(R.id.oldrteimg)
        // btncancel = v.findViewById(R.id.btn_cancel)

        // layoutgoldrate = v.findViewById(R.id.layoutgoldrate)

        saleGold = v.findViewById(R.id.imgSaleGold)
        moneyWallet = v.findViewById(R.id.imgMoneyWallet)

        trnsfrMoney = v.findViewById(R.id.imgTransferMoney)
        withdrwMoney = v.findViewById(R.id.imgWithdrawMoney)
        goldBooking = v.findViewById(R.id.imgGoldBooking)
        imgPay = v.findViewById(R.id.imgPayInstallment)
        goldDeposite = v.findViewById(R.id.imgGoldDeposite)

        imgPlan = v.findViewById(R.id.imgPlan)
        imgLoan = v.findViewById(R.id.imgLoan)

//
//        Picasso.with(requireContext()).load("https://www.vgold.co.in/dashboard/vgold_rate/vgold_rate_card_mobile.php")
//            .into(imggoldRate)


//        Glide.with(requireContext())
//            .load("https://vgold.co.in/dashboard/vgold_rate/vgold_rate_card_mobile.php")
//            .into(imggoldRate);

        Glide.with(requireContext())
            .load("https://www.vgold.co.in/dashboard/vgold_rate/vgold_rate_show_mobile.png")
            .into(imggoldRate);

        Picasso.with(requireContext()).load("https://www.vgold.co.in/vgold_rate_show.png")
            .into(oldrteimg)
        getGoldBookingGain()
        getLoanVal()
        blink()



        loanAmt.setOnClickListener {
            val intent = Intent(requireContext(), LoanActivity::class.java)
            startActivity(intent)
        }

        certificate.setOnClickListener {
            val intent = Intent(requireContext(), CertificatesActivity::class.java)
            startActivity(intent)
        }

        bepartner.setOnClickListener {
            bePartnerDialog()
        }

//
//        gbookGoldBtn.setOnClickListener {
//            val intent = Intent(requireContext(), GoldBookingActivity::class.java)
//            startActivity(intent)
//        }

//        btncancel.setOnClickListener {
//
//
//               // dialog.dismiss()
//
//        }

        withdrwMoney.setOnClickListener {
            try {
                val intent = Intent(requireContext(), WithdrawActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }

        }
        imgPlan.setOnClickListener {
            val intent = Intent(requireContext(), PlanActivity::class.java)
            startActivity(intent)
        }
        addMoney.setOnClickListener {
            try {
                val intent = Intent(requireContext(), AddMoneyActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        saleGold.setOnClickListener {

            try {
                val intent = Intent(requireContext(), SellGoldActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }


        }
        goldBooking.setOnClickListener {

            //   parentFragmentManager.beginTransaction().replace(R.id.frg_frame, GoldBookingHistoryFragment()).commit()

            val intent = Intent(requireContext(), GoldBookingHistory::class.java)
            startActivity(intent)

        }
        goldDeposite.setOnClickListener {

            val intent = Intent(requireContext(), GoldDepositHistoryActivity::class.java)
            startActivity(intent)
        }

        imgLoan.setOnClickListener {
            try {
                val intent = Intent(requireContext(), LoanActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        trnsferGold.setOnClickListener {

            if (VGoldApp.onGetUserRole().equals("member")) {
                val intent = Intent(requireContext(), ScanActivity::class.java)
                startActivity(intent)
            } else {
                AlertDialogs().alertDialogOk(
                    requireContext(),
                    "Alert",
                    "You do not have this Privilege as you are not a registered member",
                    resources.getString(R.string.btn_ok),
                    0,
                    false,
                    alertDialogOkListener
                )
            }
        }
        imgPay.setOnClickListener {
            try {
                val intent = Intent(requireContext(), PayInstallmentActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }

        }

        trnsfrMoney.setOnClickListener {
            val intent = Intent(requireContext(), ScanActivityForMoney::class.java)
            startActivity(intent)
        }

        imggoldRate.setOnClickListener {

            //   parentFragmentManager.beginTransaction().replace(R.id.frg_frame, GoldBookingHistoryFragment()).commit()


            val intent = Intent(requireContext(), GoldBookingActivity::class.java)
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        moneyWallet.setOnClickListener {

            try {
                startActivity(Intent(requireActivity(), MoneyWalletActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }
//            val intent = Intent(requireActivity(), GoldWalletActivity::class.java)
//            startActivity(intent)

        }


        imgGWallet.setOnClickListener {

            try {
                startActivity(Intent(requireActivity(), GoldWalletActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, "", Toast.LENGTH_SHORT).show()
            }
//            val intent = Intent(requireActivity(), GoldWalletActivity::class.java)
//            startActivity(intent)

        }

        imgAddGold.setOnClickListener {

            try {
                startActivity(Intent(requireActivity(), AddGoldActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show()
            }
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        }

//        imgrefer.setOnClickListener {
//            val intent = Intent(requireActivity(), ReferActivity::class.java)
//            startActivity(intent)
//
//        }

        imgassociate.setOnClickListener {

            try {
                val intent = Intent(requireActivity(), OurVendersActivity::class.java)
//
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this.context, "" + e.toString(), Toast.LENGTH_SHORT).show()
            }

        }

        imgbookgold.setOnClickListener {
            val intent = Intent(requireContext(), GoldBookingActivity::class.java)
            startActivity(intent)
        }

        imgshare.setOnClickListener {

            val bmpUri: Uri? = getLocalBitmapUri(oldrteimg)
            if (bmpUri != null) {
                // Construct a ShareIntent with link to image

                // Construct a ShareIntent with link to image
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                shareIntent.type = "image/*"
                // Launch sharing dialog for image
                // Launch sharing dialog for image

                Log.i("TAG", "onCreateView: " + bmpUri)
                Log.i("TAG", "onCreateView: " + oldrteimg)

                startActivity(Intent.createChooser(shareIntent, "Share Image"))

            }
        }

        return v
    }

    fun bePartnerDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_be_partner_dialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val window = dialog.window!!
        window.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val title = dialog.findViewById<TextView>(R.id.title)
        val btnSubmit = dialog.findViewById<TextView>(R.id.btn_submit)

        title.setOnClickListener { dialog.dismiss() }

//        if (VGoldApp.onGetIsCP().equals(1)) {
//            btnSubmit.visibility = View.GONE
//        } else {
//            btnSubmit.visibility = View.VISIBLE
//        }
        //  btnSubmit.setOnClickListener { bePartnerCall() }

        dialog.show()

    }

    fun getLocalBitmapUri(imageView: ImageView): Uri? {
        // Extract Bitmap from ImageView drawable
        val drawable = imageView.drawable
        var bmp: Bitmap? = null
        bmp = if (drawable is BitmapDrawable) {
            (imageView.drawable as BitmapDrawable).bitmap
        } else {
            return null
        }

        // Store image to default external storage directory
        var bmpUri: Uri? = null
        try {

            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            file.parentFile.mkdirs()
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }


    private fun getGoldBookingGain() {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/total_gold_booking_gain.php")
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
                    val status = json.get("status").toString()
                    if (status == "200") {

                        val prdds = json.getJSONObject("Data")

                        gain = prdds.get("gain").toString()
                        // loanVal = prdds.get("loan_amount").toString()

//                    Log.d("Upload Status", "Image Uploaded Successfully !")
                        var activity = getActivity();
                        if (activity != null && isAdded()) {
                            activity.runOnUiThread(Runnable {
                                total_gain.text = resources.getString(R.string.rs) + gain + "/-"
                                gainLayout.visibility = View.VISIBLE
                                loanLayout.visibility = View.GONE
                                certificateLayout.visibility = View.GONE
                            })
                        }
                    }
                }
            }
        })
    }


    private fun getLoanVal() {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_user_loan_eligiblity.php")
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
                    val status = json.get("status").toString()
                    if (status == "200") {

                        val prdds = json.getJSONObject("data")
                        loanVal = prdds.get("loan_amount").toString()


//                    Log.d("Upload Status", "Image Uploaded Successfully !")
                        var activity = getActivity();
                        if (activity != null && isAdded()) {
                            activity.runOnUiThread(Runnable {
                                loanAmt.text = resources.getString(R.string.rs) + loanVal + "/-"
                            })
                        }
                    }

                }
            }
        })
    }


    private fun blink() {
        val handler = Handler()
        Thread {
            val timeToBlink = 4000 //in milissegunds
            try {
                Thread.sleep(timeToBlink.toLong())
            } catch (e: Exception) {
            }
            handler.post {
                if (!TextUtils.isEmpty(gain) && !gain.equals(
                        "null",
                        ignoreCase = true
                    )
                ) {
                    if (!TextUtils.isEmpty(loanVal) && !loanVal.equals(
                            "null",
                            ignoreCase = true
                        )
                    ) {
                        if (gainLayout.getVisibility() == View.VISIBLE) {
                            gainLayout.setVisibility(View.GONE)
                            partnerLayout.setVisibility(View.VISIBLE)
                            loanLayout.setVisibility(View.GONE)
                            certificateLayout.setVisibility(View.GONE)
                        } else if (partnerLayout.getVisibility() == View.VISIBLE) {
                            gainLayout.setVisibility(View.GONE)
                            partnerLayout.setVisibility(View.GONE)
                            loanLayout.setVisibility(View.VISIBLE)
                            certificateLayout.setVisibility(View.GONE)
                        } else if (loanLayout.getVisibility() == View.VISIBLE) {
                            gainLayout.setVisibility(View.GONE)
                            partnerLayout.setVisibility(View.GONE)
                            loanLayout.setVisibility(View.GONE)
                            certificateLayout.setVisibility(View.VISIBLE)

                        } else if (certificateLayout.getVisibility() == View.VISIBLE) {
                            gainLayout.setVisibility(View.VISIBLE)
                            partnerLayout.setVisibility(View.GONE)
                            loanLayout.setVisibility(View.GONE)
                            certificateLayout.setVisibility(View.GONE)
                        }
                    } else {
                        loanLayout.setVisibility(View.GONE)
                        certificateLayout.setVisibility(View.GONE)
                        if (gainLayout.getVisibility() == View.VISIBLE) {
                            gainLayout.setVisibility(View.GONE)
                            partnerLayout.setVisibility(View.VISIBLE)
                            certificateLayout.setVisibility(View.GONE)
                        } else if (partnerLayout.getVisibility() == View.VISIBLE) {
                            gainLayout.setVisibility(View.GONE)
                            partnerLayout.setVisibility(View.GONE)
                            certificateLayout.setVisibility(View.VISIBLE)
                        } else if (certificateLayout.getVisibility() == View.VISIBLE) {
                            gainLayout.setVisibility(View.VISIBLE)
                            partnerLayout.setVisibility(View.GONE)
                            certificateLayout.setVisibility(View.GONE)
                        }
                    }
                } else if (loanVal != null && !TextUtils.isEmpty(loanVal) && !loanVal.equals(
                        "null",
                        ignoreCase = true
                    )
                ) {
                    gainLayout.setVisibility(View.GONE)
                    loanLayout.setVisibility(View.VISIBLE)
                    if (partnerLayout.getVisibility() == View.VISIBLE) {
                        partnerLayout.setVisibility(View.GONE)
                        loanLayout.setVisibility(View.VISIBLE)
                    } else if (loanLayout.getVisibility() == View.VISIBLE) {
                        partnerLayout.setVisibility(View.VISIBLE)
                        loanLayout.setVisibility(View.GONE)
                    }
                } else {
                    loanLayout.setVisibility(View.GONE)
                    partnerLayout.setVisibility(View.VISIBLE)
                }
                blink()
            }
        }.start()
    }

    override fun onDialogOk(resultCode: Int) {
        if (resultCode == 11) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }


}


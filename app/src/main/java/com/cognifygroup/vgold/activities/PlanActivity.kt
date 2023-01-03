package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class PlanActivity : AppCompatActivity(), AlertDialogOkListener {
    var screenshot: Bitmap? = null
    private lateinit var edtgoldWeight: EditText
    private lateinit var btnViewPlan: Button
    private lateinit var lblDate: TextView
    private lateinit var lblQuantity: TextView
    private lateinit var lblTotAmt: TextView
    private lateinit var txtTotAmt: TextView
    private lateinit var lblBookAmt: TextView
    private lateinit var txtBookAmt: TextView
    private lateinit var lblBookChr: TextView
    private lateinit var txtBookChr: TextView
    private lateinit var lblPaidAmt: TextView
    private lateinit var lblEMI: TextView
    private lateinit var lbl12Emi: TextView
    private lateinit var txt12Emi: TextView
    private lateinit var lbl24Emi: TextView
    private lateinit var txt24Emi: TextView
    private lateinit var lbl36Emi: TextView
    private lateinit var txt36Emi: TextView
    private lateinit var lbl48Emi: TextView
    private lateinit var txt48Emi: TextView
    private lateinit var lbl60Emi: TextView
    private lateinit var txt60Emi: TextView
    private lateinit var txt72Emi: TextView
    private lateinit var lbl72Emi: TextView
    private lateinit var txt84Emi: TextView
    private lateinit var lbl84Emi: TextView
    private lateinit var txt96Emi: TextView
    private lateinit var lbl96Emi: TextView
    private lateinit var txt108Emi: TextView
    private lateinit var lbl108Emi: TextView
    private lateinit var txt120Emi: TextView
    private lateinit var lbl120Emi: TextView
    private lateinit var palnLayout: LinearLayout
    lateinit var shareFab: FloatingActionButton
    private var alertDialogOkListener: AlertDialogOkListener = this

    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)
        supportActionBar?.title = "Gold Plan"
        btnViewPlan = findViewById(R.id.btnViewPlan)
        shareFab = findViewById(R.id.shareFab)
        lblDate = findViewById(R.id.lblDate)
        lblQuantity = findViewById(R.id.lblQuantity)
        lbl12Emi = findViewById(R.id.lbl12Emi)
        lbl24Emi = findViewById(R.id.lbl24Emi)
        lbl36Emi = findViewById(R.id.lbl36Emi)
        lbl48Emi = findViewById(R.id.lbl48Emi)
        lbl60Emi = findViewById(R.id.lbl60Emi)
        lbl72Emi = findViewById(R.id.lbl72Emi)
        lbl84Emi = findViewById(R.id.lbl84Emi)
        lbl96Emi = findViewById(R.id.lbl96Emi)
        lbl108Emi = findViewById(R.id.lbl108Emi)
        lbl120Emi = findViewById(R.id.lbl120Emi)
        lblBookAmt = findViewById(R.id.lblBookAmt)
        lblBookChr = findViewById(R.id.lblBookChr)
        lblEMI = findViewById(R.id.lblEMI)
        lblPaidAmt = findViewById(R.id.lblPaidAmt)
        lblTotAmt = findViewById(R.id.lblTotAmt)
        txt12Emi = findViewById(R.id.txt12Emi)
        txt24Emi = findViewById(R.id.txt24Emi)
        txt36Emi = findViewById(R.id.txt36Emi)
        txt48Emi = findViewById(R.id.txt48Emi)
        txt60Emi = findViewById(R.id.txt60Emi)
        txt72Emi = findViewById(R.id.txt72Emi)
        txt84Emi = findViewById(R.id.txt84Emi)
        txt96Emi = findViewById(R.id.txt96Emi)
        txt108Emi = findViewById(R.id.txt108Emi)
        txt120Emi = findViewById(R.id.txt120Emi)
        txtBookAmt = findViewById(R.id.txtBookAmt)
        txtBookChr = findViewById(R.id.txtBookChr)

        txtTotAmt = findViewById(R.id.txtTotAmt)
        edtgoldWeight = findViewById(R.id.edtgoldWeight)
        palnLayout = findViewById(R.id.palnLayout)




        btnViewPlan.setOnClickListener {
            val weight = edtgoldWeight.text.toString()
            if (weight != "null" && !TextUtils.isEmpty(weight)) {
                getPlanDetails(weight)
            }
        }
        shareFab.setOnClickListener {
            palnLayout.visibility = View.VISIBLE
            /* String weight = edtgoldWeight.getText().toString();
                File filePath = new File(Environment.getExternalStorageDirectory()
                        .getPath() + "/" + "VGOLD" + "/plans_" + weight + ".pdf");
//                        .getPath() + "/" + "VGOLD" + "/plans.pdf");

                if (filePath.exists()) {

                    Uri uri = FileProvider.getUriForFile(PlanActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider", filePath);

                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.setPackage("com.whatsapp");

                    startActivity(share);
                }*/
            getScreenShotFromView(palnLayout)

            if (screenshot != null) {
                saveMediaToStorage(screenshot!!)
            }

        }
    }

    fun saveMediaToStorage(bitmap: Bitmap) {
        var imageUri: Uri? = null
        var outputStream: OutputStream? = null
        val filename = System.currentTimeMillis().toString() + ".jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= 29) {
            val contentResolver = this.contentResolver
            if (contentResolver != null) {
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES
                )
                imageUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                if (imageUri != null) {
                    try {
                        outputStream = contentResolver.openOutputStream(imageUri)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                } else {
                    outputStream = null
                }
                fos = outputStream
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            try {
                fos = FileOutputStream(image)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        if (fos != null) {
            val var22 = fos as Closeable
            val it = var22 as OutputStream
            if (outputStream == null) {
                outputStream = it
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            //            Toast.makeText((Context) this, (CharSequence) "Captured View and saved to Gallery", Toast.LENGTH_SHORT).show();
            val whatsappIntent = Intent(Intent.ACTION_SEND)
            whatsappIntent.setPackage("com.whatsapp")
            whatsappIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            whatsappIntent.type = "image/jpeg"
            whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                this.startActivity(whatsappIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getScreenShotFromView(v: LinearLayout) {
        // create a bitmap object
        //  var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot =
                Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot!!)
            v.draw(canvas)
        } catch (e: java.lang.Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        //    return screenshot;
    }


    private fun getPlanDetails(quantity: String) {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("quantity", quantity)
            .build()
        val request = okhttp3.Request.Builder()
            .url("https://www.vgold.co.in/dashboard/webservices/get_gold_plans.php")
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

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val mMessage = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {
//                    Log.d("Upload Status", "Image Uploaded Successfully !")
                    var json = JSONObject(mMessage)
                    var status = json.get("status").toString()
                    var message = json.get("Message").toString()
                    var prdds = json.optJSONObject("Data")
                    runOnUiThread(Runnable {
                        if (status.equals("200")) {
                            shareFab.visibility = View.VISIBLE
                            palnLayout.visibility = View.VISIBLE

                            lblDate.text = prdds?.get("rate_date").toString()
                            lblQuantity.text = ("For " + prdds.get("quantity").toString() + " gms")
                            txtTotAmt.text =
                                resources.getString(R.string.rs) + prdds.get("total_amount")
                                    .toString() + "/-"
                            txtBookAmt.text =
                                resources.getString(R.string.rs) + prdds.get("booking_amount")
                                    .toString() + "/-"
                            txtBookChr.text =
                                resources.getString(R.string.rs) + prdds.get("booking_charge")
                                    .toString() + "/-"
                            lblPaidAmt.text =
                                "You Pay " + resources.getString(R.string.rs) + prdds.get("have_to_pay")
                                    .toString() + "/-"

                            var remAmt = prdds.get("remaing_amount").toString()

                            if (remAmt != "null" && !TextUtils.isEmpty(remAmt)) {
                                val remainAmt: Double =
                                    java.lang.Double.valueOf(remAmt)
                                txt12Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 12)
                                        .toString() + "/-"
                                txt24Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 24)
                                        .toString() + "/-"
                                txt36Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 36)
                                        .toString() + "/-"
                                txt48Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 48)
                                        .toString() + "/-"
                                txt60Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 60)
                                        .toString() + "/-"

                                txt72Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 72)
                                        .toString() + "/-"

                                txt84Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 84)
                                        .toString() + "/-"

                                txt96Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 96)
                                        .toString() + "/-"

                                txt108Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 108)
                                        .toString() + "/-"

                                txt120Emi.text =
                                    resources.getString(R.string.rs) + Math.round(remainAmt / 120)
                                        .toString() + "/-"
                            }
                            //  downloadPDFFile(model.getPdf_url())

                            var pdfurl = prdds.get("pdf_url").toString()
                            if (pdfurl != "null" && !TextUtils.isEmpty(pdfurl))
                                DownloadFileFromURL(pdfurl)


//                            val inputDate: String = model.getRate_date()
//                            if (inputDate != null) {
//                                try {

//                                    val outputFormat = SimpleDateFormat("dd-MMM-yyyy")
//                                    val inputFormat = SimpleDateFormat("dd-MM-yyyy")
//                                    val date = inputFormat.parse(inputDate)
//                                    lblDate.text = outputFormat.format(date)
//                                } catch (e: ParseException) {
//                                    e.printStackTrace()
//                                }
//                            }


                            AlertDialogs().alertDialogOk(
                                this@PlanActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                1,
                                false,
                                alertDialogOkListener
                            )
                        } else {
                            AlertDialogs().alertDialogOk(
                                this@PlanActivity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        }

                    })
                }
            }
        })
    }


    inner class DownloadFileFromURL(var downloadUrl: String) :
        AsyncTask<Void?, Void?, Void?>() {
        var context: Context? = null
        var version: String? = null
        var apkStorage: File? = null
        var outputFile: File? = null
        override fun onPreExecute() {
            super.onPreExecute()
        }


        /*
         override fun doInBackground(vararg arg0: Void): Void? {
            try {
                val NWUrl = URL(downloadUrl) //Create Download URl
                val c = NWUrl.openConnection() as HttpURLConnection //Open Url Connection
                c.requestMethod = "GET" //Set Request Method to "GET" since we are grtting data
                c.connect() //connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e("TAG", "Server returned HTTP " + c.responseCode + " " + c.responseMessage)
                }
                apkStorage = File(
                    Environment.getExternalStorageDirectory()
                        .path + "/" + "VGOLD"
                )
                if (!apkStorage!!.exists()) {
                    apkStorage!!.mkdirs()
                }
                //                outputFile = new File(apkStorage, "plans.pdf");
                val weight: String = edtgoldWeight.getText().toString()
                outputFile = File(apkStorage, "/plans_$weight.pdf")
                if (outputFile!!.exists()) {
                    outputFile!!.delete()
                    outputFile!!.createNewFile()
                    Log.e("TAG", "File Created")
                }
                val fos = FileOutputStream(outputFile) //Get OutputStream for NewFile Location
                val `is` = c.inputStream //Get InputStream for connection
                val buffer = ByteArray(1024) //Set buffer type
                var len1 = 0 //init length
                while (`is`.read(buffer).also { len1 = it } != -1) {
                    fos.write(buffer, 0, len1) //Write new file
                }
                //Close all connection after doing task
                fos.close()
                `is`.close()
            } catch (e: Exception) {
                //Read exception if something went wrong
                e.printStackTrace()
                outputFile = null
                Log.e("TAG", "Download Error Exception " + e.message)
            }
            return null
        }

         */

        @SuppressLint("RestrictedApi")
        override fun onPostExecute(result: Void?) {
            try {
                if (outputFile != null) {
                    shareFab.setVisibility(View.VISIBLE)
                } else {
                    Handler().postDelayed({ }, 3000)
                    Log.e("TAG", "Download Failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                //Change button text if exception occurs
                Handler().postDelayed({ }, 3000)
                Log.e("TAG", "Download Failed with Exception - " + e.localizedMessage)
            }
            super.onPostExecute(result)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                val NWUrl = URL(downloadUrl) //Create Download URl
                val c = NWUrl.openConnection() as HttpURLConnection //Open Url Connection
                c.requestMethod = "GET" //Set Request Method to "GET" since we are grtting data
                c.connect() //connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e("TAG", "Server returned HTTP " + c.responseCode + " " + c.responseMessage)
                }
                apkStorage = File(
                    Environment.getExternalStorageDirectory()
                        .path + "/" + "VGOLD"
                )
                if (!apkStorage!!.exists()) {
                    apkStorage!!.mkdirs()
                }
                //                outputFile = new File(apkStorage, "plans.pdf");
                val weight: String = edtgoldWeight.getText().toString()
                outputFile = File(apkStorage, "/plans_$weight.pdf")
                if (outputFile!!.exists()) {
                    outputFile!!.delete()
                    outputFile!!.createNewFile()
                    Log.e("TAG", "File Created")
                }
                val fos = FileOutputStream(outputFile) //Get OutputStream for NewFile Location
                val `is` = c.inputStream //Get InputStream for connection
                val buffer = ByteArray(1024) //Set buffer type
                var len1 = 0 //init length
                while (`is`.read(buffer).also { len1 = it } != -1) {
                    fos.write(buffer, 0, len1) //Write new file
                }
                //Close all connection after doing task
                fos.close()
                `is`.close()
            } catch (e: Exception) {
                //Read exception if something went wrong
                e.printStackTrace()
                outputFile = null
                Log.e("TAG", "Download Error Exception " + e.message)
            }
            return null
        }
    }

    override fun onDialogOk(resultCode: Int) {

    }
}

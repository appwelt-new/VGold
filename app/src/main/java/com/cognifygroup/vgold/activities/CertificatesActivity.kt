package com.cognifygroup.vgold.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.cognifygroup.vgold.BuildConfig
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.utilities.Constants
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors


class CertificatesActivity : AppCompatActivity() {
    private var userId = ""
    private lateinit var iv_certificate: ImageView
    private lateinit var downloadbtn: Button
    private lateinit var sharebtn: Button
    private lateinit var sharedPreferences: SharedPreferences
    private var permission = 0

    //    private  val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
//       permission = if(it){
//           1
//       }
//        else{
//            0
//       }
//    }
    private var get_cerURL = "";

    private var downl_cerURL = "";


    var lastEvent: FloatArray? = null
    var d = 0f
    var newRot = 0f
    private var isZoomAndRotate = false
    private var isOutSide = false
    private val NONE = 0
    private val DRAG = 1
    private val ZOOM = 2
    private var mode = NONE
    private val start = PointF()
    private val mid = PointF()
    var oldDist = 1f
    private var xCoOrdinate = 0f
    private var yCoOrdinate: kotlin.Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificates)
        sharedPreferences =
            this.getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()

        iv_certificate = findViewById(R.id.iv_certificate)
        downloadbtn = findViewById(R.id.downloadbtn)
        sharebtn = findViewById(R.id.sharebtn);
        supportActionBar!!.title = "Certificates"

        get_cerURL =
            "https://vgold.co.in/dashboard/vgold_accnt_certificate/files/" + userId + ".png";
        downl_cerURL =
            "https://vgold.co.in/dashboard/vgold_accnt_certificate/download_certificate.php?file=" + userId + ".png";
        //getCerDetail()
        getcerimg()


        //Picasso.with(this).load(get_cerURL).into(iv_certificate)
//
//
//        downloadbtn.setOnClickListener {
//
////            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
////            if (permission==1) {
//                dowload()
////            }
////            else{
////                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
////            }
//        }
//    }
        var mImage: Bitmap?

        // Declaring a webpath as a string
        val mWebPath =
            "https://vgold.co.in/dashboard/vgold_accnt_certificate/download_certificate.php?file=" + userId + ".png";
        // Declaring and initializing an Executor and a Handler
        val myExecutor = Executors.newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())

        // When Button is clicked, executor will
        // fetch the image and handler will display it.
        // Once displayed, it is stored locally
//        downloadbtn.setOnClickListener {
//            myExecutor.execute {
//                mImage = mLoad(mWebPath)
//                myHandler.post {
//                    iv_certificate.setImageBitmap(mImage)
//                    if (mImage != null) {
//                        mSaveMediaToStorage(mImage)
//                    }
//                }
//            }
//        }

        myExecutor.execute {
            mImage = mLoad(mWebPath)
            myHandler.post {
                iv_certificate.setImageBitmap(mImage)
                if (mImage != null) {
                    mSaveMediaToStorage(mImage)
                }
            }
        }

        sharebtn.setOnClickListener {
            val bmpUri: Uri? = getLocalBitmapUri(iv_certificate)
            if (bmpUri != null) {
                // Construct a ShareIntent with link to image

                // Construct a ShareIntent with link to image
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                shareIntent.type = "image/*"
                // Launch sharing dialog for image
                // Launch sharing dialog for image
                startActivity(Intent.createChooser(shareIntent, "Share Image"))

            }
        }


        //  scaleGestureDetector = ScaleGestureDetector(this@CertificatesActivity, ScaleListener())


        iv_certificate.setOnTouchListener(OnTouchListener { v, event ->
            val view = v as ImageView
            view.bringToFront()
            viewTransformation(view, event)
            true
        })
    }

    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            //Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }

    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    // Function to save image on the device.
    // Refer: https://www.geeksforgeeks.org/circular-crop-an-image-and-save-it-to-the-file-in-android/
    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }


    fun getcerimg() {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .build()

        val request = Request.Builder()
            // .header("Authorization", "Client-ID $IMGUR_CLIENT_ID")
            .url("https://www.vgold.co.in/dashboard/vgold_accnt_certificate/get_certificate.php?userid=" + userId)
            //.post(requestBody)
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                } else {
                    val str = response.body()!!.string();
                    val json = JSONObject(str)
                    val status = json.get("status").toString()
                    val message = json.get("Message").toString()
                    val imgPath = json.get("image_path").toString()
                    val downloadPath = json.get("download_image_path").toString()

                    runOnUiThread(Runnable {

                        Picasso.with(this@CertificatesActivity).load(imgPath).into(iv_certificate)
//                        Toast.makeText(
//                            this@CertificatesActivity,
//                            "Connection failed",
//                            Toast.LENGTH_LONG
//                        ).show()
                    })
                }
            }
        })
    }
    /*  client.newCall(request).execute().use { response ->
          if (!response.isSuccessful) throw IOException("Unexpected code $response")

          else{
              println(response.body()!!.string())
              val json = JSONObject(response.body()!!.string())
              val status = json.get("status").toString()
              val message = json.get("Message").toString()
              val imgPath = json.get("image_path").toString()
              val downloadPath = json.get("download_image_path").toString()

              Picasso.with(this@CertificatesActivity).load(imgPath).into(iv_certificate)

          }

      }*/

    /*
    private fun getCerDetail() {
        val client = OkHttpClient().newBuilder().build()
        val requestBody: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("user_id",VGoldApp.onGetUerId())
            .build()

        val request = okhttp3.Request.Builder()
            .url("https://vgold.co.in/dashboard/vgold_accnt_certificate/get_certificate.php?")
            //.header("AUTHORIZATION", "Bearer $token")
            .header("Accept", "application/json, charset=utf-8")
            .header("Content-Type", "application/json,charset=utf-8")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                e.printStackTrace()
                Log.e("failure Response", mMessage)
            }
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val message1 = response.body()!!.string()
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code" + response)
                } else {


                    try {

                        val json = JSONObject(message1)
                        val status = json.get("status").toString()
                        val message = json.get("Message").toString()
                        val imgPath = json.get("image_path").toString()
                        val downloadPath = json.get("download_image_path").toString()

                        Picasso.with(this@CertificatesActivity).load(imgPath).into(iv_certificate)

                    }catch (e: JSONException){
                        runOnUiThread(Runnable {
                        Toast.makeText(this@CertificatesActivity, "Error -${e.message}", Toast.LENGTH_LONG).show()
                    })
                    }
                }
            }
        })
    }*/

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
                applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            file.parentFile.mkdirs()
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(
                applicationContext,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }


    private fun viewTransformation(view: View, event: MotionEvent) {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                xCoOrdinate = view.getX() - event.rawX
                yCoOrdinate = view.getY() - event.rawY
                start[event.x] = event.y
                isOutSide = false
                mode = DRAG
                lastEvent = null
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    midPoint(mid, event)
                    mode = ZOOM
                }
                lastEvent = FloatArray(4)
                lastEvent!![0] = event.getX(0)
                lastEvent!![1] = event.getX(1)
                lastEvent!![2] = event.getY(0)
                lastEvent!![3] = event.getY(1)
                d = rotation(event)
            }
            MotionEvent.ACTION_UP -> {
                isZoomAndRotate = false
                if (mode === DRAG) {
                    val x = event.x
                    val y = event.y
                }
                isOutSide = true
                mode = NONE
                lastEvent = null
                mode = NONE
                lastEvent = null
            }
            MotionEvent.ACTION_OUTSIDE -> {
                isOutSide = true
                mode = NONE
                lastEvent = null
                mode = NONE
                lastEvent = null
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
                lastEvent = null
            }
            MotionEvent.ACTION_MOVE -> if (!isOutSide) {
                if (mode === DRAG) {
                    isZoomAndRotate = false
                    view.animate().x(event.rawX + xCoOrdinate).y(event.rawY + yCoOrdinate)
                        .setDuration(0).start()
                }
                if (mode === ZOOM && event.pointerCount == 2) {
                    val newDist1 = spacing(event)
                    if (newDist1 > 10f) {
                        val scale: Float = newDist1 / oldDist * view.getScaleX()
                        view.setScaleX(scale)
                        view.setScaleY(scale)
                    }
                    if (lastEvent != null) {
                        newRot = rotation(event)
                        view.setRotation((view.getRotation() + (newRot - d)) as Float)
                    }
                }
            }
        }
    }

    private fun rotation(event: MotionEvent): Float {
       /* val delta_x = (event.getX(0) - event.getX(1)).toDouble()
        val delta_y = (event.getY(0) - event.getY(1)).toDouble()
        val radians = Math.atan2(delta_y, delta_x)
        return Math.toDegrees(radians).toFloat()*/
        return 1.0f
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toInt().toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }

}



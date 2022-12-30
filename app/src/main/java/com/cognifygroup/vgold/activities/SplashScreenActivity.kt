package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.utilities.AskPermissions

class SplashScreenActivity : AppCompatActivity() {
    private var askPermissions: AskPermissions? = null
    private lateinit var vLogo: ImageView
    private lateinit var SharedPreferences: SharedPreferences
    private lateinit var sp_version: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        vLogo = findViewById(R.id.vLogoIv)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        sp_version = findViewById(R.id.sp_version)


        try {
            val pInfo: PackageInfo =
                VGoldApp.context!!.getPackageManager()
                    .getPackageInfo(
                        VGoldApp.context!!.getPackageName(),
                        0
                    )
            val version = pInfo.versionName
            sp_version.text=version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askPermissions = AskPermissions(this@SplashScreenActivity)
            if (askPermissions!!.checkAndRequestPermissions()) {
                notificationPayload()
            }
        } else {
            notificationPayload()
        }


        /* ImageView click =findViewById(R.id.click);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationPayload();
            }
        });*/
    }

    private fun notificationPayload() {
        if (intent.extras != null) {
            val payload = intent.extras!!["payload"].toString()
            if (payload != null && !TextUtils.isEmpty(payload) && !payload.equals(
                    "null",
                    ignoreCase = true
                )
            ) {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(payload)))
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(payload)))
                }
                finish()
            } else {
                Handler().postDelayed({
                    try {
                        Thread.sleep(2000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        val lIntent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                        startActivity(lIntent)
                        finish()
                    }
                }, 2000)
            }
        } else {
            Handler().postDelayed({
                try {
                    Thread.sleep(2000)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    val lIntent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(lIntent)
                    finish()
                }
            }, 2000)
        }
    }

    override fun onResume() {
        super.onResume()

//        notificationPayload();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    Intent lIntent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(lIntent);
//                    finish();
//                }
//            }
//        }, 2000);
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults!!)
        askPermissions!!.onRequestPermissionsResult(
            requestCode,
            permissions as Array<String>, grantResults
        )
    }

}
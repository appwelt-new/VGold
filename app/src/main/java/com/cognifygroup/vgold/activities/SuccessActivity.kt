package com.cognifygroup.vgold.activities
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.cognifygroup.vgold.R
import com.squareup.picasso.Picasso



class SuccessActivity : AppCompatActivity() {


    private lateinit var imgView: ImageView
    private lateinit var txtb: TextView
    private lateinit var btnOk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        imgView = findViewById(R.id.ImageView)
        btnOk = findViewById(R.id.btnOk)
        txtb = findViewById(R.id.txtb)
        supportActionBar?.title = "Success  "
//        Glide.with(this)
//           imgView .load("https://thumbs.gfycat.com/CourteousPhonyHorsemouse-size_restricted.gif")
//            .into(ImageView)

        Picasso.with(this).load("https://www.thumbs.gfycat.com/CourteousPhonyHorsemouse-size_restricted.gif")
            .into(imgView)


        if (intent.hasExtra("message")) {
            val message = intent.getStringExtra("message")
            txtb.text = message
            txtb.textSize = 12f
        }

btnOk.setOnClickListener {

    val intent= Intent(this,MainActivity ::class.java)
    startActivity(intent)
}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClickOfBtnok() {
        val intent = Intent(this@SuccessActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@SuccessActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
    }

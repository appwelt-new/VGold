package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.cognifygroup.vgold.R
import com.squareup.picasso.Picasso


class Success1 : AppCompatActivity() {
    private lateinit var imgView: ImageView
    private lateinit var txtb: TextView
    private lateinit var btnOk: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success1)

        imgView = findViewById(R.id.imgview)
        btnOk = findViewById(R.id.btnk)
        if (getIntent() != null){
            supportActionBar?.title = getIntent().getStringExtra("title").toString()

        }


//        Glide.with(this)
//           imgView .load("https://thumbs.gfycat.com/CourteousPhonyHorsemouse-size_restricted.gif")
//            .into(ImageView)

        Picasso.with(this).load("https://thumbs.gfycat.com/CourteousPhonyHorsemouse-size_restricted.gif")
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
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

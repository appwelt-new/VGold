package com.cognifygroup.vgold.utilities

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.cognifygroup.vgold.R


class TransparentProgressDialog(contextT: Context?) :
    Dialog(contextT!!, R.style.TransparentProgressDialog) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.view_custom_progress_layout)

        val iv = findViewById<ImageView>(R.id.progress)
        val rotation = AnimationUtils.loadAnimation(context, R.anim.rotate)
        rotation.repeatCount = Animation.INFINITE
        iv.startAnimation(rotation)

    }
}

package com.cognifygroup.vgold.model

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.cognifygroup.vgold.R

class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?)
        : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when (currentView.id){
                R.id.et_otp_var1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.et_otp_var2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.et_otp_var3 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.et_otp_var4 -> if (text.length == 1) nextView!!.requestFocus()

            }
        }


}
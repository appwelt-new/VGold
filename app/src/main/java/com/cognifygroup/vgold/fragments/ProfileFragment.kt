
package com.cognifygroup.vgold.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.UpdateProfileActivity
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.Constants.Companion.VGOLD_DB
import java.io.File

class ProfileFragment : Fragment() {
  //  private lateinit var profileImg: CircleImageView
    private var encodedImage = ""
    private var prfemailsh = ""
    private var prfmobnosh = ""
    private var prfaddrsh = ""
    private var prfcitysh = ""
    private var prfstatesh = ""
  private  lateinit var imgbarcode: ImageView
    private var encFile = ""
    private var coDFile: File? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var edtName: EditText
    private lateinit var edtCRnn: EditText
    private lateinit var edtPanNo: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtphNo: EditText
    private lateinit var edtAddres: EditText

    private lateinit var updateBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = requireContext().getSharedPreferences(VGOLD_DB, Context.MODE_PRIVATE)
        val v: View = inflater.inflate(R.layout.fragment_profile, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Profile"
        imgbarcode = v.findViewById(R.id.profileImage)

        edtName = v.findViewById(R.id.edtName)
//        candiFnm.isEnabled = false

        edtCRnn = v.findViewById(R.id.edtCRN)
//        candiEml.isEnabled = false
        edtPanNo = v.findViewById(R.id.edtPan)
//        candiMb.isEnabled = false
        edtphNo = v.findViewById(R.id.edtPhone)
//        candiDt.isEnabled = false
        edtEmail = v.findViewById(R.id.edtMail)

        edtAddres = v.findViewById(R.id.edtAddress)
//        candiGen.isEnabled = false
        updateBtn = v.findViewById(R.id.btnUpdateProfile)

        prfemailsh = sharedPreferences.getString(Constants.prfemail, null).toString()
        prfmobnosh = sharedPreferences.getString(Constants.prfmobno, null).toString()
        prfaddrsh = sharedPreferences.getString(Constants.prfaddr, null).toString()



        if (!prfemailsh.equals("null"))
            edtEmail.setText(prfemailsh)

        if (!prfmobnosh.equals("null"))
            edtphNo.setText(prfmobnosh)

        if (!prfaddrsh.equals("null"))
            edtAddres.setText(prfaddrsh)



        updateBtn.setOnClickListener{
            val intent= Intent(requireContext(), UpdateProfileActivity::class.java)
            startActivity(intent)
        }


    return  v
    }

    override fun onResume() {
        prfemailsh = sharedPreferences.getString(Constants.prfemail, null).toString()
        prfmobnosh = sharedPreferences.getString(Constants.prfmobno, null).toString()
        prfaddrsh = sharedPreferences.getString(Constants.prfaddr, null).toString()

        if (!prfemailsh.equals("null"))
            edtEmail.setText(prfemailsh)

        if (!prfmobnosh.equals("null"))
            edtphNo.setText(prfmobnosh)

        if (!prfaddrsh.equals("null"))
            edtAddres.setText(prfaddrsh)

        super.onResume()

    }
}
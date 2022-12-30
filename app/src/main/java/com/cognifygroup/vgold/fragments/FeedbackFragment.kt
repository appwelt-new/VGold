package com.cognifygroup.vgold.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.fragment.app.Fragment
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.MainActivity


class FeedbackFragment : Fragment() {

    private var userId = ""
    private lateinit var edt_comment : EditText
    private lateinit var btnSubmitFeedback: Button
    private lateinit var ratingBar: AppCompatRatingBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "Feedback"
        val v: View = inflater.inflate(R.layout.fragment_feedback, container, false)

        edt_comment = v.findViewById(R.id.edt_comment)
        btnSubmitFeedback = v.findViewById(R.id.btnSubmitFeedback)
        ratingBar = v.findViewById(R.id.ratingBar)


        btnSubmitFeedback.setOnClickListener {
            if (edt_comment.text.isEmpty()) {
                edt_comment.error = "Please Enter the   Complaint !"
                edt_comment.requestFocus()

            } else {
                Log.d("value is ", ratingBar.rating.toString())
                Toast.makeText(
                    requireContext(),
                    " Thank you for the valuable feedback.",
                    Toast.LENGTH_LONG
                ).show()
//                AttemptToAddFeedback(
//                    VGoldApp.onGetUerId(),
//                    edt_comment.text.toString(),
//                   ratingBar.rating.toString()
//                )
                val intent = Intent(requireContext(), MainActivity::class.java)
                // intent.putExtra("email_id", edtEmail.text.toString())
                startActivity(intent)
            }
        }
        return v
    }


}
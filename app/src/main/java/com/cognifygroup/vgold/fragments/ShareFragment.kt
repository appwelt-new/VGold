package com.cognifygroup.vgold.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.cognifygroup.vgold.R


class ShareFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Share"
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share, container, false)
    }


}
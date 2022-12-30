package com.cognifygroup.vgold.utilities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cognifygroup.vgold.activities.LoginActivity

class AskPermissions(var mContext: Context) {
    var mActivity: Activity

    init {
        mActivity = mContext as Activity
    }

    private fun checkToLaunch() {
        val lIntent = Intent(mActivity, LoginActivity::class.java)
        mActivity.startActivity(lIntent)
        mActivity.finish()
        //        openActivity(mActivity, IntroActivity.class);
    }

    fun checkAndRequestPermissions(): Boolean {
        val read_external_storage =
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        val write_external_storage =
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val camera = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
        //        int receive_sms = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECEIVE_SMS);
//        int read_sms = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS);
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (read_external_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (write_external_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        //        if (receive_sms != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
//        }
//        if (read_sms != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
//        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity, listPermissionsNeeded.toTypedArray(), 0)
            return false
        }
        return true
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> {
                val perms: MutableMap<String, Int> = HashMap()
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                //                perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                if (grantResults.size > 0) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    if (perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED //                            && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                    //                            && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        checkToLaunch()
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                mActivity,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                mActivity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                mActivity,
                                Manifest.permission.CAMERA
                            ) //                               || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.RECEIVE_SMS) ||
                        //                                ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_SMS)
                        ) {
                            val alertDialogBuilder = AlertDialog.Builder(mContext)
                            alertDialogBuilder.setMessage("All Permission required for mContext app")
                            alertDialogBuilder.setPositiveButton(
                                "OK"
                            ) { dialog, which ->
                                checkAndRequestPermissions()
                                dialog.dismiss()
                            }
                            alertDialogBuilder.setNegativeButton(
                                "Cancel"
                            ) { dialog, which -> mActivity.finish() }
                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.show()
                          //  break
                        } else {
                            Toast.makeText(
                                mContext,
                                "Go to settings and enable permissions",
                                Toast.LENGTH_LONG
                            ).show()
                            mActivity.finish()
                        }
                    }
                }
            }
        }
    }
}

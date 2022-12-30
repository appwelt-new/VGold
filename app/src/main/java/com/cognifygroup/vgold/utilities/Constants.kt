package com.cognifygroup.vgold.utilities

import android.app.Application

class Constants : Application() {

    companion object{
        const val LOGIN_API = "https://www.vgold.co.in/dashboard/webservices/login.php"
        const val OTP_VERIFICATION = "https://www.vgold.co.in/dashboard/webservices/login.php"
        const val SERVER_URL = "https://www.vgold.co.in/dashboard/webservices/"



        //Shared Pref


        const val VGOLD_DB = "VgoldDb"
        const val REMEMBER: String = "CHECKBOX"
        const val USERNAME: String = "userName"
        const val  fName: String  ="First_Name"
        const val VUSER_ID = "vusrId"
        const val prfemail = "email"
        const val prfmobno = "mobno"
        const val prfuname = "uname"
        const val qrPath = "qrpath"
        const val prfaddr = "addrs"
        const val prfcity = "city"
        const val prfstate = "state"
        const val SH_APP_CD ="ShAppCode"
    }

}
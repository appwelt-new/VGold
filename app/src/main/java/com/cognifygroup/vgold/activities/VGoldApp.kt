package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import com.cognifygroup.vgold.CPModule.CustomViewPager
import com.cognifygroup.vgold.utilities.Constants.Companion.VGOLD_DB
import com.cognifygroup.vgold.utilities.Constants.Companion.VUSER_ID
import com.cognifygroup.vgold.utilities.Constants.Companion.fName
import com.cognifygroup.vgold.utilities.Constants.Companion.prfaddr
import com.cognifygroup.vgold.utilities.Constants.Companion.prfemail
import com.cognifygroup.vgold.utilities.Constants.Companion.prfmobno
import com.cognifygroup.vgold.utilities.Constants.Companion.qrPath
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.download.BaseImageDownloader


class VGoldApp : MultiDexApplication() {
    private var mImageLoader: ImageLoader? = null
     val mContext: Context? = null
    var viewPagerr
    : CustomViewPager? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        // The following line triggers the initialization of ACRA
        // ACRA.init(this);
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        mSharedPreferences = context?.getSharedPreferences(VGOLD_DB,
            Context.MODE_PRIVATE)
        val config = ImageLoaderConfiguration.Builder(this)
            .threadPriority(Thread.NORM_PRIORITY - 2) // default
            .tasksProcessingOrder(QueueProcessingType.FIFO) // default
            .denyCacheImageMultipleSizesInMemory()
            .memoryCache(LruMemoryCache(2 * 1024 * 1024))
            .memoryCacheSize(2 * 1024 * 1024)
            .memoryCacheSizePercentage(13) // default
            .diskCacheSize(50 * 1024 * 1024)
            .diskCacheFileCount(100)
            .diskCacheFileNameGenerator(HashCodeFileNameGenerator()) // default
            .imageDownloader(BaseImageDownloader(this)) // default
            .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
            .writeDebugLogs()
            .memoryCache(WeakMemoryCache())
            .build()
        mImageLoader = ImageLoader.getInstance()
        mImageLoader!!.init(config)
    }

    companion object {
        var viewPagerr: CustomViewPager? = null
        private var mImageLoader: ImageLoader? = null
        var context: Context? = null
//            private set
var mSharedPreferences: SharedPreferences? = null
       // var viewPagerr: CustomViewPager? = null
        fun getmImageLoader(): ImageLoader? {
            return mImageLoader
        }

        fun onSetUserDetails(
            uid: String?, first: String?, last: String?,
            email: String?, no: String?,
            qr: String?, pan_no: String?,
            address: String?, city: String?,
            state: String?, userimg: String?,
            isCP: Int?,
            identityImg: String?,
            addressImg1: String?,
            addressImg2: String?,
            aadharNo: String?
        ) {
            val editor = mSharedPreferences!!.edit()
            editor.putString(VUSER_ID, uid)
            editor.putString(fName, first)
            editor.putString("LAST", last)
            editor.putString(prfemail, email)
            editor.putString(prfmobno, no)
            editor.putString(qrPath, qr)
            editor.putString("PAN_NO", pan_no)
            editor.putString("ADDRESS", address)
            editor.putString("CITY", city)
            editor.putString("STATE", state)
            editor.putString("USERIMG", userimg)
            editor.putInt("ISCP", isCP!!)
            editor.putString("IDENTITY_IMG", identityImg)
            editor.putString("ADDR_IMG1", addressImg1)
            editor.putString("ADDR_IMG2", addressImg2)
            editor.putString("ADHARNO", aadharNo)
            editor.apply()
        }

        fun onSetUserRole(user_role: String?, validity: String?) {
            val editor1 = mSharedPreferences!!.edit()
            editor1.putString("USERROLE", user_role)
            editor1.putString("VAILDITY", validity)
            editor1.apply()
        }

        fun onSetChannelPartner(totUser: String?, totGold: String?, totCommission: String?) {
            val editor1 = mSharedPreferences!!.edit()
            editor1.putString("USERS_TOT", totUser)
            editor1.putString("GOLD_TOT", totGold)
            editor1.putString("COMMISSION_TOT", totCommission)
            editor1.commit()
        }

        fun onGetUserTot(): String? {
            return mSharedPreferences!!.getString("USERS_TOT", "")
        }

        fun onGetGoldTot(): String? {
            return mSharedPreferences!!.getString("GOLD_TOT", "")
        }

        fun onGetCommissionTot(): String? {
            return mSharedPreferences!!.getString("COMMISSION_TOT", "")
        }

        fun onSetVersionCode(version_code: String?) {
            val editor2 = mSharedPreferences!!.edit()
            editor2.putString("version_code", version_code)
            editor2.commit()
        }

        fun onGetVersionCode(): String? {
            return mSharedPreferences!!.getString("version_code", "")
        }

        fun onGetValidity(): String? {
            return mSharedPreferences!!.getString("VAILDITY", "")
        }

        fun onGetUserRole(): String? {
            return mSharedPreferences!!.getString("USERROLE", "")
        }

        fun onGetFirst(): String? {
            return mSharedPreferences!!.getString(fName, "")
        }

        fun onGetLast(): String? {
            return mSharedPreferences!!.getString("LAST", "")
        }

        fun onGetNo(): String? {
            return mSharedPreferences!!.getString(prfmobno, "")
        }

        fun onGetUerId(): String? {
            return mSharedPreferences!!.getString(VUSER_ID, "")
        }

        fun onGetEmail(): String? {
            return mSharedPreferences!!.getString(prfemail, "")
        }

        fun onGetQrCode(): String? {
            return mSharedPreferences!!.getString(qrPath, "")
        }

        fun onGetPanNo(): String? {
            return mSharedPreferences!!.getString("PAN_NO", "")
        }

        fun onGetAddress(): String? {
            return mSharedPreferences!!.getString(prfaddr, "")
        }

        fun onGetCity(): String? {
            return mSharedPreferences!!.getString("CITY", "")
        }

        fun onGetState(): String? {
            return mSharedPreferences!!.getString("STATE", "")
        }

        fun onGetUserImg(): String? {
            return mSharedPreferences!!.getString("USERIMG", "")
        }

        fun onGetIsCP(): Int {
            return mSharedPreferences!!.getInt("ISCP", 0)
        }

        fun onGetAadharNo(): String? {
            return mSharedPreferences!!.getString("ADHARNO", "")
        }

        fun onGetIdentityImg(): String? {
            return mSharedPreferences!!.getString("IDENTITY_IMG", "")
        }

        fun onGetAddressImg1(): String? {
            return mSharedPreferences!!.getString("ADDR_IMG1", "")
        }

        fun onGetAddressImg2(): String? {
            return mSharedPreferences!!.getString("ADDR_IMG2", "")
        }
    }


}

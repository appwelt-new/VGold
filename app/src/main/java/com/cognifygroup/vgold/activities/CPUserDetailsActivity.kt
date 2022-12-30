package com.cognifygroup.vgold.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.cognifygroup.vgold.CPModule.CPCommissionFragment
import com.cognifygroup.vgold.CPModule.CPEMIFragment
import com.cognifygroup.vgold.CPModule.CPGoldBookingFragment
import com.cognifygroup.vgold.CPModule.CustomViewPager
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.adapters.ViewPagerAdapter
import com.cognifygroup.vgold.addcomplain.AddComplainServiceProvider
import com.cognifygroup.vgold.channelpartner.UserDetailsModel
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.LoginSessionModel
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView

class CPUserDetailsActivity : AppCompatActivity(),AlertDialogOkListener {

    var tabLayout: TabLayout? = null
    var viewPager: CustomViewPager? = null
    var txtName: TextView? = null
    var txtMobNo: TextView? = null

    var txtEmail: TextView? = null
    var txtCommission: TextView? = null
    var userImg: CircleImageView? = null

    var addComplainServiceProvider: AddComplainServiceProvider? = null
    var mAlert: AlertDialogs? = null
    private var progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    private val userDetailsList: ArrayList<UserDetailsModel>? = null
    private val tabIcons = intArrayOf(
        R.drawable.gold_coin_progress,
        R.drawable.emi,
        R.drawable.commission
    )
    private var userDetailsModel: UserDetailsModel.Data? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpuser_details)


        tabLayout = findViewById(R.id.tabLayout)
        userImg = findViewById(R.id.userImg)
        txtEmail = findViewById(R.id.txtEmail)
        txtCommission = findViewById(R.id.txtCommission)
        txtMobNo = findViewById(R.id.txtMobNo)
        txtName = findViewById(R.id.txtName)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout = findViewById(R.id.tabLayout)
        tabLayout = findViewById(R.id.tabLayout)



        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getIntentDate()
        init()
    }

    private fun getIntentDate() {
        try {
            userDetailsModel =
                intent.getSerializableExtra("userDetailsModel") as UserDetailsModel.Data?
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        progressDialog = TransparentProgressDialog(this@CPUserDetailsActivity)
        progressDialog!!.setCancelable(false)
        setFinishOnTouchOutside(false)
        mAlert = AlertDialogs().getInstance()
        addComplainServiceProvider = AddComplainServiceProvider(this)
        loginStatusServiceProvider = LoginStatusServiceProvider(this)
        checkLoginSession()
        setTabViews()
        setUserDetails()
    }

    private fun setUserDetails() {
        if (userDetailsModel != null) {
            txtName!!.setText(userDetailsModel!!.uname)
            txtMobNo!!.setText(userDetailsModel!!.umobile)
            txtEmail!!.setText(userDetailsModel!!.uemail)
            txtCommission!!.text =
                "Commission Earned : " + resources.getString(R.string.rs) + userDetailsModel!!.total_commission_created
            userImg?.let {
                Glide.with(this).load(userDetailsModel!!.upic)
                    .placeholder(R.drawable.black_user)
                    .error(R.drawable.black_user)
                    .into(it)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTabViews() {
        VGoldApp.viewPagerr = viewPager
        VGoldApp.viewPagerr?.setOffscreenPageLimit(1)
        VGoldApp.viewPagerr?.let { setupViewPager(it) }
        tabLayout!!.setupWithViewPager(VGoldApp.viewPagerr)
        VGoldApp.viewPagerr?.setPagingEnabled(false)
        setupTabIcons()

//        setupTabIcons();
        VGoldApp.viewPagerr?.setCurrentItem(0)
        VGoldApp.viewPagerr?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                VGoldApp.viewPagerr!!.setCurrentItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setupTabIcons() {
        tabLayout!!.getTabAt(0)!!.setIcon(tabIcons[0])
        tabLayout!!.getTabAt(1)!!.setIcon(tabIcons[1])
        tabLayout!!.getTabAt(2)!!.setIcon(tabIcons[2])
    }


    private fun setupViewPager(viewPager: CustomViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        val fragment1 = userDetailsModel?.uid?.let { CPGoldBookingFragment(it) }
        val fragment2 = userDetailsModel?.uid?.let { CPEMIFragment(it) }
        val fragment3 = userDetailsModel?.uid?.let {
            com.cognifygroup.vgold.CPModule.CPCommissionFragment(
                it
            )
        }
        if (fragment1 != null) {
            adapter.addFragment(fragment1, "Gold Booking")
        }
        if (fragment2 != null) {
            adapter.addFragment(fragment2, "EMI")
        }
        if (fragment3 != null) {
            adapter.addFragment(fragment3, "Commission")
        }
        viewPager.setAdapter(adapter)
    }

    private fun checkLoginSession() {
        loginStatusServiceProvider?.getLoginStatus(VGoldApp.onGetUerId(), object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    progressDialog?.hide()
                    val status: String? = (serviceResponse as LoginSessionModel).getStatus()
                    val message: String? = (serviceResponse as LoginSessionModel).getMessage()
                    val data: Boolean = (serviceResponse as LoginSessionModel).getData() == true
                    Log.i("TAG", "onSuccess: $status")
                    Log.i("TAG", "onSuccess: $message")
                    if (status == "200") {
                        if (!data) {
                            AlertDialogs().alertDialogOk(
                                this@CPUserDetailsActivity,
                                "Alert",
                                "$message,  Please relogin to app",
                                resources.getString(R.string.btn_ok),
                                11,
                                false,
                                alertDialogOkListener
                            )
                        }
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@CPUserDetailsActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )
                        //                        mAlert.onShowToastNotification(AddGoldActivity.this, message);
                    }
                } catch (e: Exception) {
                    //  progressDialog.hide();
                    e.printStackTrace()
                } finally {
                    //  progressDialog.hide();
                }
            }

            override fun <T> onFailure(apiErrorModel: T, extras: T) {

                try {
                    progressDialog?.hide()
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@CPUserDetailsActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@CPUserDetailsActivity)
                    }
                } catch (e: Exception) {
                    progressDialog?.hide()
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@CPUserDetailsActivity)
                } finally {
                    progressDialog?.hide()
                }
            }
        })
    }

    override fun onDialogOk(resultCode: Int) {
        when (resultCode) {
            11 -> {
                val LogIntent = Intent(this@CPUserDetailsActivity, LoginActivity::class.java)
                startActivity(LogIntent)
                finish()
            }
        }
    }
}

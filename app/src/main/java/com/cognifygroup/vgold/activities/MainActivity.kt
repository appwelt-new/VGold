package com.cognifygroup.vgold.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.fragments.*
import com.cognifygroup.vgold.getrefercode.ReferModel
import com.cognifygroup.vgold.getrefercode.ReferServiceProvider
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.loan.LoanServiceProvider
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.model.GetTodayGoldRateServiceProvider
import com.cognifygroup.vgold.model.LoginStatusServiceProvider
import com.cognifygroup.vgold.utilities.Constants
import com.cognifygroup.vgold.utilities.Constants.Companion.SH_APP_CD
import com.cognifygroup.vgold.utilities.Constants.Companion.fName
import com.cognifygroup.vgold.utilities.TransparentProgressDialog
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks


class MainActivity : AppCompatActivity(), AlertDialogOkListener {
    private lateinit var sharedPreferences: SharedPreferences
    private var firstName = ""
    private lateinit var navView: NavigationView
    lateinit var drawerLayout: DrawerLayout

    private lateinit var toggle: ActionBarDrawerToggle


    var mAlert: AlertDialogs? = null

    //   private val vendorOfferAdapter: VendorOfferAdapter? = null
    //  private val vendorOfferServiceProvider: VendorOfferServiceProvider? = null
    private val referServiceProvider: ReferServiceProvider? = null
    var updatedversioncode = 1.0
    private var dynamicApCd = ""
    private val progressDialog: TransparentProgressDialog? = null
    private val alertDialogOkListener: AlertDialogOkListener = this

    var getTodayGoldRateServiceProvider: GetTodayGoldRateServiceProvider? = null
    var loginStatusServiceProvider: LoginStatusServiceProvider? = null

    //   var addGoldServiceProvider: AddGoldServiceProvider? = null
    var getLoanServiceProvider: LoanServiceProvider? = null

    //   var cpServiceProvider: CPServiceProvider? = null
    private val pressBack = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        dynamicApCd = sharedPreferences.getString(SH_APP_CD, null).toString()
        firstName = sharedPreferences.getString(fName, null).toString()
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.title = "Welcome $firstName"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction().replace(R.id.frg_frame, MainFragment()).commit()



        navView.setNavigationItemSelectedListener {

            it.isChecked = true

            when (it.itemId) {
                R.id.nav_home -> replaceFragment(MainFragment(), it.title.toString())
                R.id.nav_profile -> {
                    try {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.nav_certificate -> startActivity(
                    Intent(
                        this,
                        CertificatesActivity::class.java
                    )
                )


                R.id.nav_membership -> {
                    startActivity(Intent(this, MembershipActivity::class.java))
                }


                R.id.nav_eGoldWallet -> {
                    try {
                        startActivity(Intent(this, GoldWalletActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                R.id.nav_moneyWallet -> {
                    try {
                        startActivity(Intent(this, MoneyWalletActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                //replaceFragment(MoneyWalletFragment(),it.title.toString())
                R.id.nav_payInstallment -> {
                    try {
                        startActivity(Intent(this, PayInstallmentActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                //replaceFragment(PayInstallmentFragment(),it.title.toString())
                R.id.nav_addBank -> replaceFragment(AddBankFragment(), it.title.toString())


                R.id.nav_channel_partner -> {
                    try {
                        startActivity(Intent(this, ChannelPartnerActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                R.id.nav_review -> {
                    try {
                        startActivity(Intent(this, ReviewActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

                R.id.nav_goldBookingHistory -> startActivity(
                    Intent(
                        this,
                        GoldBookingHistory::class.java
                    )
                )
                R.id.nav_goldDepositeHistory -> startActivity(
                    Intent(
                        this,
                        GoldDepositHistoryActivity::class.java
                    )
                )
                R.id.nav_addComplain -> replaceFragment(AddComplainFragment(), it.title.toString())
                R.id.nav_feedback -> replaceFragment(FeedbackFragment(), it.title.toString())
                R.id.nav_Share -> shareTextOnly()
                R.id.nav_refer -> startActivity(Intent(this, ReferActivity::class.java))

                R.id.nav_logout -> logoutAlert()

            }
            true
        }

    }


    fun OnShare() {
        VGoldApp.onGetUerId()?.let { AttemptToRefer(it) }
    }

    private fun shareTextOnly() {
        //    "https://play.google.com/store/apps/details?id=" + applicationContext.packageName + "&referrer="


        val refCode =
            "https://play.google.com/store/apps/details?id=com.cognifygroup.vgold&hl=en_IN&gl=US"


        // The value which we will sending through data via

        // other applications is defined

        // via the Intent.ACTION_SEND
        val intentt = Intent(Intent.ACTION_SEND)


        // setting type of data shared as text
        intentt.type = "text/plain"
        intentt.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")


        // Adding the text to share using putExtra
        intentt.putExtra(Intent.EXTRA_TEXT, refCode)
        startActivity(Intent.createChooser(intentt, "Share Via"))
    }


    private fun AttemptToRefer(user_id: String) {
        progressDialog?.show();
        referServiceProvider?.getReferenceCode(user_id, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    progressDialog?.hide();
                    val status: String? = (serviceResponse as ReferModel).getStatus()
                    val message: String? = (serviceResponse as ReferModel).getMessage()
                    val data: String? = (serviceResponse as ReferModel).getData()
                    if (status == "200") {
                        if (data != null && !TextUtils.isEmpty(data)) {
                            createDynamicLink(data)


                        } /*else {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                String packageName = getApplicationContext().getPackageName();
                                intent.putExtra(Intent.EXTRA_TEXT, "https://www.play.google.com/store/apps/details?id=" + packageName + "&referrer=" + data);
                                startActivity(Intent.createChooser(intent, "Share App with Friends!"));
                            }*/
                    } else {
                        AlertDialogs().alertDialogOk(
                            this@MainActivity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
                        )

                        //                        mAlert.onShowToastNotification(MainActivity.this, message);
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    //                    progressDialog.hide();
                } finally {
                    //                    progressDialog.hide();
                }
            }

            override fun <T> onFailure(apiErrorModel: T, extras: T) {

                try {
                    //                    progressDialog.hide();
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(
                            this@MainActivity,
                            (apiErrorModel as BaseServiceResponseModel).message
                        )
                    } else {
                        PrintUtil.showNetworkAvailableToast(this@MainActivity)
                    }
                } catch (e: Exception) {
                    //                    progressDialog.hide();
                    e.printStackTrace()
                    PrintUtil.showNetworkAvailableToast(this@MainActivity)
                } finally {
                    //                    progressDialog.hide();
                }
            }
        })
    }

    private fun createDynamicLink(refCode: String) {
        val link =
            "https://play.google.com/store/apps/details?id=" + applicationContext.packageName + "&referrer=" + refCode
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(link))
            .setDomainUriPrefix("https://vgold.page.link")
            .setAndroidParameters(
                DynamicLink.AndroidParameters.Builder("com.cognifygroup.myappvgold")
                    .setMinimumVersion(125)
                    .build()
            )
            .setIosParameters(
                DynamicLink.IosParameters.Builder("com.cognifygroup.myappvgold")
                    .setAppStoreId("123456789")
                    .setMinimumVersion("1.0.1")
                    .build()
            )
            .buildShortDynamicLink()
            .addOnSuccessListener(OnSuccessListener<Any> { shortDynamicLink ->
                val mInvitationUrl: String =
                    java.lang.String.valueOf(shortDynamicLink)
                if (mInvitationUrl != null && !TextUtils.isEmpty(mInvitationUrl)) {
//                        Log.d("TAG", mInvitationUrl);
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, mInvitationUrl)
                    startActivity(Intent.createChooser(intent, "Share App with Friends!"))
                }
            })
    }


    private fun replaceFragment(fragment: Fragment, title: String) {


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frg_frame, fragment)
            .addToBackStack("MainFragment")
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main_top_logo, menu)
        return true


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun fragmentMethod() {
        val intent = Intent(this@MainActivity, ReferAndEarnActivity::class.java)
        startActivity(intent)

    }

    private fun logoutAlert() {

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putString(SH_APP_CD, dynamicApCd)
        editor.apply()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


//    private fun logoutAlert() {
//        val alertDialog = AlertDialog.Builder(this)
//
//        // Setting Dialog Title
//        alertDialog.setTitle("")
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Are you sure to logout?")
//
//        // On pressing Settings button
//        alertDialog.setPositiveButton(
//            "Yes"
//        ) { dialog, which ->
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            intent.flags =
//                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
//            finish()
//        }
//
//        // on pressing cancel button
//        alertDialog.setNegativeButton(
//            "No"
//        ) { dialog, which -> dialog.cancel() }
//        // Showing Alert Message
//        alertDialog.show()
//    }

    override fun onDialogOk(resultCode: Int) {
        if (resultCode == 11) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
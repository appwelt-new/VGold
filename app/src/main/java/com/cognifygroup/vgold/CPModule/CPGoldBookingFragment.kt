package com.cognifygroup.vgold.CPModule

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.AlertDialogs
import com.cognifygroup.vgold.activities.PrintUtil
import com.cognifygroup.vgold.adapters.CPUserGoldDetailsAdapter
import com.cognifygroup.vgold.channelpartner.UserGoldDetailsModel
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class CPGoldBookingFragment     // Required empty public constructor
    (private val uid: String) : Fragment(),
    AlertDialogOkListener {
    private var activity: Activity? = null

    //    @InjectView(R.id.recyclerGoldDetails)
    //    RecyclerView recyclerGoldDetails;
    private var progressDialog: TransparentProgressDialog? = null
    private var mCPUserServiceProvider: CPServiceProvider? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var recyclerGoldDetails: RecyclerView? = null
    private var noData: TextView? = null
    private var mAdapter: CPUserGoldDetailsAdapter? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_cp_gold_details, container, false)
        //        ButterKnife.inject(activity);
        initView(view)
        return view
    }

    private fun initView(view: View) {
        progressDialog = TransparentProgressDialog(activity)
        progressDialog!!.setCancelable(false)
        requireActivity().setFinishOnTouchOutside(false)
        mCPUserServiceProvider = CPServiceProvider(activity)
        val etGoldSearch = view.findViewById<AppCompatEditText>(R.id.etGoldSearch)
        noData = view.findViewById<View>(R.id.noData) as TextView
        recyclerGoldDetails = view.findViewById<View>(R.id.recyclerGoldDetails) as RecyclerView
        recyclerGoldDetails!!.layoutManager = LinearLayoutManager(activity)
        etGoldSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count < before) {
                    mAdapter?.resetData()
                }
                if (mAdapter != null) {
                   // mAdapter!!.getFilter().filter(s.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        userGoldBookingDetails
    }

    private val userGoldBookingDetails: Unit
        private get() {
            progressDialog?.show()
            mCPUserServiceProvider!!.getUserGoldDetails(uid, object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val Status: String? = (serviceResponse as UserGoldDetailsModel).status
                        val message: String? = (serviceResponse as UserGoldDetailsModel).message
                        val userGoldDetailsArrayList: ArrayList<UserGoldDetailsModel.Data>? =
                            (serviceResponse as UserGoldDetailsModel).data
                        if (Status == "200") {
                            if (userGoldDetailsArrayList != null && userGoldDetailsArrayList.size > 0) {
                                noData!!.visibility = View.GONE
                                recyclerGoldDetails!!.layoutManager = LinearLayoutManager(activity)
                                mAdapter = CPUserGoldDetailsAdapter(
                                    activity!!, userGoldDetailsArrayList,
                                    uid
                                )
                                recyclerGoldDetails!!.adapter = mAdapter
                            } else {
                                noData!!.visibility = View.VISIBLE
                            }
                        } else {
                            AlertDialogs().alertDialogOk(
                                activity,
                                "Alert",
                                message,
                                resources.getString(R.string.btn_ok),
                                0,
                                false,
                                alertDialogOkListener
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        progressDialog?.hide()
                    }
                }

                override fun <T> onFailure(apiErrorModel: T, extras: T) {

                    try {
                        if (apiErrorModel != null) {
                            PrintUtil.showToast(
                                activity,
                                (apiErrorModel as BaseServiceResponseModel).message
                            )
                        } else {
                            PrintUtil.showNetworkAvailableToast(activity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        PrintUtil.showNetworkAvailableToast(activity)
                    } finally {
                        progressDialog?.hide()
                    }
                }
            })
        }

    override fun onDialogOk(resultCode: Int) {}
}
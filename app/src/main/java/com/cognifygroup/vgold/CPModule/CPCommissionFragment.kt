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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.AlertDialogs
import com.cognifygroup.vgold.activities.PrintUtil
import com.cognifygroup.vgold.adapters.CPUserCommissionDetailsAdapter
import com.cognifygroup.vgold.channelpartner.UserCommissionDetailsModel
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class CPCommissionFragment     // Required empty public constructor
    (private val uid: String) : Fragment(),
    AlertDialogOkListener {
    private var activity: Activity? = null
    private var progressDialog: TransparentProgressDialog? = null
    private var mCPUserServiceProvider: com.cognifygroup.vgold.CPModule.CPServiceProvider? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var recyclerCommissionDetails: RecyclerView? = null
    private var noData: TextView? = null
    private var mAdapter: CPUserCommissionDetailsAdapter? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_cp_commission_details, container, false)
        ButterKnife.inject(activity)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        progressDialog = TransparentProgressDialog(activity)
        progressDialog!!.setCancelable(false)
        requireActivity().setFinishOnTouchOutside(false)
        mCPUserServiceProvider = com.cognifygroup.vgold.CPModule.CPServiceProvider(activity)
        val etCommissionSearch = view.findViewById<AppCompatEditText>(R.id.etCommissionSearch)
        noData = view.findViewById<View>(R.id.noData) as TextView
        recyclerCommissionDetails =
            view.findViewById<View>(R.id.recyclerCommissionDetails) as RecyclerView
        recyclerCommissionDetails!!.setHasFixedSize(true)
        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerCommissionDetails!!.layoutManager = mLayoutManager
        recyclerCommissionDetails!!.itemAnimator = DefaultItemAnimator()
        etCommissionSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count < before) {
                    mAdapter?.resetData()
                }
                if (mAdapter != null) {
                    mAdapter?.getFilter()?.toString()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        userCommissionDetails
    }

    private val userCommissionDetails: Unit
        private get() {
            progressDialog?.show()
            mCPUserServiceProvider?.getUserCommissionDetails(uid, object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val Status: String? =
                            (serviceResponse as UserCommissionDetailsModel).status
                        val message: String? =
                            (serviceResponse as UserCommissionDetailsModel).message
                        val userGoldCommssionDetailsArrayList: ArrayList<UserCommissionDetailsModel.Data>? =
                            (serviceResponse as UserCommissionDetailsModel).data
                        if (Status == "200") {
                            if (userGoldCommssionDetailsArrayList != null && userGoldCommssionDetailsArrayList.size > 0) {
                                noData!!.visibility = View.GONE
                                mAdapter = activity?.let {
                                    CPUserCommissionDetailsAdapter(
                                        it, userGoldCommssionDetailsArrayList,
                                        uid
                                    )
                                }
                                recyclerCommissionDetails!!.adapter = mAdapter
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
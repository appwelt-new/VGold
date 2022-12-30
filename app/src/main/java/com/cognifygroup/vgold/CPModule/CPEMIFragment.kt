package com.cognifygroup.vgold.CPModule

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.AlertDialogs
import com.cognifygroup.vgold.activities.PrintUtil
import com.cognifygroup.vgold.adapters.CPEMIStatusDetailsAdapter
import com.cognifygroup.vgold.adapters.CPUserEMIDetailsAdapter
import com.cognifygroup.vgold.channelpartner.UserEMIDetailsModel
import com.cognifygroup.vgold.channelpartner.UserEMIStatusDetailsModel
import com.cognifygroup.vgold.interfaces.APICallback
import com.cognifygroup.vgold.interfaces.AlertDialogOkListener
import com.cognifygroup.vgold.model.BaseServiceResponseModel
import com.cognifygroup.vgold.utilities.TransparentProgressDialog

class CPEMIFragment     // Required empty public constructor
    (private val uid: String) : Fragment(),
    AlertDialogOkListener, CPUserEMIDetailsAdapter.EMIDetailsListener {
    private var activity: Activity? = null
    private var progressDialog: TransparentProgressDialog? = null
    private var mCPUserServiceProvider: CPServiceProvider? = null
    private val alertDialogOkListener: AlertDialogOkListener = this
    private var recyclerEMIDetails: RecyclerView? = null
    private var noData: TextView? = null
    private var mAdapter: CPUserEMIDetailsAdapter? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_cp_emi_details, container, false)
        ButterKnife.inject(activity)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        progressDialog = TransparentProgressDialog(activity)
        progressDialog!!.setCancelable(false)
        requireActivity().setFinishOnTouchOutside(false)
        mCPUserServiceProvider = CPServiceProvider(activity)
        val etEMISearch = view.findViewById<AppCompatEditText>(R.id.etEMISearch)
        noData = view.findViewById<View>(R.id.noData) as TextView
        recyclerEMIDetails = view.findViewById<View>(R.id.recyclerEMIDetails) as RecyclerView
        recyclerEMIDetails!!.layoutManager = LinearLayoutManager(activity)
        etEMISearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count < before) {
                    if (mAdapter != null) {
                        mAdapter!!.resetData()
                    }
                }
                if (mAdapter != null) {
                   // mAdapter!!.getFilter().filter(s.toString())
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        userEMIDetails
    }

    private val userEMIDetails: Unit
        private get() {
            progressDialog?.show()
            mCPUserServiceProvider!!.getUserEMIDetails(uid, object : APICallback {
                override fun <T> onSuccess(serviceResponse: T) {
                    try {
                        val Status: String? = (serviceResponse as UserEMIDetailsModel).status
                        val message: String? = (serviceResponse as UserEMIDetailsModel).message
                        val userGoldCommssionDetailsArrayList: ArrayList<UserEMIDetailsModel.Data>? =
                            (serviceResponse as UserEMIDetailsModel).data
                        if (Status == "200") {
                            if (userGoldCommssionDetailsArrayList != null && userGoldCommssionDetailsArrayList.size > 0) {
                                noData!!.visibility = View.GONE
                                setListData(userGoldCommssionDetailsArrayList)
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

    private fun setListData(userGoldCommssionDetailsArrayList: ArrayList<UserEMIDetailsModel.Data>) {
        mAdapter =
            activity?.let { CPUserEMIDetailsAdapter(it, userGoldCommssionDetailsArrayList, this) }
        recyclerEMIDetails!!.adapter = mAdapter
    }

    private fun EMIDetailsDialog(statusList: ArrayList<UserEMIStatusDetailsModel.Data>?) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.cp_emi_details_dialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val window = dialog.window!!
        window.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val closeLayout = dialog.findViewById<LinearLayoutCompat>(R.id.closeLayout)
        val rvEMIStatusDetails = dialog.findViewById<RecyclerView>(R.id.rvEMIStatusDetails)
        rvEMIStatusDetails.layoutManager = LinearLayoutManager(activity)
        if (statusList != null && statusList.size > 0) {
            val adapter = activity?.let { CPEMIStatusDetailsAdapter(it, statusList) }
            rvEMIStatusDetails.adapter = adapter
        }
        closeLayout.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onItemClick(model: UserEMIDetailsModel.Data?) {
        if (model != null) {
            model.gold_booking_id?.let { getEMIStatusDetails(it) }
        }
    }

    private fun getEMIStatusDetails(BookingId: String) {
        progressDialog?.show()
        mCPUserServiceProvider!!.getUserEMIStatusDetails(BookingId, object : APICallback {
            override fun <T> onSuccess(serviceResponse: T) {
                try {
                    val Status: String? = (serviceResponse as UserEMIStatusDetailsModel).status
                    val message: String? =
                        (serviceResponse as UserEMIStatusDetailsModel).message
                    val userEMIStatusDetailsArrayList: ArrayList<UserEMIStatusDetailsModel.Data>? =
                        (serviceResponse as UserEMIStatusDetailsModel).data
                    if (Status == "200") {
                        if (userEMIStatusDetailsArrayList != null && userEMIStatusDetailsArrayList.size > 0) {
                            EMIDetailsDialog(userEMIStatusDetailsArrayList)
                        }
                    } else {
                        AlertDialogs().alertDialogOk(
                            activity, "Alert", message,
                            resources.getString(R.string.btn_ok), 0, false, alertDialogOkListener
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

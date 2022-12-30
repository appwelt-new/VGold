package com.cognifygroup.vgold.adapters

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.InjectView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.channelpartner.UserEMIDetailsModel
import java.util.*

class CPUserEMIDetailsAdapter(
    private val mContext: Context,
    userEmiDetailsArrayList: ArrayList<UserEMIDetailsModel.Data>,
    listener: EMIDetailsListener
) : RecyclerView.Adapter<CPUserEMIDetailsAdapter.MyViewHolder>() {
    private val activity: Activity
    var str: String? = null
    var userEmiDetailsArrayList: ArrayList<UserEMIDetailsModel.Data>
    var orgUserEmiDetailsArrayList: ArrayList<UserEMIDetailsModel.Data>
    private val listener: EMIDetailsListener
    private var customerFilter: Filter? = null

    init {
        activity = mContext as Activity
        this.userEmiDetailsArrayList = userEmiDetailsArrayList
        orgUserEmiDetailsArrayList = userEmiDetailsArrayList
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cp_user_emi_details_adapter, null)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataList: UserEMIDetailsModel.Data = userEmiDetailsArrayList[position]
        if (dataList.last_paid_date != null && !TextUtils.isEmpty(dataList.last_paid_date)) {
            holder.txtDate?.setText(dataList.last_paid_date)
            holder.lastPaidLayout!!.visibility = View.VISIBLE
        } else {
            holder.lastPaidLayout!!.visibility = View.GONE
        }
        holder.txtGoldBookingId?.setText(dataList.gold_booking_id)
        holder.txtEmiAmt!!.text =
            mContext.resources.getString(R.string.rs) + dataList.monthly_installment
        holder.txtNextInstallment?.setText(dataList.upcoming_installment_no)


//        holder.detailLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(dataList);
//            }
//        });
    }

    override fun getItemCount(): Int {
        return userEmiDetailsArrayList.size
    }

    inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        @InjectView(R.id.txtDate)
        var txtDate: TextView? = null

        @InjectView(R.id.txtGoldBookingId)
        var txtGoldBookingId: TextView? = null

        @InjectView(R.id.txtEmiAmt)
        var txtEmiAmt: TextView? = null

        @InjectView(R.id.txtNextInstallment)
        var txtNextInstallment: TextView? = null

        @InjectView(R.id.detailLayout)
        var detailLayout: LinearLayout? = null

        @InjectView(R.id.lastPaidLayout)
        var lastPaidLayout: LinearLayout? = null

        init {
            ButterKnife.inject(this, itemView)
        }
    }

    interface EMIDetailsListener {
        fun onItemClick(model: UserEMIDetailsModel.Data?)
    }

    fun resetData() {
        userEmiDetailsArrayList = orgUserEmiDetailsArrayList
    }

    fun getFilter(): Any {

        if (customerFilter == null) customerFilter =
            CustomFilter()

        return customerFilter as Filter
    }



    val filter: Filter?
        get() {
            if (customerFilter == null) customerFilter = CustomFilter()
            return customerFilter
        }

    private inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            try {
                // We implement here the filter logic
                if (constraint.length == 0) {
                    // No filter implemented we return all the list
                    results.values = orgUserEmiDetailsArrayList
                    results.count = orgUserEmiDetailsArrayList.size
                } else {
                    // We perform filtering operation
                    val customerArrayList: MutableList<UserEMIDetailsModel.Data> =
                        ArrayList<UserEMIDetailsModel.Data>()
                    for (dataModel in userEmiDetailsArrayList) {
                        if (dataModel.gold_booking_id?.toLowerCase(Locale.getDefault())!!
                                .contains(
                                    constraint.toString().lowercase(
                                        Locale.getDefault()
                                    )
                                )
                            || dataModel.gold?.toLowerCase(Locale.getDefault())!!.contains(
                                constraint.toString().lowercase(
                                    Locale.getDefault()
                                )
                            )
                            || dataModel.monthly_installment?.toLowerCase(Locale.getDefault())!!
                                .contains(
                                    constraint.toString().lowercase(
                                        Locale.getDefault()
                                    )
                                )
                        ) {
                            customerArrayList.add(dataModel)
                        }
                    }
                    results.values = customerArrayList
                    results.count = customerArrayList.size
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            userEmiDetailsArrayList = results.values as ArrayList<UserEMIDetailsModel.Data>
            notifyDataSetChanged()
        }
    }
}
package com.cognifygroup.vgold.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.InjectView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.channelpartner.UserCommissionDetailsModel
import java.util.*


    class CPUserCommissionDetailsAdapter(
        private val mContext: Context,
        userCommissionDetailsArrayList: ArrayList<UserCommissionDetailsModel.Data>,
        uid: String
    ) : RecyclerView.Adapter<CPUserCommissionDetailsAdapter.MyViewHolder>() {
        private val activity: Activity
        var str: String? = null
        var uid: String
        var userCommissionDetailsArrayList: ArrayList<UserCommissionDetailsModel.Data>
        var orgUserCommissionDetailsArrayList: ArrayList<UserCommissionDetailsModel.Data>
        private var customerFilter: Filter? = null

        init {
            activity = mContext as Activity
            this.userCommissionDetailsArrayList = userCommissionDetailsArrayList
            orgUserCommissionDetailsArrayList = userCommissionDetailsArrayList
            this.uid = uid
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.cp_user_commission_details_adapter, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val dataList: UserCommissionDetailsModel.Data = userCommissionDetailsArrayList[position]
            holder.txtDate?.setText(dataList.created_date)
            holder.txtGoldBookingId?.setText(dataList.booking_id + " - " + dataList.gold + " gm")
            holder.txtCommssion!!.text =
                mContext.resources.getString(R.string.rs) + dataList.commission_amount
        }

        override fun getItemCount(): Int {
            return userCommissionDetailsArrayList.size
        }

        inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(
            itemView!!
        ) {
            @InjectView(R.id.txtDate)
            var txtDate: TextView? = null

            @InjectView(R.id.txtGoldBookingId)
            var txtGoldBookingId: TextView? = null

            @InjectView(R.id.txtCommssion)
            var txtCommssion: TextView? = null

            init {
                ButterKnife.inject(this, itemView)
            }
        }

        fun resetData() {
            userCommissionDetailsArrayList = orgUserCommissionDetailsArrayList
        }

        fun getFilter(): Any {
            if (customerFilter == null) customerFilter = CustomFilter()
            return customerFilter!!
        }


        private inner class CustomFilter : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                try {
                    // We implement here the filter logic
                    if (constraint == null || constraint.length == 0) {
                        // No filter implemented we return all the list
                        results.values = orgUserCommissionDetailsArrayList
                        results.count = orgUserCommissionDetailsArrayList.size
                    } else {
                        // We perform filtering operation
                        val customerArrayList: MutableList<UserCommissionDetailsModel.Data> =
                            ArrayList<UserCommissionDetailsModel.Data>()
                        for (dataModel in userCommissionDetailsArrayList) {
                            if (dataModel.booking_id?.toLowerCase(Locale.getDefault())!!.contains(
                                    constraint.toString().lowercase(
                                        Locale.getDefault()
                                    )
                                )
                                || dataModel.gold?.toLowerCase(Locale.getDefault())!!.contains(
                                    constraint.toString().lowercase(
                                        Locale.getDefault()
                                    )
                                )
                                || dataModel.created_date?.toLowerCase(Locale.getDefault())
                                    !!.contains(
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
                userCommissionDetailsArrayList =
                    results.values as ArrayList<UserCommissionDetailsModel.Data>
                notifyDataSetChanged()
            }
        }
    }

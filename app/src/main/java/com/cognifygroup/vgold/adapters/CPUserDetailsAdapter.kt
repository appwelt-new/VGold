package com.cognifygroup.vgold.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.channelpartner.UserDetailsModel_
import java.util.*

class CPUserDetailsAdapter(
    private val mContext: Context,
    userDetailsArrayList: ArrayList<UserDetailsModel_.Data>
) : RecyclerView.Adapter<CPUserDetailsAdapter.MyViewHolder>() {

    val activity: Activity
    var str: String? = null
    var userDetailsArrayList: ArrayList<UserDetailsModel_.Data>
    var orgUserDetailsArrayList: ArrayList<UserDetailsModel_.Data>
    private var customerFilter: Filter? = null


    init {
        activity = mContext as Activity
        this.userDetailsArrayList = userDetailsArrayList
        orgUserDetailsArrayList = userDetailsArrayList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cp_user_details_item_adapter, null)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var dataList: UserDetailsModel_.Data = this.userDetailsArrayList.get(position)
        holder.txtName?.setText(dataList.uname + " " + dataList.total_gold_in_account + " gm")

//        holder.detailLayout?.setOnClickListener {
//            val LogIntent = Intent(activity, CPUserDetailsActivity::class.java)
//            LogIntent.putExtra("userDetailsModel", dataList as Serializable)
//            mContext.startActivity(LogIntent)
//            notifyDataSetChanged()
//        }
    }

    override fun getItemCount(): Int {
        return userDetailsArrayList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName: TextView = itemView.findViewById(R.id.txtName)
    }

    fun resetData() {
        userDetailsArrayList = orgUserDetailsArrayList
    }


    fun getFilter(): Filter? {
        if (customerFilter == null) customerFilter =
            CustomFilter()

        return customerFilter
    }


    val getFilter: Filter
        get() {
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
                    results.values = userDetailsArrayList
                    results.count = orgUserDetailsArrayList.size
                } else {
                    // We perform filtering operation
                    val customerArrayList: MutableList<UserDetailsModel_.Data> =
                        ArrayList<UserDetailsModel_.Data>()
                    for (dataModel in userDetailsArrayList) {
                        if (dataModel.uname?.toLowerCase(Locale.getDefault())!!.contains(
                                constraint.toString().lowercase(
                                    Locale.getDefault()
                                )
                            )
                            || dataModel.umobile?.toLowerCase(Locale.getDefault())!!.contains(
                                constraint.toString().lowercase(
                                    Locale.getDefault()
                                )
                            )
                            || dataModel.uid?.toLowerCase(Locale.getDefault())!!.contains(
                                constraint.toString().lowercase(
                                    Locale.getDefault()
                                )
                            )
                            || dataModel.uemail?.toLowerCase(Locale.getDefault())!!.contains(
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
            userDetailsArrayList = results.values as ArrayList<UserDetailsModel_.Data>
            notifyDataSetChanged()

//            no_tv.setText("(" + String.valueOf(results.count) + ")");
        }
    }
}
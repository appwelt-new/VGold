package com.cognifygroup.vgold.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.InjectView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.channelpartner.UserGoldDetailsModel
import java.util.*

class CPUserGoldDetailsAdapter(
    private val mContext: Context,
    userGoldDetailsArrayList: ArrayList<UserGoldDetailsModel.Data>,
    uid: String
) : RecyclerView.Adapter<CPUserGoldDetailsAdapter.MyViewHolder>() {
    private val activity: Activity
    var str: String? = null
    var uid: String
    var userGoldDetailsArrayList: ArrayList<UserGoldDetailsModel.Data>
    var orgUserGoldDetailsArrayList: ArrayList<UserGoldDetailsModel.Data>
    private var customerFilter: Filter? = null

    init {
        activity = mContext as Activity
        this.userGoldDetailsArrayList = userGoldDetailsArrayList
        orgUserGoldDetailsArrayList = userGoldDetailsArrayList
        this.uid = uid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cp_user_gold_details_adapter, null)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataList: UserGoldDetailsModel.Data = userGoldDetailsArrayList[position]
        holder.txtDate?.setText(dataList.added_date)
        holder.txtGoldBookingId?.setText(dataList.gold_booking_id)
        holder.txtGoldQty?.setText(dataList.gold + " gm")
        holder.txtRate!!.text = mContext.resources.getString(R.string.rs) + dataList.rate
        holder.txtGoldBookingValue!!.text =
            mContext.resources.getString(R.string.rs) + dataList.booking_amount
        holder.txtBookingCharge!!.text =
            mContext.resources.getString(R.string.rs) + dataList.booking_charge
        holder.txtDownPayment!!.text =
            mContext.resources.getString(R.string.rs) + dataList.down_payment
        holder.txtInstallment!!.text =
            mContext.resources.getString(R.string.rs) + dataList.monthly_installment
        holder.txtTenure?.setText(dataList.tennure)
        holder.txtPromoCode?.setText(dataList.promo_code)
        if (dataList.account_status.equals("1")) {
            holder.txtAccountStatus!!.text = "Active"
            holder.txtAccountStatus!!.setTextColor(Color.argb(255, 0, 128, 0))
            holder.closeLayout!!.visibility = View.GONE
        } else if (dataList.account_status.equals("2")) {
            holder.txtAccountStatus!!.text = "Matured"
            holder.txtAccountStatus!!.setTextColor(Color.BLUE)
            holder.closeLayout!!.visibility = View.GONE
        } else {
            holder.txtAccountStatus!!.text = "Closed"
            holder.txtAccountStatus!!.setTextColor(Color.RED)
            holder.closeLayout!!.visibility = View.VISIBLE
        }
        holder.progressBar!!.secondaryProgress = dataList.totalPaidInstallment!!.toInt()
        holder.progressBar!!.progress = dataList.totalPaidInstallment!!.toInt()
        holder.progressBar!!.max = dataList.tennure!!.toInt()
        holder.imgGoldBookingHistory!!.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://www.vgold.co.in/dashboard/user/module/goldbooking/booking_receipt.php?bid=" + dataList.gold_booking_id
                        .toString() + "&&user_id=" + uid
                )
            )
            mContext.startActivity(browserIntent)
        }
    }

    override fun getItemCount(): Int {
        return userGoldDetailsArrayList.size
    }

    inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        @InjectView(R.id.txtDate)
        var txtDate: TextView? = null

        @InjectView(R.id.txtGoldBookingId)
        var txtGoldBookingId: TextView? = null

        @InjectView(R.id.txtGoldQty)
        var txtGoldQty: TextView? = null

        @InjectView(R.id.txtRate)
        var txtRate: TextView? = null

        @InjectView(R.id.txtGoldBookingValue)
        var txtGoldBookingValue: TextView? = null

        @InjectView(R.id.txtBookingCharge)
        var txtBookingCharge: TextView? = null

        @InjectView(R.id.txtDownPayment)
        var txtDownPayment: TextView? = null

        @InjectView(R.id.txtInstallment)
        var txtInstallment: TextView? = null

        @InjectView(R.id.txtTenure)
        var txtTenure: TextView? = null

        @InjectView(R.id.txtPromoCode)
        var txtPromoCode: TextView? = null

        @InjectView(R.id.txtAccountStatus)
        var txtAccountStatus: TextView? = null

        @InjectView(R.id.imgGoldBookingHistory)
        var imgGoldBookingHistory: ImageView? = null

        @InjectView(R.id.progressBar)
        var progressBar: ProgressBar? = null

        @InjectView(R.id.closeLayout)
        var closeLayout: LinearLayout? = null

        @InjectView(R.id.detailLayout)
        var detailLayout: LinearLayout? = null

        init {
            ButterKnife.inject(this, itemView)
        }
    }

    fun resetData() {
        userGoldDetailsArrayList = orgUserGoldDetailsArrayList
    }

    fun getFilter(): Any {
        if (customerFilter == null) customerFilter = CustomFilter()
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
                if (constraint == null || constraint.length == 0) {
                    // No filter implemented we return all the list
                    results.values = orgUserGoldDetailsArrayList
                    results.count = orgUserGoldDetailsArrayList.size
                } else {
                    // We perform filtering operation
                    val customerArrayList: MutableList<UserGoldDetailsModel.Data> =
                        ArrayList<UserGoldDetailsModel.Data>()
                    for (dataModel in userGoldDetailsArrayList) {
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
                            || dataModel.added_date?.toLowerCase(Locale.getDefault())!!.contains(
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
            userGoldDetailsArrayList = results.values as ArrayList<UserGoldDetailsModel.Data>
            notifyDataSetChanged()
        }
    }
}
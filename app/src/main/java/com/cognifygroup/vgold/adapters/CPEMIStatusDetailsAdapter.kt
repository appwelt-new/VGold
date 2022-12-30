package com.cognifygroup.vgold.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.InjectView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.channelpartner.UserEMIStatusDetailsModel

class CPEMIStatusDetailsAdapter(
    private val mContext: Context,
    emiStatusDetailsArrayList: ArrayList<UserEMIStatusDetailsModel.Data>
) : RecyclerView.Adapter<CPEMIStatusDetailsAdapter.MyViewHolder>() {
    private val activity: Activity
    var emiStatusDetailsArrayList: ArrayList<UserEMIStatusDetailsModel.Data>

    init {
        activity = mContext as Activity
        this.emiStatusDetailsArrayList = emiStatusDetailsArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.cp_user_emi_status_adapter, null)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataList: UserEMIStatusDetailsModel.Data = emiStatusDetailsArrayList[position]
        holder.txtDate?.setText(dataList.transaction_date)
        if (dataList.is_paid.equals("1")) {
            holder.txtIsPaid!!.text = "Paid"
            holder.imgPaid!!.visibility = View.VISIBLE
        } else {
            holder.txtIsPaid!!.text = "Pending"
        }
        holder.txtInstallment!!.text =
            mContext.resources.getString(R.string.rs) + dataList.installment
        holder.txtRemainAmt!!.text =
            mContext.resources.getString(R.string.rs) + dataList.remaining_amount
        holder.txtNextDueDate?.setText(dataList.next_due_date)
    }

    override fun getItemCount(): Int {
        return emiStatusDetailsArrayList.size
    }

    inner class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        @InjectView(R.id.txtDate)
        var txtDate: TextView? = null

        @InjectView(R.id.txtIsPaid)
        var txtIsPaid: TextView? = null

        @InjectView(R.id.txtInstallment)
        var txtInstallment: TextView? = null

        @InjectView(R.id.txtRemainAmt)
        var txtRemainAmt: TextView? = null

        @InjectView(R.id.txtNextDueDate)
        var txtNextDueDate: TextView? = null

        @InjectView(R.id.imgPaid)
        var imgPaid: ImageView? = null

        init {
            ButterKnife.inject(this, itemView)
        }
    }
}
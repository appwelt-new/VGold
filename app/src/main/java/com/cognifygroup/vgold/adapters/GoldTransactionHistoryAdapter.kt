package com.cognifygroup.vgold.adapters

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.InjectView
import com.cognifygroup.vgold.GetGoldTransactionHistory.GetGoldTransactionHistoryModel
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.GoldTransactionHistoryActivity
import com.cognifygroup.vgold.activities.TransactionDetailsActivity


class GoldTransactionHistoryAdapter(
    Context: GoldTransactionHistoryActivity,
    goldTransactionHistoryArrayList: ArrayList<GetGoldTransactionHistoryModel.Data>,
    booking_id: String
) :
    RecyclerView.Adapter<GoldTransactionHistoryAdapter.MyViewHolder>() {
    private val mContext: GoldTransactionHistoryActivity
    var str: String? = null
    var booking_id: String
    var goldTransactionHistoryArrayList: ArrayList<GetGoldTransactionHistoryModel.Data>

    init {
        mContext = Context
        this.goldTransactionHistoryArrayList = goldTransactionHistoryArrayList
        this.booking_id = booking_id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.gold_transaction_history_item_adapter, null)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtTransactionId!!.text = booking_id
        if (goldTransactionHistoryArrayList[position].entry_status.equals("Credited")) {
            holder.txtStatus!!.text = "Credit"
            holder.txtStatus!!.setTextColor(Color.argb(255, 0, 128, 0))
        } else {
            holder.txtStatus!!.text = "Debit"
            holder.txtStatus!!.setTextColor(Color.RED)
        }
        holder.txtInstallmentAmount!!.text =
            "Amount :  \u20B9 " + goldTransactionHistoryArrayList[position].installment
        holder.txtTransactionId!!.text = "" + goldTransactionHistoryArrayList[position].id
        holder.txtTransactionDate!!.text =
            "Date  :  " + goldTransactionHistoryArrayList[position].transaction_date
        holder.cardTransactionHistory!!.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, TransactionDetailsActivity::class.java)
                    .putExtra(
                        "installment",
                        goldTransactionHistoryArrayList[position].installment
                    )
                    .putExtra("recipt_no", goldTransactionHistoryArrayList[position].id)
                    .putExtra(
                        "date",
                        goldTransactionHistoryArrayList[position].transaction_date
                    )
                    .putExtra(
                        "remainingamt",
                        goldTransactionHistoryArrayList[position].remaining_amount
                    )
                    .putExtra("period", goldTransactionHistoryArrayList[position].period)
                    .putExtra(
                        "txnid",
                        goldTransactionHistoryArrayList[position].transaction_id
                    )
                    .putExtra(
                        "bankdetail",
                        goldTransactionHistoryArrayList[position].bank_details
                    )
                    .putExtra(
                        "paymentmethod",
                        goldTransactionHistoryArrayList[position].payment_method
                    )
                    .putExtra("chequeno", goldTransactionHistoryArrayList[position].cheque_no)
                    .putExtra(
                        "adminstatus",
                        goldTransactionHistoryArrayList[position].admin_status
                    )
                    .putExtra("gold_id", booking_id)
                    .putExtra(
                        "next_due_date",
                        goldTransactionHistoryArrayList[position].next_due_date
                    )
            )
        }
    }

    override fun getItemCount(): Int {
        return goldTransactionHistoryArrayList.size
    }

    inner class MyViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        @InjectView(R.id.cardTransactionHistory)
        var cardTransactionHistory: CardView? = null

        @InjectView(R.id.txtTransactionId)
        var txtTransactionId: TextView? = null

        @InjectView(R.id.txtInstallmentAmount)
        var txtInstallmentAmount: TextView? = null

        @InjectView(R.id.txtTransactionDate)
        var txtTransactionDate: TextView? = null

        @InjectView(R.id.txtStatus)
        var txtStatus: TextView? = null

        init {
            ButterKnife.inject(this, itemView)
        }
    }
}

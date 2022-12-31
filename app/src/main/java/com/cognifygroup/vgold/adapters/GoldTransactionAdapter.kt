package com.cognifygroup.vgold.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.model.GetAllTransactionGoldModel
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat


class GoldTransactionAdapter(
    mContext: Context,
    goldTransactionArraylist: JSONArray
) :
    RecyclerView.Adapter<GoldTransactionAdapter.MyViewHolder>() {
    var mContext: Context
    var goldTransactionArraylist: JSONArray

    init {
        this.mContext = mContext
        this.goldTransactionArraylist = goldTransactionArraylist
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoldTransactionAdapter.MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.gold_booking_history_adapter, null)
        return GoldTransactionAdapter.MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GoldTransactionAdapter.MyViewHolder, position: Int) {
        val list: JSONObject? = goldTransactionArraylist.get(position) as JSONObject?

        if (!list?.opt("transaction_id")?.equals("")!!) {
            holder.txtTxnIdMoney1.setText(list.optString("transaction_id"))
        } else if (!list.opt("online_transaction_id")!!.equals("")) {
            holder.txtTxnIdMoney1.setText(list.optString("online_transaction_id"))
        } else {
            holder.txtTxnIdMoney1.setText(list.optString("cheque_no"))
        }
        var gold: Double = list.optString("gold").toDouble()
        val numberFormat = DecimalFormat("#.000")
        gold = numberFormat.format(gold).toDouble()
        holder.txtgoldBalance.text = "$gold GM"
        holder.txtTimeDateMoney1.setText(list.optString("transaction_date"))
        holder.txtPaymentThrough.setText(list.optString("payment_method"))
        if (!list.optString("received_from").equals("")) {
            holder.txtPaymentFromTo.setText(list.optString("received_from"))
        } else {
            holder.txtPaymentFromTo!!.setText(list.optString("transafer_to"))
        }
        if (list.optString("admin_status").equals("0")) {
            holder.txtStatus1.setText("Pending")
        } else {
            holder.txtStatus1.setText("Success")
        }
    }

    override fun getItemCount(): Int {
        return goldTransactionArraylist.length()
    }


    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtTxnIdMoney1: TextView = itemView.findViewById(R.id.txtTxnIdMoney1)
        val txtgoldBalance: TextView = itemView.findViewById(R.id.txtgoldBalance)
        val txtTimeDateMoney1: TextView = itemView.findViewById(R.id.txtTimeDateMoney1)
        val txtPaymentThrough: TextView = itemView.findViewById(R.id.txtPaymentThrough)
        val txtPaymentFromTo: TextView = itemView.findViewById(R.id.txtPaymentFromTo)
        val txtStatus1: TextView = itemView.findViewById(R.id.txtStatus1)

    }
}
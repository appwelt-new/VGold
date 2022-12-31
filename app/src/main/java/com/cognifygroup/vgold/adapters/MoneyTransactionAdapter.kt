package com.cognifygroup.vgold.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import org.json.JSONArray
import org.json.JSONObject

class MoneyTransactionAdapter(mContext: Context, moneyTransactionArraylist: JSONArray) :
    RecyclerView.Adapter<MoneyTransactionAdapter.MyViewHolder>() {
    var mContext: Context
    var moneyTransactionArraylist: JSONArray

    init {
        this.mContext = mContext
        this.moneyTransactionArraylist = moneyTransactionArraylist
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTxnIdMoney: TextView = itemView.findViewById(R.id.txtTxnIdMoney)
        val txtRupeeMoney: TextView = itemView.findViewById(R.id.txtRupeeMoney)
        val txtTimeDateMoney: TextView = itemView.findViewById(R.id.txtTimeDateMoney)
        val txtPaymentThrough: TextView = itemView.findViewById(R.id.txtPaymentThrough)
        val txtPaymentFromTo: TextView = itemView.findViewById(R.id.txtPaymentFromTo)
        val txtStatus1: TextView = itemView.findViewById(R.id.txtStatus1)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MoneyTransactionAdapter.MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.booking_history_adapter, null)
        return MoneyTransactionAdapter.MyViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list: JSONObject? = moneyTransactionArraylist.get(position) as JSONObject?

        if (list?.optString("transaction_id").equals("")) {
            holder.txtTxnIdMoney.setText(
                list?.getString("transaction_id")
            )
        } else if (list?.optString("online_transaction_id").equals("")) {
            holder.txtTxnIdMoney!!.setText(
                list?.getString("online_transaction_id")
            )
        } else {
            holder.txtTxnIdMoney.setText(list?.getString("cheque_no"))
        }

        holder.txtRupeeMoney.text = "Rs." + list?.getString("amount")
        holder.txtTimeDateMoney.setText(
            list?.getString("transaction_date")
        )
        holder.txtPaymentThrough!!.setText(
            list?.getString("payment_method")
        )

        if (!list?.getString("received_from").equals("")) {
            holder.txtPaymentFromTo!!.setText(
                list?.getString("received_from")
            )
        } else {
            holder.txtPaymentFromTo!!.setText(
                list?.getString("transafer_to")
            )
        }
        holder.txtStatus1!!.setText(
            list?.getString("status")
        )
    }

    override fun getItemCount(): Int {
        return moneyTransactionArraylist.length()
    }
}
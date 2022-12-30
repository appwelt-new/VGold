package com.cognifygroup.vgold.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.GetAllTransactionMoneyModel

class MoneyTransactionAdapter(mContext: Context, moneyTransactionArraylist:ArrayList<GetAllTransactionMoneyModel.Data>):
    RecyclerView.Adapter<MoneyTransactionAdapter.MyViewHolder>(){
    var mContext: Context
     var moneyTransactionArraylist: ArrayList<GetAllTransactionMoneyModel.Data>

    init {
        this.mContext = mContext
        this.moneyTransactionArraylist = moneyTransactionArraylist
    }



    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txtTxnIdMoney: TextView = itemView.findViewById(R.id.txtTxnIdMoney)
        val txtRupeeMoney: TextView = itemView.findViewById(R.id.txtRupeeMoney)
        val txtTimeDateMoney: TextView = itemView.findViewById(R.id.txtTimeDateMoney)
        val txtPaymentThrough: TextView = itemView.findViewById(R.id.txtPaymentThrough)
        val txtPaymentFromTo: TextView = itemView.findViewById(R.id.txtPaymentFromTo)
        val txtStatus1: TextView = itemView.findViewById(R.id.txtStatus1)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyTransactionAdapter.MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.booking_history_adapter, null)
        return MoneyTransactionAdapter.MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!moneyTransactionArraylist.get(position).transaction_id.equals("")) {
            holder.txtTxnIdMoney!!.setText(
                moneyTransactionArraylist.get(position).transaction_id
            )
        } else if (!moneyTransactionArraylist.get(position).online_transaction_id.equals("")) {
            holder.txtTxnIdMoney!!.setText(
                moneyTransactionArraylist.get(position).online_transaction_id
            )
        } else {
            holder.txtTxnIdMoney!!.setText(moneyTransactionArraylist.get(position).cheque_no)
        }
        holder.txtRupeeMoney!!.text = "Rs." + moneyTransactionArraylist.get(position).amount
        holder.txtTimeDateMoney!!.setText(
            moneyTransactionArraylist.get(position).transaction_date
        )
        holder.txtPaymentThrough!!.setText(
            moneyTransactionArraylist.get(position).payment_method
        )

        if (!moneyTransactionArraylist.get(position).received_from.equals("")) {
            holder.txtPaymentFromTo!!.setText(
                moneyTransactionArraylist.get(position).received_from
            )
        } else {
            holder.txtPaymentFromTo!!.setText(
                moneyTransactionArraylist.get(position).transafer_to
            )
        }
        holder.txtStatus1!!.setText(moneyTransactionArraylist.get(position).status)
    }

    override fun getItemCount(): Int {
        return moneyTransactionArraylist.size
    }
}
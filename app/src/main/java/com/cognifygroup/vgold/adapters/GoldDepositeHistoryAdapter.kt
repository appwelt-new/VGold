package com.cognifygroup.vgold.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.model.GoldDepositeHistoryItem
import com.cognifygroup.vgold.utilities.Constants

class GoldDepositeHistoryAdapter(branchList: List<GoldDepositeHistoryItem>) : RecyclerView.Adapter<GoldDepositeHistoryAdapter.GoldDepositHistoryViewHolder>(){

    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    private var listData: MutableList<GoldDepositeHistoryItem> = branchList as MutableList <GoldDepositeHistoryItem>

    class GoldDepositHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate1: TextView = itemView.findViewById(R.id.txtDate1)
        val gItemNo: TextView = itemView.findViewById(R.id.txtGoldBookingId1)
        val txtGoldQty1: TextView = itemView.findViewById(R.id.txtGoldQty1)
        val txtRate1: TextView = itemView.findViewById(R.id.txtRate1)
        val txtGoldMaturityweight: TextView = itemView.findViewById(R.id.txtGoldMaturityweight)
        val txtAmount1: TextView = itemView.findViewById(R.id.txtAmount1)
        val txtgold_quality: TextView = itemView.findViewById(R.id.txtgold_quality)
        val txtTenure1: TextView = itemView.findViewById(R.id.txtTenure1)
        val txtAccountStatus: TextView = itemView.findViewById(R.id.txtAccountStatus)
        val totDepoAmt: TextView = itemView.findViewById(R.id.totDepoAmt)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)
        val txtPurity: TextView = itemView.findViewById(R.id.txtPurity)
        val txtRemark: TextView = itemView.findViewById(R.id.txtRemark)
        val txtGoldClosingDate: TextView = itemView.findViewById(R.id.txtGoldClosingDate)
        val imgGoldDepositeHpdf: ImageView = itemView.findViewById(R.id.imgGoldDepositeHistory)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoldDepositHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.gold_deposit_history_item,
            parent, false
        )
        return GoldDepositHistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GoldDepositHistoryViewHolder, position: Int) {
        val currentCan = listData[position]


//        val date: String = goldBookingHistoryArrayList.get(position).getAdded_date()
        val date: String = currentCan.added_date
        val year = date.substring(0, 4)
        val month = date.substring(5, 7)
        val day = date.substring(date.length - 2, date.length)

        holder.txtDate1.text = "$day-$month-$year"
        holder.gItemNo.text = currentCan.gold_deposite_id

        holder.txtGoldQty1.text = currentCan.gold
        holder.txtRate1.text = currentCan.rate
        holder.txtGoldMaturityweight.text = currentCan.maturity_weight
        //   holder.txtBankGurantee.setText(goldBookingHistoryArrayList.get(position).getBank_guarantee());
        //   holder.txtBankGurantee.setText(goldBookingHistoryArrayList.get(position).getBank_guarantee());
        holder.txtAmount1.text = currentCan.amount
        holder.txtgold_quality.text = currentCan.gold_quality
        holder.txtTenure1.text = currentCan.tennure
        holder.totDepoAmt.text = currentCan.current_value_amount
        holder.txtStatus.text = currentCan.status_name
        holder.txtPurity.text = currentCan.addpurity
        holder.txtRemark.text = currentCan.remark
        val date2: String = currentCan.closing_date
        val year2 = date.substring(0, 4)
        val month2 = date.substring(5, 7)
        val day2 = date.substring(date.length - 2, date.length)
        holder.txtGoldClosingDate.text = "$day2-$month2-$year2"
        sharedPreferences = holder.itemView.context.getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()

        if (currentCan.account_status.equals("1")) {
            holder.txtAccountStatus.text = "Active"
            holder.txtAccountStatus.setTextColor(Color.argb(255, 0, 128, 0))
        } else if (currentCan.account_status.equals("2")) {
            holder.txtAccountStatus.text = "Matured"
            holder.txtAccountStatus.setTextColor(Color.BLUE)
        } else {
            holder.txtAccountStatus.text = ""
            holder.txtAccountStatus.setTextColor(Color.RED)
        }
        holder.imgGoldDepositeHpdf.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "http://vgold.co.in/dashboard/user/module/golddeposite/deposite_certificate.php?did=" + holder.gItemNo.text.toString() + "&&user_id=" + userId
                )
            )
            holder.itemView.context.startActivity(browserIntent)

        }


    }

    override fun getItemCount() = listData.size
}
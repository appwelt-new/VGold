package com.cognifygroup.vgold.adapters

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cognifygroup.vgold.R
import com.cognifygroup.vgold.activities.GoldTransactionHistoryActivity
import com.cognifygroup.vgold.model.GoldBookingHistoryItem
import com.cognifygroup.vgold.utilities.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class GoldBookingHistoryAdapter(branchList: List<GoldBookingHistoryItem>) :
    RecyclerView.Adapter<GoldBookingHistoryAdapter.GoldBookingHistoryViewHolder>() {

    private var userId = ""
    private lateinit var sharedPreferences: SharedPreferences
    private var listData: MutableList<GoldBookingHistoryItem> =
        branchList as MutableList<GoldBookingHistoryItem>

    class GoldBookingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goldPdf: ImageView = itemView.findViewById(R.id.imgGoldBookingHistory)
        val prgsBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val gDateTxt: TextView = itemView.findViewById(R.id.txtDate1)
        val gItemNo: TextView = itemView.findViewById(R.id.txtGoldBookingId1)
        val gItemWtQtty: TextView = itemView.findViewById(R.id.txtweight1)
        val gRatte: TextView = itemView.findViewById(R.id.txtRate1)
        val gBookVl: TextView = itemView.findViewById(R.id.txtGoldBookingValue1)
        val gBookChrg: TextView = itemView.findViewById(R.id.txtBookingCharge1)
        val gBookAmt: TextView = itemView.findViewById(R.id.txtDownPayment1)
        val gInstlm: TextView = itemView.findViewById(R.id.txtInstallment1)
        val gTenr: TextView = itemView.findViewById(R.id.txtTenure1)
        val gPaidBln: TextView = itemView.findViewById(R.id.txtPaidAmt)
        val gBalAmt: TextView = itemView.findViewById(R.id.txtBalAmt)
        val gTodGain: TextView = itemView.findViewById(R.id.txtGain)
        val gttlPdInstlment: TextView = itemView.findViewById(R.id.totPdInstlmnt)
        val gAccountStatus: TextView = itemView.findViewById(R.id.txtAccountStatus)
        val gcloseDate: TextView = itemView.findViewById(R.id.txtClosingDate)
        val layoutBookingAmount: LinearLayout = itemView.findViewById(R.id.layoutBookingAmount)
        val layoutBookingCharges: LinearLayout = itemView.findViewById(R.id.layoutBookingCharges)
        val layoutInstallment: LinearLayout = itemView.findViewById(R.id.layoutInstallment)
        val layoutTenure: LinearLayout = itemView.findViewById(R.id.layoutTenure)
        val layoutPaidAmount: LinearLayout = itemView.findViewById(R.id.layoutPaidAmount)
        val layoutBalanceAmount: LinearLayout = itemView.findViewById(R.id.layoutBalanceAmount)
        val layoutTodayGain: LinearLayout = itemView.findViewById(R.id.layoutTodayGain)
        val layoutRate: LinearLayout = itemView.findViewById(R.id.layoutRate)
        val layoutClosingDate: LinearLayout = itemView.findViewById(R.id.layoutClosingDate)
        val cardRating1: RelativeLayout = itemView.findViewById(R.id.cardRating1)


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GoldBookingHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.gold_booking_history_item,
            parent, false
        )
        return GoldBookingHistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GoldBookingHistoryViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val currentCan = listData[position]
        holder.gDateTxt.text = currentCan.gdate
        holder.gItemNo.text = currentCan.gitemno
        holder.gItemWtQtty.text = currentCan.gitemweight
        holder.gRatte.text = currentCan.grate
        holder.gBookVl.text = currentCan.gbookvlu
        holder.gBookChrg.text = currentCan.gbookcharg
        holder.gBookAmt.text = currentCan.gbookamt
        holder.gInstlm.text = currentCan.ginstalmnt
        holder.gTenr.text = currentCan.gtenur + " Month"
        holder.gPaidBln.text = currentCan.gpaidamt
        holder.gBalAmt.text = currentCan.gbalamt
        holder.gTodGain.text = currentCan.gtodaygain
        holder.gttlPdInstlment.text = currentCan.gtotPaidInstllmnt
        holder.gAccountStatus.text = currentCan.gAcStatus

        sharedPreferences =
            holder.itemView.context.getSharedPreferences(Constants.VGOLD_DB, Context.MODE_PRIVATE)
        userId = sharedPreferences.getString(Constants.VUSER_ID, null).toString()

        val date: String = listData.get(position).gdate
        val year = date.substring(0, 4)
        val month = date.substring(5, 7)
        val day = date.substring(date.length - 2, date.length)
        holder.gDateTxt.setText("$day-$month-$year")



        holder.gcloseDate.text = convertDateTimeFormat(
            currentCan.gtenur,
            holder.gDateTxt.text.toString()
        )


        if (holder.gttlPdInstlment.text.equals(null) || holder.gttlPdInstlment.text.isEmpty())
            holder.gttlPdInstlment.text = "0"

        if (holder.gItemWtQtty.text.equals(null) || holder.gItemWtQtty.text.isEmpty())
            holder.gItemWtQtty.text = "0"

        if (holder.gTenr.text.equals(null) || holder.gTenr.text.isEmpty())
            holder.gTenr.text = "0"

        // *************************************************************


        if (listData.get(position).gAcStatus.equals("1")) {
            holder.gAccountStatus.setText("Active")
            holder.gAccountStatus.setTextColor(Color.argb(255, 0, 128, 0))
            //  holder.closeLayout.setVisibility(View.GONE);
        } else if (listData.get(position).gAcStatus.equals("2")) {
            holder.gAccountStatus.setText("Matured")
            holder.gAccountStatus.setTextColor(Color.BLUE)
            //  holder.closeLayout.setVisibility(View.GONE);
        } else if (listData.get(position).gAcStatus.equals("0")) {
            holder.gAccountStatus.setText("Closed")
            holder.gAccountStatus.setTextColor(Color.RED)
            // holder.closeLayout.setVisibility(View.VISIBLE);
            // holder.layoutLogo.setVisibility(View.GONE);
            holder.gcloseDate.setVisibility(View.VISIBLE)
        }


        //********* edited by suraj *****************************************


        //********* edited by suraj *****************************************
        if (listData.get(position).gAcStatus
                .equals("1") || listData.get(position).gAcStatus
                .equals("2")
        ) {
            holder.gBookChrg.setVisibility(View.VISIBLE)
            holder.gBookAmt.setVisibility(View.VISIBLE)
            holder.gInstlm.setVisibility(View.VISIBLE)
            holder.gTenr.setVisibility(View.VISIBLE)
            holder.gPaidBln.setVisibility(View.VISIBLE)
            holder.gBalAmt.setVisibility(View.VISIBLE)
            holder.gTodGain.setVisibility(View.VISIBLE)
            holder.layoutBookingAmount.setVisibility(View.VISIBLE)
            holder.layoutBookingCharges.setVisibility(View.VISIBLE)
            holder.layoutInstallment.setVisibility(View.VISIBLE)
            holder.layoutTenure.setVisibility(View.VISIBLE)
            holder.layoutPaidAmount.setVisibility(View.VISIBLE)
            holder.layoutBalanceAmount.setVisibility(View.VISIBLE)
            holder.layoutTodayGain.setVisibility(View.VISIBLE)
            //  holder.closeLayout.setVisibility(View.VISIBLE);
            holder.layoutRate.setVisibility(View.VISIBLE)
            // holder.layoutLogo.setVisibility(View.VISIBLE)
            holder.layoutClosingDate.setVisibility(View.VISIBLE)
            //9763583584
        } else {
            holder.gBookChrg.setVisibility(View.GONE)
            holder.gBookAmt.setVisibility(View.GONE)
            holder.gInstlm.setVisibility(View.GONE)
            holder.gTenr.setVisibility(View.GONE)
            holder.gPaidBln.setVisibility(View.GONE)
            holder.gBalAmt.setVisibility(View.GONE)
            holder.gTodGain.setVisibility(View.GONE)
            holder.layoutBookingAmount.setVisibility(View.GONE)
            holder.layoutBookingCharges.setVisibility(View.GONE)
            holder.layoutInstallment.setVisibility(View.GONE)
            holder.layoutTenure.setVisibility(View.GONE)
            holder.layoutPaidAmount.setVisibility(View.GONE)
            holder.layoutBalanceAmount.setVisibility(View.GONE)
            holder.layoutTodayGain.setVisibility(View.GONE)
            //   holder.closeLayout.setVisibility(View.GONE);
            holder.layoutRate.setVisibility(View.VISIBLE)
            // holder.gBookVl.setVisibility(View.GONE)
            holder.layoutClosingDate.setVisibility(View.VISIBLE)
        }


        //*****************************************************************

        holder.prgsBar.setSecondaryProgress(holder.gttlPdInstlment.text.toString().toInt())
        holder.prgsBar.setProgress(holder.gttlPdInstlment.text.toString().toInt(), true)
        holder.prgsBar.setMax((holder.gTenr.text.toString().filter { it.isDigit() }).toInt())
        ObjectAnimator.ofInt(holder.prgsBar, "progress", 0, holder.prgsBar.secondaryProgress)
            .apply {
                duration = 3000
            }.start()


        val closingDate: String = listData.get(position).gcloseDate
        val closing_year = date.substring(0, 4)
        val closing_month = date.substring(5, 7)
        val closing_day = date.substring(date.length - 2, date.length)

  //      holder.gcloseDate.setText("$closing_day-$closing_month-$closing_year")

        //   PDF Button Code
        holder.goldPdf.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://www.vgold.co.in/dashboard/user/module/goldbooking/booking_receipt.php?bid=" + holder.gItemNo.text.toString() + "&&user_id=" + userId
                )
            )
            holder.itemView.context.startActivity(browserIntent)

        }

        holder.cardRating1.setOnClickListener {
         holder.itemView.context.startActivity(
                Intent(
                    holder.itemView.context,
                    GoldTransactionHistoryActivity::class.java
                ).putExtra(
                    "GOLD_BOOKING_ID",
                    currentCan.gitemno
                )
            )
        }
    }

    override fun getItemCount() = listData.size


    @SuppressLint("SimpleDateFormat")
    fun convertDateTimeFormat(
        tenure: String?,
        date1: String?
    ): String? {

        var add_value = 0
        if (tenure.equals("12")) {
            add_value = 1;
        } else if (tenure.equals("24")) {
            add_value = 2;
        } else if (tenure.equals("36")) {
            add_value = 3;
        } else if (tenure.equals("48")) {
            add_value = 4;
        } else if (tenure.equals("60")) {
            add_value = 5;
        } else if (tenure.equals("72")) {
            add_value = 6;
        } else if (tenure.equals("84")) {
            add_value = 7;
        } else if (tenure.equals("96")) {
            add_value = 8;
        } else if (tenure.equals("108")) {
            add_value = 9;
        } else if (tenure.equals("120")) {
            add_value = 10;
        }
        var df = SimpleDateFormat("dd-MM-yyyy")
        var date: Date? = null
        var nextDate: String? = ""
        try {
            date = date1?.let { SimpleDateFormat("dd-MM-yyyy").parse(it) }
            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }
            calendar.add(Calendar.YEAR, add_value)
           //   Log.i("TAG", "convertDateTimeFormattt: "+tenure)
            //            df.setTimeZone(TimeZone.getTimeZone("Portugal"));
            nextDate = df.format(calendar.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return nextDate
    }
}
package com.cognifygroup.vgold.utilities

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cognifygroup.vgold.interfaces.GetBookingIdModel


class ColorSpinnerAdapter(
     context: Context?,
    textViewResourceId: Int?,
    values: List<GetBookingIdModel.Data>?
) :
    ArrayAdapter<GetBookingIdModel.Data?>(context!!, textViewResourceId!!, values!!) {
    private val values: List<GetBookingIdModel.Data>

    init {
        this.values = values!!
    }

    override fun getCount(): Int {
        return super.getCount()
    }

    override fun getItem(position: Int): GetBookingIdModel.Data? {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.setText(values[position].id)
        if (values[position].is_paid != null) {
            if (values[position].is_paid === 1) label.setBackgroundColor(Color.GREEN)
        }
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        label.setText(values[position].id)
        if (values[position].is_paid != null) {
            if (values[position].is_paid == 1) label.setBackgroundColor(Color.GREEN)
        }
        return label
    }
}

package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus


import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.item_booking_status.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 25/3/19.
 */
class BookingStatusListAdapter(val responseList: BookingList, val context: Context, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(com.cloudwell.paywell.services.R.layout.item_booking_status, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return responseList.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val count = position + 1

        val model = responseList.data.get(position)

        val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
        val inputDateStr = model.firstRequestDateTime
        val date = inputFormat.parse(inputDateStr)
        val outputDateStr = outputFormat.format(date)

        holder.tvSerialNumber.setText("" + count)
        holder.tvDate.setText(model.bookingId)
        holder.tvDate.setText(outputDateStr)
        holder.tvBookingStatus.setText(model.message)
        holder.tvBookingId.setText(model.bookingId)

        if (model.message != null) {
            if (model.invoiceUrl != null) {
                holder.ivSymbolTicketed.visibility = View.VISIBLE
            } else {
                holder.ivSymbolTicketed.visibility = View.INVISIBLE
            }
        } else {
            holder.ivSymbolTicketed.visibility = View.INVISIBLE
        }

        holder.ivSymbolTicketed.setOnClickListener {
            if (model.invoiceUrl != null) {
                Logger.v("InvoiceURl: " + model.invoiceUrl)
                itemClickListener.onItemClick(model)
            }
        }

        holder.tvBookingId.setOnClickListener {
            if (model.invoiceUrl != null) {
                Logger.v("InvoiceURl: " + model.invoiceUrl)
                itemClickListener.onItemClick(model)
            }
        }

        if (position % 2 == 0)
            holder.rootLayout_booking_status.setBackgroundColor(Color.parseColor("#d8ead2"))
        else
            holder.rootLayout_booking_status.setBackgroundColor(Color.parseColor("#b0d5a4"))

    }

}


interface ItemClickListener {

    fun onItemClick(position: Datum)

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val rootLayout_booking_status = view.rootLayout_booking_status
    val tvSerialNumber = view.tvSerialNumber
    val tvBookingId = view.tvBookingId
    val tvDate = view.tvDate
    val tvBookingStatus = view.tvBookingStatus
    val tvAction = view.tvAction
    val ivSymbolTicketed = view.ivSymbolTicketed

}
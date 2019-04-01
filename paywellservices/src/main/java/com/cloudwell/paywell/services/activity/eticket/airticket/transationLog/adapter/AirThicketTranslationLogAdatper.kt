package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu
import kotlinx.android.synthetic.main.test.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 1/4/19.
 */
class AirThicketTranslationLogAdatper : RecyclerView.Adapter<ViewHolder> {


    var context: Context? = null

    private var mInflater: LayoutInflater?
    private var data: List<Datum>


    constructor(context: Context, data: List<Datum>) {
        this.context = context
        this.mInflater = LayoutInflater.from(context)
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater!!.inflate(com.cloudwell.paywell.services.R.layout.item_air_tricket_transtion_log, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val count = position + 1

        val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
        val inputDateStr = data.get(position).firstRequestDateTime
        val date = inputFormat.parse(inputDateStr)
        val outputDateStr = outputFormat.format(date)

        holder.tvSerialNumber.setText("" + count)
        holder.tvDate.setText(data.get(position).bookingId)
        holder.tvDate.setText(outputDateStr)
        holder.tvBookingStatus.setText(data.get(position).message)
        holder.tvBookingId.setText(data.get(position).bookingId)
        holder.tvAction.setText(data.get(position).totalFare)

        if (position % 2 == 0)
            holder.rootLayout_booking_status.setBackgroundColor(Color.parseColor("#d8ead2"))
        else
            holder.rootLayout_booking_status.setBackgroundColor(Color.parseColor("#b0d5a4"))


    }


}


interface ItemClickListener {

    fun onItemClick(position: Int, favoriteMenu1: FavoriteMenu)
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    // Holds the TextView that will add each animal to
    val rootLayout_booking_status = view.rootLayout_booking_status
    val tvSerialNumber = view.tvSerialNumber
    val tvBookingId = view.tvBookingId
    val tvDate = view.tvDate
    val tvBookingStatus = view.tvBookingStatus
    val tvAction = view.tvAction

}
package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTicketRepository.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.GetSeatViewRquestPojo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.RequestScheduledata
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata.ScheduleDataItem
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import kotlinx.android.synthetic.main.bus_trip_item_list.view.*
import java.text.DecimalFormat


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class BusTripListAdapter(val items: List<ScheduleDataItem>, val context: Context, val requestScheduledata : RequestScheduledata, val extraCharge:Double ,val onClickListener: OnClickListener) : RecyclerView.Adapter<ViewHolderNew>() {

    override fun onBindViewHolder(holder: ViewHolderNew, position: Int) {
        val model = items.get(position)

        val isAc = model.coachType

        holder.tvTransportNameAndType.text = (model.companyName?.toUpperCase() ?: "") + ", " + isAc
        holder.tvCoachNo.text = ": " + model.coachNo
        holder.tvDepartureTime.text = ": " + (model.departureTime)

        val prices = model.fares?.plus(extraCharge)

        holder.tvPrices.text = DecimalFormat("#").format(prices)

        if (model.resSeatInfo == null) {
            holder.tvAvailableSeat.text = ":"

            val userName = AppHandler.getmInstance(AppController.getContext()).userName

            val po = GetSeatViewRquestPojo()
            po.departureDate = requestScheduledata.departingDate
            po.fromCity = requestScheduledata.departure
            po.toCity = requestScheduledata.destination
            po.optionId = model.busServiceType+"_"+model.departureId
            po.username = userName

            holder.progressBar.visibility = View.VISIBLE
            BusTicketRepository().getSeatView(po).observeForever {
                holder.progressBar.visibility = View.INVISIBLE
                val tototalAvailableSeat = it?.tototalAvailableSeat ?: 0
                holder.tvAvailableSeat.text = ": " + tototalAvailableSeat

                if (it != null) {
                    onClickListener.onUpdateData(position, it)
                }


            }

        } else {
            holder.progressBar.visibility = View.INVISIBLE
            holder.tvAvailableSeat.text = ": " + model.resSeatInfo?.tototalAvailableSeat
        }



        holder.ivSelect.setOnClickListener {
            onClickListener.onClick(position)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNew {
        val view = LayoutInflater.from(context).inflate(R.layout.bus_trip_item_list, parent, false)
        return ViewHolderNew(view)
    }


    override fun getItemCount(): Int {
        return items.size
    }


}

interface OnClickListener {

    fun onClick(position: Int)
    fun onUpdateData(position: Int, resSeatInfo: ResSeatInfo)
}


class ViewHolderNew(view: View) : RecyclerView.ViewHolder(view) {

    val tvTransportNameAndType = view.tvTransportNameAndType
    val tvCoachNo = view.tvCoachNo
    val tvDepartureTime = view.tvDepartureTime
    val tvAvailableSeat = view.tvAvailableSeat
    val ivSelect = view.ivSelect
    val tvPrices = view.TransporttvPrices
    val progressBar = view.progressBar
}

package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.cloudwell.paywell.services.utils.BusCalculationHelper
import kotlinx.android.synthetic.main.bus_trip_item_list.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class BusTripListAdapter(val items: List<TripScheduleInfoAndBusSchedule>, val context: Context, val requestBusSearch: RequestBusSearch, val transportID: String, val onClickListener: OnClickListener) : RecyclerView.Adapter<ViewHolderNew>() {

    override fun onBindViewHolder(holder: ViewHolderNew, position: Int) {
        val model = items.get(position)

        var isAc = ""
        if (model.busLocalDB?.busIsAc.equals("1")) {
            isAc = "AC"
        } else {
            isAc = "NON AC"
        }

        holder.tvTransportNameAndType.text = (model.busLocalDB?.name?.toUpperCase()
                ?: "") + ", " + isAc
        holder.tvCoachNo.text = ": " + model.busSchedule?.coachNo
        holder.tvDepartureTime.text = ": " + (model.busSchedule?.scheduleTime ?: "")


        val prices = BusCalculationHelper.getPrices(model.busSchedule?.ticketPrice, requestBusSearch.date)
        holder.tvPrices.text = prices


        // val departureId = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.DEPARTURE_ID) as String


        val transport_id = transportID
        val route = requestBusSearch.from + "-" + requestBusSearch.to
        val bus_id = "" + (model.busLocalDB?.busID ?: "")
        val departure_id = model.busSchedule!!.schedule_time_id
        val departure_date = requestBusSearch.date
        val seat_ids = model.busSchedule?.allowedSeatNumbers ?: ""


        if (model.resSeatInfo == null) {
            holder.tvAvailableSeat.text = ": 0"

            holder.progressBar.visibility = View.VISIBLE
            BusTicketRepository().getSeatCheck(transport_id, route, bus_id, departure_id, departure_date, seat_ids).observeForever {
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

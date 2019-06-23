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
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.bus_trip_item_list.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class BusTripListAdapter(val items: List<TripScheduleInfoAndBusSchedule>, val context: Context, val requestBusSearch: RequestBusSearch, val busTicketRepository: BusTicketRepository, val onClickListener: OnClickListener) : RecyclerView.Adapter<ViewHolderNew>() {


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
        holder.tvPrices.text = ":" + model.busSchedule?.ticketPrice
        holder.tvCoachNo.text = ": " + model.busSchedule?.coachNo
        holder.tvDepartureTime.text = ": " + (model.busSchedule?.scheduleTime ?: "")
        holder.tvAvailableSeat.text = ": "


        val transportID = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.TRANSPORT_ID) as String
        val departureId = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.DEPARTURE_ID) as String


        val transport_id = transportID
        val route = requestBusSearch.to + "-" + requestBusSearch.from
        val bus_id = "" + (model.busLocalDB?.busID ?: "")
        val departure_id = departureId
        val departure_date = requestBusSearch.date
        val seat_ids = model.busSchedule?.allowedSeatNumbers ?: ""

        if (model.resSeatInfo?.tototalAvailableSeat ?: 0 == 0) {
            holder.progressBar.visibility = View.VISIBLE
            BusTicketRepository(mContext = context).getSeatCheck(transport_id, route, bus_id, departure_id, departure_date, seat_ids).observeForever {
                holder.progressBar.visibility = View.INVISIBLE
                holder.tvAvailableSeat.text = ":" + (it?.tototalAvailableSeat ?: 0)

                if (it != null) {
                    onClickListener.onUpdateData(position, it)
                }

            }
        } else {
            holder.progressBar.visibility = View.GONE

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

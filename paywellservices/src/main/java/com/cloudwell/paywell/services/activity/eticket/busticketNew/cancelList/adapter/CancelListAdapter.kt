package com.cloudwell.paywell.services.activity.eticket.busticketNew.cancelList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.model.TicketInfo

/**
 * Created by YASIN on 03,July,2019
 * Email: yasinenubd5@gmail.com
 */
class CancelListAdapter(private val list: List<TicketInfo>, val onClick: OnClick) : RecyclerView.Adapter<CancelListAdapter.TransactionLogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionLogViewHolder {
        return TransactionLogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_ticket_cancel, parent, false))
    }

    override fun onBindViewHolder(holder: TransactionLogViewHolder, position: Int) {
        val ticketInfo = list[position]
        holder.tvJounryType.text = "" + ticketInfo.journey_type
        holder.tvFromAndTo.text = "" + ticketInfo.journeyRoute
        holder.tvBusName.text = "" + ticketInfo.transportName
        holder.tvDate.text = "" + ticketInfo.departureDateTime + ""
        holder.tvSeatNo.text = "" + ticketInfo.seat
        holder.tvCounterAndTime.text = "Boarding at" + ticketInfo.boardingPoint
        holder.tvTotalPrices.text = "Total Amount: " + ticketInfo.totalAmount
        holder.tvCanceltionFee.text = "Cancellation Fee: " + ticketInfo.cancellationFee
        holder.tvBaseFare.text = "Base Fare: " + ticketInfo.basePrice
        holder.tvRetrunAmount.text = "Return Amount: " + ticketInfo.returnAmount


        holder.btCancel.setOnClickListener {
            onClick.onClick(ticketInfo)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnDialogClose {
        fun onClose()
    }

    inner class TransactionLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvJounryType: TextView
        var tvFromAndTo: TextView
        var tvBusName: TextView
        var tvDate: TextView
        var tvSeatNo: TextView
        var tvCounterAndTime: TextView
        var btCancel: Button
        var tvTotalPrices: TextView
        var tvCanceltionFee: TextView
        var tvBaseFare: TextView
        var tvRetrunAmount: TextView

        init {
            tvJounryType = itemView.findViewById(R.id.tvJounryType)
            tvFromAndTo = itemView.findViewById(R.id.tvFromAndTo)
            tvBusName = itemView.findViewById(R.id.tvBusName)
            tvDate = itemView.findViewById(R.id.tvDate)
            tvSeatNo = itemView.findViewById(R.id.tvSeatNo)
            tvCounterAndTime = itemView.findViewById(R.id.tvCounterAndTime)
            btCancel = itemView.findViewById(R.id.btCancel)
            tvTotalPrices = itemView.findViewById(R.id.tvTotalPrices)
            tvCanceltionFee = itemView.findViewById(R.id.tvCancelationFee)
            tvBaseFare = itemView.findViewById(R.id.tvBaseFare)
            tvRetrunAmount = itemView.findViewById(R.id.tvRetrunAmount)
        }
    }

    interface OnClick {
        fun onClick(ticketInfo: TicketInfo)
    }

}
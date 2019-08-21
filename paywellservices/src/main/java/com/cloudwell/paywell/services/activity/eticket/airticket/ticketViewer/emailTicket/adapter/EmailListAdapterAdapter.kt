package com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.emailTicket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Passenger
import kotlinx.android.synthetic.main.item_mail_list.view.*


class EmailListAdapterAdapter(var passengers: MutableList<Passenger>, val context: Context?, var itemClickListener: ItemClickListener) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return passengers.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(com.cloudwell.paywell.services.R.layout.item_mail_list, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val m = passengers.get(position) as Passenger
        holder.tvEmail.text = "" + m.email

        if (m.isCheckEmail == true) {
            holder.isEmailCheck.isChecked = true
        } else {
            holder.isEmailCheck.isChecked = false
        }


        holder.isEmailCheck.setOnClickListener {
            val checked = holder.isEmailCheck.isChecked
            m.isCheckEmail = checked
            val passenger = m as Passenger
            passengers.set(position, passenger)
        }

        if (m.isDefalt) {
            holder.isEmailCheck.visibility = View.INVISIBLE
            holder.tvEmail.visibility = View.INVISIBLE

            holder.ivAdd.visibility = View.VISIBLE
            holder.tvAddEmail.visibility = View.VISIBLE
        } else {
            holder.isEmailCheck.visibility = View.VISIBLE
            holder.tvEmail.visibility = View.VISIBLE

            holder.ivAdd.visibility = View.INVISIBLE
            holder.tvAddEmail.visibility = View.INVISIBLE
        }

        holder.tvAddEmail.setOnClickListener {
            itemClickListener.onAddClick()
        }

        holder.ivAdd.setOnClickListener {
            itemClickListener.onAddClick()
        }

        holder.isEmailCheck.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                itemClickListener.onStateChange(position, b)
            } else {

            }

        }

    }

    interface ItemClickListener {
        fun onAddClick()
        fun onStateChange(position: Int, b: Boolean);
    }

}


class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val rootLayout = view.rootLayout_booking_status
    val tvEmail = view.tvEmail
    val isEmailCheck = view.isEmailCheck

    val ivAdd = view.ivAdd
    val tvAddEmail = view.tvAddEmail
}

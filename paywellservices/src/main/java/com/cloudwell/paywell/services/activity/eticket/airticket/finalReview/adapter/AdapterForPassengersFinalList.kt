package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import kotlinx.android.synthetic.main.passenger_list_item_defalt_edit.view.*

class AdapterForPassengersFinalList(var context: Context, var items: List<Passenger>, private var onClickListener: OnClickListener) : RecyclerView.Adapter<AdapterForPassengersFinalList.ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterForPassengersFinalList.ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.passenger_list_item_final, parent, false)
        return AdapterForPassengersFinalList.ViewHolder(view)


    }


    override fun onBindViewHolder(holder: AdapterForPassengersFinalList.ViewHolder, position: Int) {
        val model = items.get(position)
        holder.tvShortFirstNameLastName.text = "${model.firstName} / ${model.lastName}"

        holder.ivEdit.setOnClickListener {

            onClickListener.onEditClick(model, position)
        }


    }

    public fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvShortFirstNameLastName = view.tvShortFirstNameLastName
        val ivEdit = view.ivEdit


    }

    interface OnClickListener {
        fun onEditClick(model: Passenger, position: Int)

        fun onDeleted(model: Passenger, position: Int)
    }

}

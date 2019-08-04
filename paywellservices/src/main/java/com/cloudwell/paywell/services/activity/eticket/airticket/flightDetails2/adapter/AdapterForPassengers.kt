package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import kotlinx.android.synthetic.main.flight_list_item.view.*
import kotlinx.android.synthetic.main.passenger_list_item_defalt_add.view.*
import kotlinx.android.synthetic.main.passenger_list_item_with_title.view.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/3/19.
 */
class AdapterForPassengers(var context: Context, var items: List<Passenger>) : RecyclerView.Adapter<AdapterForPassengers.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {

        if (position == items.size - 1) {
            return 0
        } else {
            return 1
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        when (viewType) {
            0 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.passenger_list_item_defalt_add, parent, false)
                return ViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(com.cloudwell.paywell.services.R.layout.passenger_list_item_with_title, parent, false)
                return ViewHolder(view)
            }
        }

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items.get(position)

        when (holder.itemViewType) {
            0 -> {

                if (model.isDefault) {
                    holder.ivAddPassenger.visibility = View.VISIBLE
                    holder.tvAddEdit.visibility = View.VISIBLE
                } else {
                    holder.ivAddPassenger.visibility = View.INVISIBLE
                    holder.tvAddEdit.visibility = View.INVISIBLE
                }
            }
            else -> {

                holder.tvFirstNameLastName.text = model.firstName + "/" + model.lastName
                holder.tvPassportNumber.text = model.passportNumber


                if (!model.passportNumber.equals("")) {
                    holder.tvPassportNumber.text = model.passportNumber
                }

                if (!model.nIDnumber.equals("")) {
                    holder.tvPassportNumber.text = model.nIDnumber
                }

                if (model.isPassengerSleted) {
                    holder.ivIsSeleted.visibility = View.VISIBLE
                } else {
                    holder.ivIsSeleted.visibility = View.INVISIBLE
                }

            }
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStringTime = view.tvStringTime
        val ivAddPassenger = view.ivAddPassenger
        val tvAddEdit = view.tvShortFirstNameLastName

        var layoutPassengerInfo = view.layoutPassengerInfo
        val tvFirstNameLastName = view.tvFirstNameAndLastName
        val tvPassportNumber = view.tvPassportNumber
        val ivIsSeleted = view.ivIsSeleted

    }


}
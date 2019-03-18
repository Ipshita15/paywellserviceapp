package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import kotlinx.android.synthetic.main.passenger_list_item_final.view.*

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

        var number = position + 1
        holder.tvShortFirstNameLastName.text = "Passenger No: " + number

        holder.tvPassengerType.text = "Passenger type: " + model.paxType

        if (model.isLeadPassenger) {
            holder.tvLeadPassenger.text = "Lead passenger: Yes"
        } else {
            holder.tvLeadPassenger.text = "Lead passenger: No"
        }

        holder.tvTitle.text = "Title: " + model.title
        holder.tvFirstName.text = "First Name: " + model.firstName
        holder.tvLastName.text = "Last Name: " + model.lastName
        holder.tvCountry.text = "Country: " + model.country
        holder.tvGender.text = "Gender: " + model.gender
        holder.tvContactNumber.text = "Contact Number: " + model.contactNumber
        holder.tvEmailAddress.text = "Email: " + model.email

        if (model.passportNumber.equals("")) {
            holder.tvPassport.visibility = View.GONE
        } else {
            holder.tvPassport.visibility = View.GONE
            holder.tvPassport.text = "Passport ID: " + model.passportNumber
        }

        if (model.nIDnumber.equals("")) {
            holder.tvNid.visibility = View.GONE
        } else {
            holder.tvNid.visibility = View.GONE
            holder.tvNid.text = "National ID: " + model.nIDnumber
        }

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
        val tvPassengerType = view.tvPassengerTypeFinal
        val tvLeadPassenger = view.tvLeadPassenger
        val tvTitle = view.tvTitle
        val tvFirstName = view.tvFirstName
        val tvLastName = view.tvLastName
        val tvCountry = view.tvCountry
        val tvGender = view.tvGender
        val tvContactNumber = view.tvContactNumber
        val tvEmailAddress = view.tvEmailAddress
        val tvPassport = view.tvPasswordFinal
        val tvNid = view.tvNid


    }

    interface OnClickListener {
        fun onEditClick(model: Passenger, position: Int)

        fun onDeleted(model: Passenger, position: Int)
    }

}

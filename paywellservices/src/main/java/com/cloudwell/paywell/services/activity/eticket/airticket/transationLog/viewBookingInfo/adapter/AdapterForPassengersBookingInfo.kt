package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.viewBookingInfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Passenger
import com.cloudwell.paywell.services.utils.CountryUtility
import kotlinx.android.synthetic.main.fragment_bus_ticket_confirm_successfull_fragment.view.*
import kotlinx.android.synthetic.main.passenger_list_item_final.view.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-05-13.
 */
class AdapterForPassengersBookingInfo(var context: Context, var items: List<Passenger>) : RecyclerView.Adapter<AdapterForPassengersBookingInfo.ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.passenger_list_item_book_info, parent, false)
        return ViewHolder(view)


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items.get(position)

        val number = position + 1
        holder.tvShortFirstNameLastName.text = "Passenger No: " + number


        val paxType = model.paxType
        holder.tvPassengerType.text = "Passenger type: " + paxType

        holder.tvTitle.text = "Title: " + model.nameTitle
        holder.tvFirstName.text = "First Name: " + model.firstName
        holder.tvLastName.text = "Last Name: " + model.lastName

        val countryCode1 = CountryUtility.getCountryCode(model.countryCode)


        holder.tvCountry.text = "Country: " + countryCode1
        holder.tvGender.text = "Gender: " + model.gender
        holder.tvContactNumber.text = "Contact Number: " + model.contactNumber
        holder.tvEmailAddress.text = "Email: " + model.email
        holder.tvDateOfBirth.text = "Date Of Birth: " + model.dateOfBirth.toString().split(" ").get(0)



        if (model.passportNumber == null || model.passportNumber.equals("")) {
            holder.tvPassport.visibility = View.GONE
        } else {
            holder.tvPassport.visibility = View.VISIBLE
            holder.tvPassport.text = "Passport ID: " + model.passportNumber
        }

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
        val tvGender = view.tvBoardingPointName
        val tvDateOfBirth = view.tvDateOfBirth
        val tvContactNumber = view.tvContactNumber
        val tvEmailAddress = view.tvEmailAddress
        val tvPassport = view.tvPasswordFinal
        val tvNid = view.tvNid
        val tvPassportExpiryDate = view.tvPassportExpiryDate
        val tvPassportNationallity = view.tvPassportNationallity


    }

    interface OnClickListener {
        fun onEditClick(model: Passenger, position: Int)

        fun onDeleted(model: Passenger, position: Int)
    }

}

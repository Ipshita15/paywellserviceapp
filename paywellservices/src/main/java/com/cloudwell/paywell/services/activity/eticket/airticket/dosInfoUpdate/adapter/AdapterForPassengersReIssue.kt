package com.cloudwell.paywell.services.activity.eticket.airticket.dosInfoUpdate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Passenger
import com.cloudwell.paywell.services.utils.CountryUtility
import kotlinx.android.synthetic.main.passenger_list_item_final.view.*
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvContactNumber
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvCountry
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvDateOfBirth
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvEmailAddress
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvFirstName
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvGender
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvLastName
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvPassengerTypeFinal
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvPasswordFinal
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvShortFirstNameLastName
import kotlinx.android.synthetic.main.passenger_list_item_final.view.tvTitle
import kotlinx.android.synthetic.main.passenger_list_item_reissue.view.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-05-13.
 */
class AdapterForPassengersReIssue(var context: Context, var items: List<Passenger>, val onClickListener: OnClickListener) : RecyclerView.Adapter<AdapterForPassengersReIssue.ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.passenger_list_item_reissue, parent, false)
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

        holder.ivEditPassenger.setOnClickListener {
            onClickListener.onEditClick(model, position)

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
        val tvGender = view.tvGender
        val tvDateOfBirth = view.tvDateOfBirth
        val tvContactNumber = view.tvContactNumber
        val tvEmailAddress = view.tvEmailAddress
        val tvPassport = view.tvPasswordFinal
        val tvNid = view.tvNid
        val tvPassportExpiryDate = view.tvPassportExpiryDate
        val tvPassportNationallity = view.tvPassportNationallity
        val ivEditPassenger = view.ivEditPassenger


    }

    interface OnClickListener {
        fun onEditClick(model: Passenger, position: Int)
        fun onDeleted(model: Passenger, position: Int)
    }

}

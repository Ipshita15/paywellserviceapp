package com.cloudwell.paywell.services.activity.eticket.airticket


import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.SearchViewActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.AirportsSearchActivity
import kotlinx.android.synthetic.main.fragment_one_way.*
import mehdi.sakout.fancybuttons.FancyButton
import java.text.SimpleDateFormat
import java.util.*


class OneWayFragment : Fragment(), View.OnClickListener {

    companion object {
        val KEY_REQUEST_KEY = "KEY_REQUEST_KEY"
        val KEY_REQUEST_FOR_FROM = 1
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(com.cloudwell.paywell.services.R.layout.fragment_one_way, container, false)

        val tvDepart = view.findViewById<TextView>(com.cloudwell.paywell.services.R.id.tvDepart)
        val tvDepartDate = view.findViewById<TextView>(com.cloudwell.paywell.services.R.id.tvDepartDate)
        val airTicketClass = view.findViewById<TextView>(com.cloudwell.paywell.services.R.id.airTicketClass)
        val llPassenger = view.findViewById<LinearLayout>(com.cloudwell.paywell.services.R.id.llPsngr)
        val btnSearch = view.findViewById<FancyButton>(com.cloudwell.paywell.services.R.id.btn_search)
        val tvFrom = view.findViewById<LinearLayout>(com.cloudwell.paywell.services.R.id.tvFrom)


        tvDepart.setOnClickListener(this)
        tvDepartDate.setOnClickListener(this)
        airTicketClass.setOnClickListener(this)
        llPassenger.setOnClickListener(this)
        btnSearch.setOnClickListener(this)
        tvFrom.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            com.cloudwell.paywell.services.R.id.tvDepart -> {
                showDepartDatePicker()
            }

            com.cloudwell.paywell.services.R.id.tvDepartDate -> {
                showDepartDatePicker()
            }


            com.cloudwell.paywell.services.R.id.airTicketClass -> {

                handleClass()

            }

            R.id.llPsngr -> {

                handlePassengerClick()
            }

            R.id.btn_search -> {

                val intent = Intent(activity?.applicationContext, SearchViewActivity::class.java)
                startActivity(intent)
            }

            R.id.tvFrom -> {

                val intent = Intent(activity?.applicationContext, AirportsSearchActivity::class.java)
                intent.putExtra(KEY_REQUEST_KEY, KEY_REQUEST_FOR_FROM)
                startActivity(intent)
            }
        }
    }

    private fun handlePassengerClick() {
        val b = Bundle()
        b.putString("myAdult", airTicketAdult.text.toString())
        b.putString("myKid", airTicketKid.text.toString())
        b.putString("myInfant", airTicketInfant.text.toString())

        val passengerBottomSheet = PassengerBottomSheetDialog()
        passengerBottomSheet.setmListenerPsngr(object : PassengerBottomSheetDialog.PsngrBottomSheetListener {
            override fun onInfantButtonClickListener(text: String) {
                onAdultPsngrTextChange(text)
            }

            override fun onKidButtonClickListener(text: String) {
                onKidPsngrTextChange(text)

            }

            override fun onAdultButtonClickListener(text: String) {
                onInfantPsngrTextChange(text)

            }

        })
        passengerBottomSheet.arguments = b
        passengerBottomSheet.show(fragmentManager, "psngrBottomSheet")
    }


    fun onAdultPsngrTextChange(text: String) {
        airTicketAdult.setText(text)
    }

    fun onKidPsngrTextChange(text: String) {
        airTicketKid.setText(text)
    }

    fun onInfantPsngrTextChange(text: String) {
        airTicketInfant.setText(text)
    }

    private fun handleClass() {
        val b = Bundle()
        b.putString("myClassName", airTicketClass.text.toString())

        val bottomSheet = ClassBottomSheetDialog()
        bottomSheet.setOnClassListener(object : ClassBottomSheetDialog.ClassBottomSheetListener {
            override fun onButtonClickListener(text: String) {

                airTicketClass.setText(text)
            }

        })

        bottomSheet.arguments = b
        bottomSheet.show(fragmentManager, "classBottomSheet")
    }

    private fun showDepartDatePicker() {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val thismonth = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->

                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    val date = calendar.getTime()

                    val nameOfDayOfWeek = SimpleDateFormat("EEE").format(date)
                    val nameOfMonth = SimpleDateFormat("MMM").format(calendar.getTime())

                    tvDepartDate.text = "$nameOfDayOfWeek, $day $nameOfMonth"
                    tvDepart.setTextColor(Color.BLACK);


                }, year, thismonth, dayOfMonth)

        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        calendar.add(Calendar.MONTH, 6)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis


        datePickerDialog.show()

    }

    fun onClassTextChange(text: String) {
        airTicketClass.setText(text)

    }


}

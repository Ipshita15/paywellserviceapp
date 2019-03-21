package com.cloudwell.paywell.services.activity.eticket.airticket

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.FlightSearchViewActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.AirportsSearchActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.RequestAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.Segment
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.customView.multipDatePicker.SlyCalendarDialog
import kotlinx.android.synthetic.main.fragment_round_trip.*
import kotlinx.android.synthetic.main.fragment_round_trip.view.*
import java.text.SimpleDateFormat
import java.util.*


class RoundTripFragment : Fragment(), View.OnClickListener, SlyCalendarDialog.Callback {


    private lateinit var fromAirport: Airport
    private lateinit var toAirport: Airport
    var mClassModel = ClassModel("Economy", "Economy", true)

    var humanReadAbleDateFirst: String = ""
    var humanReadAbleDateSecond: String = ""


    lateinit var tvClass: TextView
    lateinit var tvAdult: TextView
    lateinit var tvKid: TextView
    lateinit var tvInfant: TextView
    lateinit var llPassenger: LinearLayout

    private val REQ_CODE_FROM = 1
    private val REQ_CODE_TO = 3

    companion object {
        val KEY_REQUEST_KEY = "KEY_REQUEST_KEY"
        val KEY_REQUEST_FOR_FROM = 1
        val KEY_FROM = "From"
        val KEY_To = "To"
        val KEY_AIRPORT = "Airport"
    }

    private lateinit var searchRoundTripModel: SearchRoundTripModel


    override fun onDataSelected(firstDate: Calendar?, secondDate: Calendar?, hours: Int, minutes: Int) {
        if (firstDate != null && secondDate != null) {

            val nameOfDayOfWeekFirst = SimpleDateFormat("EEE").format(firstDate.time)
            val nameOfMonthFirst = SimpleDateFormat("MMM").format(firstDate.time)
            val dayFirst = SimpleDateFormat("dd").format(firstDate.time)

            tvDepartDate.text = "$nameOfDayOfWeekFirst, $dayFirst $nameOfMonthFirst"
            tvDepart1.setTextColor(Color.BLACK);

            humanReadAbleDateFirst = SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH).format(firstDate.time)


            val nameOfDayOfWeekSecound = SimpleDateFormat("EEE").format(secondDate.time)
            val nameOfMonthSecound = SimpleDateFormat("MMM").format(secondDate.time)
            val daySecound = SimpleDateFormat("dd").format(secondDate.time)

            tvDepartDate2.text = "$nameOfDayOfWeekSecound, $daySecound $nameOfMonthSecound"
            tvDepart2.setTextColor(Color.BLACK);

            humanReadAbleDateSecond = SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH).format(secondDate.time)

        }

    }

    override fun onCancelled() {
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            com.cloudwell.paywell.services.R.id.tvFrom -> {

                val intent = Intent(context, AirportsSearchActivity::class.java)
                intent.putExtra("from", 1)
                intent.putExtra("isTo", false)
                startActivityForResult(intent, REQ_CODE_FROM)
            }

            com.cloudwell.paywell.services.R.id.layoutTo -> {

                val intent = Intent(context, AirportsSearchActivity::class.java)
                intent.putExtra("from", 1)
                intent.putExtra("isTo", true)
                startActivityForResult(intent, REQ_CODE_TO)
            }

            com.cloudwell.paywell.services.R.id.layoutDepart -> {


                val callback = com.cloudwell.paywell.services.customView.multipDatePicker.SlyCalendarDialog()
                        .setSingle(false)
                        .setCallback(this)


                callback.show(activity?.supportFragmentManager, "TAG_SLYCALENDAR")


            }

            R.id.btn_search -> {

                handleSearchClick()
            }
        }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_round_trip, container, false)

        val tsFrom = view.findViewById<TextSwitcher>(com.cloudwell.paywell.services.R.id.tsRoundTripFrom)
        val tsFromPort = view.findViewById<TextSwitcher>(com.cloudwell.paywell.services.R.id.tsRoundTripFromPort)
        val tsTo = view.findViewById<TextSwitcher>(com.cloudwell.paywell.services.R.id.tsRoundTripTo)
        val tsToPort = view.findViewById<TextSwitcher>(com.cloudwell.paywell.services.R.id.tsRoundTripToPort)
        val ivSwitchTrip = view.findViewById<ImageView>(com.cloudwell.paywell.services.R.id.ivRoundTripTextSwitcher)

        val tvFrom = view.findViewById<LinearLayout>(com.cloudwell.paywell.services.R.id.tvFrom)
        val layoutTo = view.findViewById<LinearLayout>(com.cloudwell.paywell.services.R.id.layoutTo)
        val layoutDepart = view.findViewById<LinearLayout>(com.cloudwell.paywell.services.R.id.layoutDepart)

        tvClass = view.findViewById(com.cloudwell.paywell.services.R.id.airTicketClass)
        llPassenger = view.findViewById(com.cloudwell.paywell.services.R.id.llPsngr)
        tvAdult = view.findViewById(com.cloudwell.paywell.services.R.id.airTicketAdult)
        tvKid = view.findViewById(com.cloudwell.paywell.services.R.id.airTicketKid)
        tvInfant = view.findViewById(com.cloudwell.paywell.services.R.id.airTicketInfant)



        tvFrom.setOnClickListener(this)
        layoutTo.setOnClickListener(this)
        layoutDepart.setOnClickListener(this)


        llPassenger.setOnClickListener(this)


        view.btn_search.setOnClickListener(this)



        tsFrom.setFactory {
            TextView(ContextThemeWrapper(context,
                    com.cloudwell.paywell.services.R.style.TicketFrom), null, 0)
        }
        tsFromPort.setFactory {
            TextView(ContextThemeWrapper(context,
                    com.cloudwell.paywell.services.R.style.TicketFromPort), null, 0)
        }
        tsTo.setFactory {
            TextView(ContextThemeWrapper(context,
                    com.cloudwell.paywell.services.R.style.TicketTo), null, 0)
        }
        tsToPort.setFactory {
            TextView(ContextThemeWrapper(context,
                    com.cloudwell.paywell.services.R.style.TicketToPort), null, 0)
        }
        val inAnim = AnimationUtils.loadAnimation(context,
                android.R.anim.fade_in)
        val outAnim = AnimationUtils.loadAnimation(context,
                android.R.anim.fade_out)
        inAnim.duration = 200
        outAnim.duration = 200
        tsFrom.inAnimation = inAnim
        tsFrom.outAnimation = outAnim
        tsFromPort.inAnimation = inAnim
        tsFromPort.outAnimation = outAnim
        tsTo.inAnimation = inAnim
        tsTo.outAnimation = outAnim
        tsToPort.inAnimation = inAnim
        tsToPort.outAnimation = outAnim

        tsFrom.setCurrentText(activity?.application?.getString(R.string.from))
        tsFromPort.setCurrentText(activity?.application?.getString(R.string.airport))
        view.tvHitFrom.visibility = View.INVISIBLE


        tsTo.setCurrentText(activity?.application?.getString(R.string.to))
        tsToPort.setCurrentText(activity?.application?.getString(R.string.airport))
        view.tvHitTo.visibility = View.INVISIBLE

        val textFrom = tsFrom.currentView as TextView
        val textFromPort = tsFromPort.currentView as TextView
        val textTo = tsTo.currentView as TextView
        val textToPort = tsToPort.currentView as TextView



        searchRoundTripModel = SearchRoundTripModel(textFrom.text.toString(), textTo.text.toString(), textFromPort.text.toString(), textToPort.text.toString())

        ivSwitchTrip.setOnClickListener {
            tsFrom.setText(searchRoundTripModel.getToName())
            tsTo.setText(searchRoundTripModel.getFromName())
            val from = searchRoundTripModel.getFromName()
            searchRoundTripModel.setFromName(searchRoundTripModel.getToName())
            searchRoundTripModel.setToName(from)

            tsFromPort.setText(searchRoundTripModel.getToPortName())
            tsToPort.setText(searchRoundTripModel.getFromPortName())

            val fromPort = searchRoundTripModel.getFromPortName()
            searchRoundTripModel.setFromPortName(searchRoundTripModel.getToPortName())
            searchRoundTripModel.setToPortName(fromPort)

        }

        tvClass.setOnClickListener {


            handleClass()
        }

        llPassenger.setOnClickListener {

            handlePassengerClick()
        }



        return view
    }

    private fun handleClass() {
        val b = Bundle()
        b.putString("myClassName", airTicketClass.text.toString())

        val bottomSheet = ClassBottomSheetDialog()
        bottomSheet.setOnClassListener(object : ClassBottomSheetDialog.ClassBottomSheetListener {
            override fun onButtonClickListener(classModel: ClassModel) {

                mClassModel = classModel

                airTicketClass.setText(classModel.className)
            }

        })

        bottomSheet.arguments = b
        bottomSheet.show(fragmentManager, "classBottomSheet")
    }


    private fun handlePassengerClick() {
        val b = Bundle()
        b.putString("myAdult", airTicketAdult.text.toString())
        b.putString("myKid", airTicketKid.text.toString())
        b.putString("myInfant", airTicketInfant.text.toString())

        val passengerBottomSheet = PassengerBottomSheetDialog()
        passengerBottomSheet.setmListenerPsngr(object : PassengerBottomSheetDialog.PsngrBottomSheetListener {
            override fun onInfantButtonClickListener(text: String) {
                onInfantPsngrTextChange(text)

            }

            override fun onKidButtonClickListener(text: String) {
                onKidPsngrTextChange(text)

            }

            override fun onAdultButtonClickListener(text: String) {
                onAdultPsngrTextChange(text)

            }

        })
        passengerBottomSheet.arguments = b
        passengerBottomSheet.show(fragmentManager, "psngrBottomSheet")
    }

    fun onClassTextChange(text: String) {
        tvClass.setText(text)
    }

    fun onAdultPsngrTextChange(text: String) {
        tvAdult.setText(text)
    }

    fun onKidPsngrTextChange(text: String) {
        tvKid.setText(text)
    }

    fun onInfantPsngrTextChange(text: String) {
        tvInfant.setText(text)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val get = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.AIRPORT) as Airport

            when (requestCode) {
                REQ_CODE_FROM -> {

                    fromAirport = get

                    searchRoundTripModel.setFromName(get.iata)
                    searchRoundTripModel.setFromPortName(get.airportName)

                    tsRoundTripFrom.setText(get.iata)
                    tsRoundTripFromPort.setText(get.airportName)
                    tvHitFrom.visibility = View.VISIBLE
                }

                REQ_CODE_TO -> {

                    toAirport = get

                    searchRoundTripModel.setToName(get.iata)
                    searchRoundTripModel.setToPortName(get.airportName)

                    tsRoundTripTo.setText(get.iata)
                    tsRoundTripToPort.setText(get.airportName)

                    tvHitTo.visibility = View.VISIBLE
                }


            }
        }
    }

    private fun handleSearchClick() {
        if (searchRoundTripModel.getFromName().equals(OneWayFragment.KEY_FROM)) {
            Toast.makeText(activity?.applicationContext, "Please select from airport", Toast.LENGTH_LONG).show()

            return
        }

        if (searchRoundTripModel.getToName().equals(OneWayFragment.KEY_To)) {
            Toast.makeText(activity?.applicationContext, "Please select arrival airport", Toast.LENGTH_LONG).show()
            return
        }

        if (humanReadAbleDateFirst.equals("")) {
            Toast.makeText(activity?.applicationContext, "Please select depart date", Toast.LENGTH_LONG).show()
            return
        }

        if (humanReadAbleDateSecond.equals("")) {
            Toast.makeText(activity?.applicationContext, "Please select return date", Toast.LENGTH_LONG).show()
            return
        }


        val list = mutableListOf<Segment>()
        val segment1 = Segment(mClassModel.apiClassName, humanReadAbleDateFirst, toAirport.iata, fromAirport.iata)
        val segment2 = Segment(mClassModel.apiClassName, humanReadAbleDateSecond, fromAirport.iata, toAirport.iata)

        list.add(segment1)
        list.add(segment2)

        val requestAirSearch = RequestAirSearch(airTicketAdult.text.toString().toLong(), airTicketKid.text.toString().toLong(), airTicketInfant.text.toString().toLong(), "Return", list)


        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.REQUEST_AIR_SERACH, requestAirSearch)


        val intent = Intent(activity?.applicationContext, FlightSearchViewActivity::class.java)
        startActivity(intent)

    }
}

package com.cloudwell.paywell.services.activity.eticket.airticket

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.RequestAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Segment
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.AirportsSearchActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.FlightSearchViewActivity
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.customView.multipDatePicker.SlyCalendarDialog
import kotlinx.android.synthetic.main.fragment_round_trip.*
import kotlinx.android.synthetic.main.fragment_round_trip.view.*
import java.text.SimpleDateFormat
import java.util.*


class RoundTripFragment : Fragment(), View.OnClickListener, SlyCalendarDialog.Callback {


    private lateinit var fromAirport: Airport
    private lateinit var toAirport: Airport


    var humanReadAbleDateFirst: String = ""
    var humanReadAbleDateSecond: String = ""


    lateinit var tvClass: TextView
    lateinit var tvAdult: TextView
    lateinit var tvKid: TextView
    lateinit var tvInfant: TextView
    lateinit var llPassenger: LinearLayout

    private val REQ_CODE_FROM = 1
    private val REQ_CODE_TO = 3


    lateinit var mClassModel: ClassModel

    lateinit var airTicketAdult: TextView
    lateinit var airTicketKid: TextView
    lateinit var airTicketInfant: TextView

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
            tvDepartDate.setTextColor(Color.BLACK);

            humanReadAbleDateFirst = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(firstDate.time)


            AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE, humanReadAbleDateFirst)


            val nameOfDayOfWeekSecound = SimpleDateFormat("EEE").format(secondDate.time)
            val nameOfMonthSecound = SimpleDateFormat("MMM").format(secondDate.time)
            val daySecound = SimpleDateFormat("dd").format(secondDate.time)

            tvDepartDate2.text = "$nameOfDayOfWeekSecound, $daySecound $nameOfMonthSecound"
            tvDepartDate2.setTextColor(Color.BLACK);

            humanReadAbleDateSecond = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(secondDate.time)

            AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE, humanReadAbleDateSecond)

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

            com.cloudwell.paywell.services.R.id.btn_search -> {

                handleSearchClick()
            }
        }


    }


    private var tsFrom: TextSwitcher? = null

    private var tsFromPort: TextSwitcher? = null

    private var tsTo: TextSwitcher? = null


    private var tsToPort: TextSwitcher? = null

    private var ivSwitchTrip: ImageView? = null
    private var tvFrom: LinearLayout? = null
    private var layoutTo: LinearLayout? = null
    private var layoutDepart: LinearLayout? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_round_trip, container, false)

        tsFrom = view.findViewById<TextSwitcher>(R.id.tsRoundTripFrom)
        tsFromPort = view.findViewById<TextSwitcher>(R.id.tsRoundTripFromPort)
        tsTo = view.findViewById<TextSwitcher>(R.id.tsRoundTripTo)
        tsToPort = view.findViewById<TextSwitcher>(R.id.tsRoundTripToPort)
        ivSwitchTrip = view.findViewById<ImageView>(R.id.ivRoundTripTextSwitcher)

        tvFrom = view.findViewById<LinearLayout>(R.id.tvFrom)
        layoutTo = view.findViewById<LinearLayout>(R.id.layoutTo)
        layoutDepart = view.findViewById<LinearLayout>(R.id.layoutDepart)



        inilitzationView(view)


        return view
    }

    private fun inilitzationView(view: View) {


        tvClass = view.findViewById(R.id.airTicketClass)
        llPassenger = view.findViewById(R.id.llPsngr)
        tvAdult = view.findViewById(R.id.airTicketAdult)
        tvKid = view.findViewById(R.id.airTicketKid)
        tvInfant = view.findViewById(R.id.airTicketInfant)


        airTicketInfant = view.findViewById<TextView>(R.id.airTicketInfant)
        airTicketAdult = view.findViewById<TextView>(R.id.airTicketAdult)
        airTicketKid = view.findViewById<TextView>(R.id.airTicketKid)


        tvFrom?.setOnClickListener(this)
        layoutTo?.setOnClickListener(this)
        layoutDepart?.setOnClickListener(this)


        llPassenger.setOnClickListener(this)


        view.btn_search.setOnClickListener(this)


        tsFrom?.removeAllViews()
        tsFrom?.setFactory {
            TextView(ContextThemeWrapper(context, R.style.TicketFrom), null, 0)
        }
        tsFromPort?.removeAllViews()
        tsFromPort?.setFactory {
            TextView(ContextThemeWrapper(context, R.style.TicketFromPort), null, 0)
        }
        tsTo?.removeAllViews()
        tsTo?.setFactory {
            TextView(ContextThemeWrapper(context, R.style.TicketTo), null, 0)
        }
        tsToPort?.removeAllViews()
        tsToPort?.setFactory {
            TextView(ContextThemeWrapper(context, R.style.TicketToPort), null, 0)
        }
        val inAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        val outAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        inAnim.duration = 200
        outAnim.duration = 200
        tsFrom!!.inAnimation = inAnim
        tsFrom!!.outAnimation = outAnim
        tsFromPort!!.inAnimation = inAnim
        tsFromPort!!.outAnimation = outAnim
        tsTo!!.inAnimation = inAnim
        tsTo!!.outAnimation = outAnim
        tsToPort!!.inAnimation = inAnim
        tsToPort!!.outAnimation = outAnim

        val textFrom = tsFrom!!.currentView as TextView
        val textFromPort = tsFromPort!!.currentView as TextView
        val textTo = tsTo!!.currentView as TextView
        val textToPort = tsToPort!!.currentView as TextView



        searchRoundTripModel = SearchRoundTripModel(textFrom.text.toString(), textTo.text.toString(), textFromPort.text.toString(), textToPort.text.toString())

        ivSwitchTrip?.setOnClickListener {


            searchRoundTripModel.setFromName(textFrom.text.toString())
            searchRoundTripModel.setFromPortName(textFromPort.text.toString())

            searchRoundTripModel.setToName(textTo.text.toString())
            searchRoundTripModel.setToPortName(textToPort.text.toString())


            // swift view
            tsFrom!!.setText(searchRoundTripModel.getToName())
            tsTo!!.setText(searchRoundTripModel.getFromName())

            tsFromPort!!.setText(searchRoundTripModel.getToPortName())
            tsToPort!!.setText(searchRoundTripModel.getFromPortName())

            // swif object value


//

//
            val fromAirTricket = Airport()
//            fromAirTricket.airportName = searchRoundTripModel.fromPort
//            fromAirTricket.iata = searchRoundTripModel.getFromName()
//
//            AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.FROM_CACHE, fromAirTricket)
//
//
//            val toAirTricket = Airport()
//            fromAirTricket.airportName = searchRoundTripModel.toPort
//            fromAirTricket.iata = searchRoundTripModel.getToName()
//
//            AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.TO_CACHE, toAirTricket)


        }




        tvClass.setOnClickListener {


            handleClass()
        }

        llPassenger.setOnClickListener {

            handlePassengerClick()
        }


        val fromCacheAirport = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.FROM_CACHE) as Airport?
        if (fromCacheAirport != null) {
            view.tsRoundTripFrom.setText(fromCacheAirport.iata)
            view.tsRoundTripFromPort.setText(fromCacheAirport.airportName)
            view.tvHitFrom.visibility = View.VISIBLE

            fromAirport = Airport()
            fromAirport.iata = fromCacheAirport.iata



            searchRoundTripModel.setFromName(fromCacheAirport.iata)
            searchRoundTripModel.setFromPortName(fromCacheAirport.airportName)


            val fromAirTricket = Airport()
            fromAirTricket.airportName = searchRoundTripModel.fromPort
            fromAirTricket.iata = searchRoundTripModel.getFromName()

            AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.FROM_CACHE, fromAirTricket)


        } else {
            view.tsRoundTripFrom.setCurrentText(activity?.application?.getString(R.string.from))
            view.tsRoundTripFromPort.setCurrentText(activity?.application?.getString(R.string.airport))
            view.tvHitFrom.visibility = View.INVISIBLE
        }


        val toCacheAirport = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.TO_CACHE) as Airport?
        if (toCacheAirport != null) {

            view.tsRoundTripTo.setText(toCacheAirport.iata)
            view.tsRoundTripToPort.setText(toCacheAirport.airportName)
            view.tvHitTo.visibility = View.VISIBLE

            toAirport = Airport()
            toAirport.iata = toCacheAirport.iata


            searchRoundTripModel.setToName(toCacheAirport.iata)
            searchRoundTripModel.setToName(toCacheAirport.airportName)


            val toAirTricket = Airport()
            toAirTricket.airportName = toCacheAirport.airportName
            toAirTricket.iata = toCacheAirport.iata

            AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.TO_CACHE, toAirTricket)


        } else {
            view.tsRoundTripTo.setCurrentText(activity?.application?.getString(R.string.to))
            view.tsRoundTripToPort.setCurrentText(activity?.application?.getString(R.string.airport))
            view.tvHitTo.visibility = View.INVISIBLE
        }


        val crachDepartureDate = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE_SECOUND_ROUND) as String?
        if (crachDepartureDate != null) {
            view.tvDepartDate.text = "" + crachDepartureDate
            view.tvDepartDate.setTextColor(Color.BLACK)

            val firstDate = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE_SECOUND_ROUND) as String
            humanReadAbleDateFirst = firstDate
            view.tvDepartDate.text = "" + firstDate
            view.tvDepartDate.setTextColor(Color.BLACK)

            val secoundDate = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE_SECOUND_ROUND) as String
            humanReadAbleDateSecond = secoundDate
            view.tvDepartDate2.text = "" + secoundDate
            view.tvDepartDate2.setTextColor(Color.BLACK)


        }


        val fromAirTricket = Airport()
        fromAirTricket.airportName = searchRoundTripModel.fromPort
        fromAirTricket.iata = searchRoundTripModel.getFromName()

        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.FROM_CACHE_ROUND, fromAirTricket)


        val toAirTricket = Airport()
        fromAirTricket.airportName = searchRoundTripModel.toPort
        fromAirTricket.iata = searchRoundTripModel.getToName()

        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.TO_CACHE_ROUND, toAirTricket)


        val classModel = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.CLASS_TYPE) as ClassModel?
        if (classModel == null) {

            mClassModel = ClassModel("Economy", "Economy", true)
        } else {
            mClassModel = classModel
            view.airTicketClass.setText(classModel.className)
        }


        val infanntPass = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.INFANT_PSNGER) as Int?
        if (infanntPass != null) {
            onInfantPsngrTextChange("" + infanntPass)
        }


        val kidPsnGer = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.KID_PSNGER) as Int?
        if (kidPsnGer != null) {
            onKidPsngrTextChange("" + kidPsnGer)
        }


        val adulPassger = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.ADUL_PSNGER) as Int?
        if (adulPassger != null) {
            onAdultPsngrTextChange("" + adulPassger)
        }
    }

    override fun onResume() {
        super.onResume()
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
        airTicketAdult.setText(text)
        val toInt = text.toInt()

        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.ADUL_PSNGER, toInt)

        if (toInt > 0) {
            airTicketAdult.setTextColor(getResources().getColor(com.cloudwell.paywell.services.R.color.black33333))
        } else {
            airTicketAdult.setTextColor(getResources().getColor(com.cloudwell.paywell.services.R.color.blackcccccc))
        }
    }

    fun onKidPsngrTextChange(text: String) {
        airTicketKid.setText(text)
        val toInt = text.toInt()

        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.KID_PSNGER, toInt)

        if (toInt > 0) {
            airTicketKid.setTextColor(getResources().getColor(com.cloudwell.paywell.services.R.color.black33333))
        } else {
            airTicketKid.setTextColor(getResources().getColor(com.cloudwell.paywell.services.R.color.blackcccccc))
        }
    }

    fun onInfantPsngrTextChange(text: String) {
        airTicketInfant.setText(text)
        val toInt = text.toInt()
        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.INFANT_PSNGER, toInt)

        if (toInt > 0) {
            airTicketInfant.setTextColor(getResources().getColor(com.cloudwell.paywell.services.R.color.black33333))
        } else {
            airTicketInfant.setTextColor(getResources().getColor(com.cloudwell.paywell.services.R.color.blackcccccc))
        }
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

                    AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.FROM_CACHE, fromAirport)

                }

                REQ_CODE_TO -> {

                    toAirport = get

                    searchRoundTripModel.setToName(get.iata)
                    searchRoundTripModel.setToPortName(get.airportName)

                    tsRoundTripTo.setText(get.iata)
                    tsRoundTripToPort.setText(get.airportName)

                    tvHitTo.visibility = View.VISIBLE

                    AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.TO_CACHE, toAirport)
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        try {
            if (isVisibleToUser) {
                inilitzationView(myView)
            } else {

            }
        } catch (e: Exception) {
            com.orhanobut.logger.Logger.v("", "")
        }

    }
}

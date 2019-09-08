package com.cloudwell.paywell.services.activity.eticket.airticket.fragment


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.*
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.RequestAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Segment
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.AirportsSearchActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.FlightSearchViewActivity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.utils.FormatHelper
import com.cloudwell.paywell.services.utils.FormatHelper.getPortLevelText
import com.franmontiel.fullscreendialog.FullScreenDialogFragment
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.fragment__one_way_v2.*
import kotlinx.android.synthetic.main.fragment__one_way_v2.view.*
import mehdi.sakout.fancybuttons.FancyButton
import java.text.SimpleDateFormat
import java.util.*


class OneWayV2Fragment : Fragment(), View.OnClickListener, FullScreenDialogFragment.OnConfirmListener, FullScreenDialogFragment.OnDiscardListener {

    private val KEY_TAG = OneWayV2Fragment::class.java!!.getName()
    private val REQ_CODE_FROM = 1
    private val REQ_CODE_TO = 3

    private var mAppHandler: AppHandler? = null
    private lateinit var frameLayout: FrameLayout
    private lateinit var dialogFragment: FullScreenDialogFragment
    val dialogTag = "dialog"

    val myCalendar = Calendar.getInstance()


    private lateinit var fromAirport: Airport
    private lateinit var toAirport: Airport

    private lateinit var searchRoundTripModel: SearchRoundTripModel
    lateinit var mClassModel: ClassModel

    lateinit var airTicketAdult: TextView
    lateinit var airTicketKid: TextView
    lateinit var airTicketInfant: TextView


    var isReSchuduler = false


    companion object {
        val KEY_REQUEST_KEY = "KEY_REQUEST_KEY"
        val KEY_REQUEST_FOR_FROM = 1
        val KEY_FROM = "From"
        val KEY_To = "To"
        val KEY_AIRPORT = "Airport"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment__one_way_v2, container, false)

        init(view)

        return view
    }

    private fun init(view: View) {
        frameLayout = view.findViewById(R.id.myView)
        val tvDepart = view.findViewById<TextView>(R.id.tvDepart2)
        val tvDepartDate = view.findViewById<TextView>(R.id.tvDepartDate)
        val airTicketClass = view.findViewById<TextView>(R.id.airTicketClass)
        val llPassenger = view.findViewById<LinearLayout>(R.id.llPsngr)
        val btnSearch = view.findViewById<FancyButton>(R.id.btn_search)
        val tvFrom = view.findViewById<LinearLayout>(R.id.tvFrom)
        val layoutTo = view.findViewById<LinearLayout>(R.id.layoutTo)
        val layoutDeaprtDate = view.findViewById<LinearLayout>(R.id.layoutDeaprtDate)

        mAppHandler = AppHandler.getmInstance(context)

        tvDepart.setOnClickListener(this)
        tvDepartDate.setOnClickListener(this)
        airTicketClass.setOnClickListener(this)
        llPassenger.setOnClickListener(this)
        btnSearch.setOnClickListener(this)
        tvFrom.setOnClickListener(this)
        layoutTo.setOnClickListener(this)
        layoutDeaprtDate.setOnClickListener(this)

        view.btn_search.setOnClickListener(this)


        airTicketInfant = view.findViewById<TextView>(R.id.airTicketInfant)
        airTicketAdult = view.findViewById<TextView>(R.id.airTicketAdult)
        airTicketKid = view.findViewById<TextView>(R.id.airTicketKid)

        val tsFrom = view.findViewById<TextSwitcher>(R.id.tsOneWayTripFrom)
        val tsFromPort = view.findViewById<TextSwitcher>(R.id.tsOneWayTripFromPort)
        val tsTo = view.findViewById<TextSwitcher>(R.id.tsOneWayTripTo)
        val tsToPort = view.findViewById<TextSwitcher>(R.id.tsOneWayTripToPort)
        val ivSwitchTrip = view.findViewById<ImageView>(R.id.ivOneWayTripTextSwitcher)

        tsFrom.removeAllViews()
        tsFrom.setFactory {
            TextView(ContextThemeWrapper(context,
                    R.style.TicketFrom), null, 0)
        }
        tsFromPort.removeAllViews()
        tsFromPort.setFactory {
            TextView(ContextThemeWrapper(context,
                    R.style.TicketFromPort), null, 0)
        }
        tsTo.removeAllViews()
        tsTo.setFactory {
            TextView(ContextThemeWrapper(context,
                    R.style.TicketTo), null, 0)
        }
        tsToPort.removeAllViews()
        tsToPort.setFactory {
            TextView(ContextThemeWrapper(context,
                    R.style.TicketToPort), null, 0)
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

        val fromCacheAirport = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.FROM_CACHE) as Airport?
        if (fromCacheAirport != null) {
            view.tsOneWayTripFrom.setText(fromCacheAirport.iata)
            view.tsOneWayTripFromPort.setText(fromCacheAirport.airportName)
            view.tvHitFrom.visibility = View.VISIBLE

            fromAirport = Airport()
            fromAirport.iata = fromCacheAirport.iata

        } else {
            tsFrom.setCurrentText(activity?.application?.getString(R.string.from))
            tsFromPort.setCurrentText(activity?.application?.getString(R.string.airport))
            view.tvHitFrom.visibility = View.INVISIBLE
        }


        val toCacheAirport = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.TO_CACHE) as Airport?
        if (toCacheAirport != null) {

            view.tsOneWayTripTo.setText(toCacheAirport.iata)
            view.tsOneWayTripToPort.setText(toCacheAirport.airportName)
            view.tvHitTo.visibility = View.VISIBLE

            toAirport = Airport()
            toAirport.iata = toCacheAirport.iata

        } else {
            tsTo.setCurrentText(activity?.application?.getString(R.string.to))
            tsToPort.setCurrentText(activity?.application?.getString(R.string.airport))
            view.tvHitTo.visibility = View.INVISIBLE
        }


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


            val fromAirTricket = Airport()
            fromAirTricket.airportName = searchRoundTripModel.fromPort
            fromAirTricket.iata = searchRoundTripModel.getFromName()
            AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.FROM_CACHE, fromAirTricket)


            val toAirTricket = Airport()
            toAirTricket.airportName = searchRoundTripModel.toPort
            toAirTricket.iata = searchRoundTripModel.getToName()

            AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.TO_CACHE, toAirTricket)



            toAirport.iata = toAirTricket.iata
            fromAirport.iata = fromAirTricket.iata

        }


        val fromAirTricket = Airport()
        fromAirTricket.airportName = searchRoundTripModel.fromPort
        fromAirTricket.iata = searchRoundTripModel.getFromName()

        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.FROM_CACHE, fromAirTricket)


        val toAirTricket = Airport()
        fromAirTricket.airportName = searchRoundTripModel.toPort
        fromAirTricket.iata = searchRoundTripModel.getToName()

        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.TO_CACHE, fromAirTricket)


        val crachDepartureDate = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE) as String?
        if (crachDepartureDate != null) {
            view.tvDepartDate.text = "" + crachDepartureDate
            view.tvDepartDate.setTextColor(Color.BLACK)
            val departDate = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE_API_formate) as String
            searchRoundTripModel.departDate = departDate

        }

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


        // only run when reschedule

        if (AirTicketMainActivity.item.mSearchLog.size != 0) {
            isReSchuduler = true

            val mSearchLog = AirTicketMainActivity.item.mSearchLog.get(0)

            val originPort = mSearchLog.originPort
            val destinationPort = mSearchLog.destinationPort
            val cabinClass = mSearchLog.cabinClass
            val adultQty = mSearchLog.adultQty
            val childQty = mSearchLog.childQty
            val infantQty = mSearchLog.infantQty


            tsFrom.setCurrentText(originPort)
            AirThicketRepository(AppController.getContext()).getAirportBy(originPort).observeForever {
                tsFromPort.setCurrentText(getPortLevelText(it))
                tsFromPort.isEnabled = false
                tsFromPort.alpha = 0.5f
                tvFrom.isEnabled = false
                tvFrom.alpha = 0.5f

                searchRoundTripModel.setFromName(originPort)
                fromAirport.iata = originPort


            }

            tsTo.setCurrentText(destinationPort)
            AirThicketRepository(AppController.getContext()).getAirportBy(destinationPort).observeForever {
                tsToPort.setCurrentText(getPortLevelText(it))
                tsToPort.isEnabled = false
                tsToPort.alpha = 0.5f

                layoutTo.isEnabled = false
                layoutTo.alpha = 0.5f

                searchRoundTripModel.setToName(destinationPort)
                toAirport.iata = destinationPort

            }

            tvDepartDate.setText(getString(R.string.date))
            searchRoundTripModel.departDate = ""


            mClassModel = ClassModel(cabinClass, cabinClass, true)
            view.airTicketClass.setText(mClassModel.className)
            view.airTicketClass.isEnabled = false
            view.airTicketClass.alpha = 0.5f

            onAdultPsngrTextChange("" + adultQty)
            airTicketAdult.isEnabled = false
            airTicketAdult.alpha = 0.5f

            onKidPsngrTextChange("" + childQty)
            airTicketKid.isEnabled = false
            airTicketKid.alpha = 0.5f


            onInfantPsngrTextChange("" + infantQty)
            airTicketInfant.isEnabled = false
            airTicketInfant.alpha = 0.5f

            llPassenger.isEnabled = false
            llPassenger.alpha = 0.5f


            view.btn_search.setText("Reschedule search")

            view.tsOneWayTripFromPort.visibility = View.INVISIBLE
            view.tsOneWayTripToPort.visibility = View.INVISIBLE


        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvDepart2 -> {
                showDepartDatePicker()
            }

            R.id.tvDepartDate -> {
                showDepartDatePicker()
            }

            R.id.layoutDeaprtDate -> {
                showDepartDatePicker()
            }


            R.id.airTicketClass -> {

                handleClass()

            }

            R.id.llPsngr -> {

                handlePassengerClick()
            }

            R.id.btn_search -> {

                handleSearchClick()
            }

            R.id.tvFrom -> {

                val intent = Intent(context, AirportsSearchActivity::class.java)
                intent.putExtra("from", 1)
                intent.putExtra("isTo", false)
                startActivityForResult(intent, REQ_CODE_FROM)
            }

            R.id.layoutTo -> {

                val intent = Intent(context, AirportsSearchActivity::class.java)
                intent.putExtra("from", 1)
                intent.putExtra("isTo", true)
                startActivityForResult(intent, REQ_CODE_TO)
            }


            R.id.llDatePicker -> {
                DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

        }
    }


    var date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {

        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel()
        }

    }

    private fun updateDateLabel() {
        val date = "yyyy-MM-dd"
        val myFormat = SimpleDateFormat(date, Locale.ENGLISH).parse(date) as Date


        val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(myFormat)

        val myFormatOne = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormatOne, Locale.ENGLISH)


        tvDepartDate.setText(humanReadAbleDate.format(myCalendar.time))

        searchRoundTripModel.departDate = humanReadAbleDate.format(myCalendar.time)
        Log.e("logtag", myFormatOne)
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


    fun onAdultPsngrTextChange(text: String) {
        airTicketAdult.setText(text)
        val toInt = text.toInt()

        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.ADUL_PSNGER, toInt)

        if (toInt > 0) {
            airTicketAdult.setTextColor(getResources().getColor(R.color.black33333))
        } else {
            airTicketAdult.setTextColor(getResources().getColor(R.color.blackcccccc))
        }
    }

    fun onKidPsngrTextChange(text: String) {
        airTicketKid.setText(text)
        val toInt = text.toInt()

        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.KID_PSNGER, toInt)

        if (toInt > 0) {
            airTicketKid.setTextColor(getResources().getColor(R.color.black33333))
        } else {
            airTicketKid.setTextColor(getResources().getColor(R.color.blackcccccc))
        }
    }

    fun onInfantPsngrTextChange(text: String) {
        airTicketInfant.setText(text)
        val toInt = text.toInt()
        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.INFANT_PSNGER, toInt)

        if (toInt > 0) {
            airTicketInfant.setTextColor(getResources().getColor(R.color.black33333))
        } else {
            airTicketInfant.setTextColor(getResources().getColor(R.color.blackcccccc))
        }
    }

    private fun handleClass() {
        val b = Bundle()
        b.putString("myClassName", airTicketClass.text.toString())

        val bottomSheet = ClassBottomSheetDialog()
        bottomSheet.setOnClassListener(object : ClassBottomSheetDialog.ClassBottomSheetListener {
            override fun onButtonClickListener(classModel: ClassModel) {
                mClassModel = classModel

                airTicketClass.setText(classModel.className)

                AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.CLASS_TYPE, classModel)

            }

        })

        bottomSheet.arguments = b
        bottomSheet.show(fragmentManager, "classBottomSheet")
    }

    private fun showDepartDatePicker() {

        val calendar = Calendar.getInstance()

        if (!tvDepartDate.text.equals(getString(R.string.date))) {
            val crachDepartureDate = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE_API_formate) as String?

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(crachDepartureDate) as Date
            calendar.time = date

        }

        val year = calendar.get(Calendar.YEAR)
        val thismonth = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    val date = calendar.getTime()

                    val nameOfDayOfWeek = SimpleDateFormat("EEE", Locale.ENGLISH).format(date)
                    val nameOfMonth = SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.getTime())

                    tvDepartDate.text = "$nameOfDayOfWeek, $day $nameOfMonth"
                    tvDepartDate.setTextColor(Color.BLACK);

                    AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE, "" + "$nameOfDayOfWeek, $day $nameOfMonth")


                    val mMonth = month + 1;

                    val androidSystemdate = "${year}-${mMonth}-${day}"

                    val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(androidSystemdate) as Date
                    val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)


                    searchRoundTripModel.departDate = humanReadAbleDate

                    AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.DEPART_DATE_API_formate, "" + humanReadAbleDate)


                }, year, thismonth, dayOfMonth)

        val calendarMin = Calendar.getInstance()
        datePickerDialog.datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = calendarMin.timeInMillis
        datePickerDialog.show()


    }


    override fun onConfirm(result: Bundle?) {
    }

    override fun onDiscard() {
        dialogFragment.dismiss()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val get = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.AIRPORT) as Airport

            when (requestCode) {
                REQ_CODE_FROM -> {

                    fromAirport = get

                    var cityOrStatusName = ""
                    if (!get.city.equals("")) {
                        cityOrStatusName = get.city + "/"
                    } else if (!get.state.equals("")) {
                        cityOrStatusName = get.state + "/"
                    }

                    searchRoundTripModel.setFromName(get.iata)
                    searchRoundTripModel.setFromPortName("" + FormatHelper.formatText(cityOrStatusName + get.airportName))

                    tsOneWayTripFrom.setText(get.iata)
                    tsOneWayTripFromPort.setText(FormatHelper.formatText(cityOrStatusName + get.airportName))
                    tvHitFrom.visibility = View.VISIBLE

                    fromAirport.airportName = FormatHelper.formatText(cityOrStatusName + get.airportName).toString()

                    AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.FROM_CACHE, get)

                }

                REQ_CODE_TO -> {

                    toAirport = get

                    var cityOrStatusName = ""
                    if (!get.city.equals("")) {
                        cityOrStatusName = get.city + "/"
                    } else if (!get.state.equals("")) {
                        cityOrStatusName = get.state + "/"
                    }


                    searchRoundTripModel.setToName(get.iata)
                    searchRoundTripModel.setToPortName("" + FormatHelper.formatText(cityOrStatusName + get.airportName))

                    tsOneWayTripTo.setText(get.iata)
                    tsOneWayTripToPort.setText("" + FormatHelper.formatText(cityOrStatusName + get.airportName))
                    tvHitTo.visibility = View.VISIBLE

                    toAirport.airportName = FormatHelper.formatText(cityOrStatusName + get.airportName).toString()

                    AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.TO_CACHE, get)

                }

            }
        }
    }

    private fun handleSearchClick() {

        if (searchRoundTripModel.getFromName().equals(OneWayV2Fragment.KEY_FROM)) {
            Toast.makeText(activity?.applicationContext, getString(R.string.please_select_from_airport), Toast.LENGTH_LONG).show()

            return
        }

        if (searchRoundTripModel.getToName().equals(OneWayV2Fragment.KEY_To)) {
            Toast.makeText(activity?.applicationContext, getString(R.string.please_select_arrival_airport), Toast.LENGTH_LONG).show()
            return
        }

        if (searchRoundTripModel.departDate.equals("")) {
            Toast.makeText(activity?.applicationContext, getString(R.string.please_select_depart_date), Toast.LENGTH_LONG).show()
            return
        }


        val list = mutableListOf<Segment>()
        val segment = Segment(mClassModel.apiClassName, searchRoundTripModel.departDate, toAirport.iata, fromAirport.iata)
        list.add(segment)

        val requestAirSearch = RequestAirSearch(airTicketAdult.text.toString().toLong(), airTicketKid.text.toString().toLong(), airTicketInfant.text.toString().toLong(), "Oneway", list)

        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.REQUEST_AIR_SERACH, requestAirSearch)
        AppStorageBox.put(activity?.applicationContext, AppStorageBox.Key.REQUEST_API_reschedule, AirTicketMainActivity.item)


        val intent = Intent(activity?.applicationContext, FlightSearchViewActivity::class.java)
        intent.putExtra("isReSchuduler", isReSchuduler)

        startActivity(intent)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        try {
            if (isVisibleToUser) {
                init(myView)
            } else {

            }
        } catch (e: Exception) {
            Logger.v("", "")
        }


    }


}



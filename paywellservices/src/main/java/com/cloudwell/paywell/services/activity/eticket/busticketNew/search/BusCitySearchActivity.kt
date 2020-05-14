package com.cloudwell.paywell.services.activity.eticket.busticketNew.search

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.ViewModelProviders
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.BusHosttActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatCheckBookAPI
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.CitiesListItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.RequestScheduledata
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata.ScheduleDataItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm.ResposeTicketConfirm
import com.cloudwell.paywell.services.activity.eticket.busticketNew.search.FullScreenDialogBus.OnCitySet
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bus_city_search_new.*
import kotlinx.android.synthetic.main.bus_advance_setttings.*
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class BusCitySearchActivity : BusTricketBaseActivity(), OnCitySet, IbusTransportListView, OnItemSelectedListener {

    private var fromCitiesListItem: CitiesListItem? = null
    private var toCitiesListItem: CitiesListItem? = null

    private var fromString: String? = null
    private var toString: String? = null
    private lateinit var myCalender: Calendar
    private lateinit var myCalenderRetrun: Calendar
    private var simpleDateFormat: SimpleDateFormat? = null
    private val dateString = String()

    var viewBoardingPoint: View? = null
    private var viewMode: BusTransportViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_city_search_new)


        setToolbar(getString(R.string.search_transport_ticket), applicationContext.resources.getColor(R.color.bus_ticket_toolbar_title_text_color))


        myCalender = Calendar.getInstance()
        myCalenderRetrun = Calendar.getInstance()


        viewBoardingPoint = findViewById(R.id.viewBoardingPoint)


        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        AppStorageBox.put(this, AppStorageBox.Key.BUS_JOURNEY_DATE, simpleDateFormat!!.format(myCalender.timeInMillis))
        dateTV.text = myCalender.get(Calendar.DAY_OF_MONTH).toString()
        monthTV.text = DateFormatSymbols().months[myCalender.get(Calendar.MONTH)]
        dayTV.text = DateFormatSymbols().weekdays[myCalender.get(Calendar.DAY_OF_WEEK)]


        myCalenderRetrun.add(Calendar.DATE, 1)

        dateTVRound.text = myCalenderRetrun.get(Calendar.DAY_OF_MONTH).toString()
        monthTVRound.text = DateFormatSymbols().months[myCalenderRetrun.get(Calendar.MONTH)]
        dayTVRound.text = DateFormatSymbols().weekdays[myCalenderRetrun.get(Calendar.DAY_OF_WEEK)]




        busFromCityTS.setFactory(ViewSwitcher.ViewFactory {
            val switcherTextView = TextView(applicationContext)
            switcherTextView.textSize = 18f
            switcherTextView.setTextColor(Color.BLACK)
            switcherTextView
        })
        fromString = FROM_STRING
        toString = TO_STRING




        busToCityTS.setFactory(ViewSwitcher.ViewFactory {
            val switcherTextView = TextView(applicationContext)
            switcherTextView.textSize = 18f
            switcherTextView.setTextColor(Color.BLACK)
            switcherTextView
        })



        val inAnim = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in)
        val outAnim = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out)
        inAnim.duration = 200
        outAnim.duration = 200
        busFromCityTS.inAnimation = inAnim
        busFromCityTS.outAnimation = outAnim
        busToCityTS.inAnimation = inAnim
        busToCityTS.outAnimation = outAnim

        textSwitchIV.setOnClickListener(View.OnClickListener {

              switchFromAndTo()

        })

        val isBusTicket = AppStorageBox.get(applicationContext, AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW) as Boolean

        if (isBusTicket) {
            setOldDataForBus()
        } else{
            setOldDataForLaunch()
        }


        btn_search.setOnClickListener(View.OnClickListener {


            if (HandleSearch()) return@OnClickListener


        })
        fromLL.setOnClickListener(View.OnClickListener {
            val dialog = FullScreenDialogBus()
            val b = Bundle()
            b.putString(FullSCREEN_DIALOG_HEADER, FROM_STRING)
            dialog.arguments = b

            val ft = supportFragmentManager.beginTransaction()
            dialog.show(ft, FullScreenDialogBus.TAG)
        })
        toLL.setOnClickListener(View.OnClickListener { view: View? ->
            val dialog = FullScreenDialogBus()
            val b = Bundle()
            b.putString(FullSCREEN_DIALOG_HEADER, TO_STRING)
            dialog.arguments = b
            val ft = supportFragmentManager.beginTransaction()
            dialog.show(ft, FullScreenDialogBus.TAG)
        })

        radioGroupTripType.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonOneWay -> {
                    var i = 0
                    while (i < llRetrunDate.childCount) {
                        val view = llRetrunDate.getChildAt(i)
                        view.isEnabled = false // Or whatever you want to do with the view.
                        view.visibility = View.INVISIBLE
                        i++
                    }

                    viewLineRetrun.visibility = View.GONE
                    tvLevelReturnType.visibility = View.GONE
                    radioGroupRetrun.visibility = View.GONE
                    tvLevelRetrunTime.visibility = View.GONE
                    radioGroupRetunTime.visibility = View.GONE

                }
                R.id.radioButtonButtonRoud -> {
                    var i = 0
                    while (i < llRetrunDate.childCount) {
                        val view = llRetrunDate.getChildAt(i)
                        view.isEnabled = true // Or whatever you want to do with the view.
                        view.visibility = View.VISIBLE
                        i++
                    }
                    viewLineRetrun.visibility = View.VISIBLE
                    tvLevelReturnType.visibility = View.VISIBLE
                    radioGroupRetrun.visibility = View.VISIBLE
                    tvLevelRetrunTime.visibility = View.VISIBLE
                    radioGroupRetunTime.visibility = View.VISIBLE

                }
            }
        })



        linearLayoutForAdvanceSearch.setOnClickListener {
            if (advanceSearch.visibility == View.VISIBLE) {
                advanceSearch.visibility = View.GONE
                ivUpDown.setBackgroundResource(R.drawable.icon_down)
            } else {
                advanceSearch.visibility = View.VISIBLE
                ivUpDown.setBackgroundResource(R.drawable.ic_up)
            }
        }


        llJourneyDate.setOnClickListener {

            val datePickerDialog = DatePickerDialog(this@BusCitySearchActivity, R.style.DialogThemeBus, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                myCalender.set(i, i1, i2)
                dateTV.text = myCalender.get(Calendar.DAY_OF_MONTH).toString()
                monthTV.text = DateFormatSymbols().months[myCalender.get(Calendar.MONTH)]
                dayTV.text = DateFormatSymbols().weekdays[myCalender.get(Calendar.DAY_OF_WEEK)]
                AppStorageBox.put(this@BusCitySearchActivity, AppStorageBox.Key.BUS_JOURNEY_DATE, simpleDateFormat!!.format(myCalender.timeInMillis))
            }, myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 10000
            try {
                val  myCalender1 = Calendar.getInstance()
                datePickerDialog.datePicker.minDate = myCalender.time.time
                myCalender1.add(Calendar.DAY_OF_MONTH, 30)
                datePickerDialog.datePicker.maxDate = myCalender1.time.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            datePickerDialog.show()
        }


        llRetrunDate.setOnClickListener {

            val datePickerDialog = DatePickerDialog(this@BusCitySearchActivity, R.style.DialogTheme, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                myCalender.set(i, i1, i2)
                dateTVRound.text = myCalender.get(Calendar.DAY_OF_MONTH).toString()
                monthTVRound.text = DateFormatSymbols().months[myCalender.get(Calendar.MONTH)]
                dayTVRound.text = DateFormatSymbols().weekdays[myCalender.get(Calendar.DAY_OF_WEEK)]
                AppStorageBox.put(this@BusCitySearchActivity, AppStorageBox.Key.BUS_RETURN_DATE, simpleDateFormat!!.format(myCalender.timeInMillis))
            }, myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 10000
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val validateDate = AppStorageBox.get(applicationContext, AppStorageBox.Key.BUS_JOURNEY_DATE) as String
                var date: Date? = null
                date = sdf.parse(validateDate)

                val cal = Calendar.getInstance()
                cal.time = date
                datePickerDialog.datePicker.minDate = cal.time.time
                cal.add(Calendar.DAY_OF_MONTH, 30)
                datePickerDialog.datePicker.maxDate =  cal.time.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            datePickerDialog.show()
        }


        initViewModel()

    }

    private fun HandleSearch(): Boolean {
        if (fromCitiesListItem == null) {
            Toast.makeText(applicationContext, getString(R.string.text_bus_from_error), Toast.LENGTH_LONG).show()
            return true
        }

        if (toCitiesListItem == null) {
            Toast.makeText(applicationContext, getString(R.string.text_bus_to_error), Toast.LENGTH_LONG).show()
            return true
        }

        val p = RequestScheduledata()
        p.username = mAppHandler.userName
        p.departure = "" + fromCitiesListItem?.citiesName
        p.destination = "" + toCitiesListItem?.citiesName
        p.deviceId = mAppHandler.androidID


        val departingDate = AppStorageBox.get(applicationContext, AppStorageBox.Key.BUS_JOURNEY_DATE) as String
        p.departingDate = departingDate


        val isBusTicket = AppStorageBox.get(applicationContext, AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW) as Boolean
        if (isBusTicket) {
            p.transportType = "1"
        } else {
            p.transportType = "0"
        }


        val isAdvanceSearchvisibility = advanceSearch.visibility

        if (isAdvanceSearchvisibility == View.VISIBLE) {

            val checkedRadioButtonId = radioGroupJounyType.checkedRadioButtonId
            val departingTime = when (checkedRadioButtonId) {
                R.id.radioJourneyTimeAll -> "All"
                R.id.radioJourneyTimeAllMorning -> "Morning"
                R.id.radioJourneyTimeAllEvening -> "Evening"
                R.id.radioJourneyTimeAllNight -> "Night"

                else -> "All"
            }
            p.departingTime = departingTime


            val id = radioGroupJounryType.checkedRadioButtonId
            val coachType = when (id) {
                R.id.radioBtmAll -> "All"
                R.id.radioBtmAC -> "AC"
                R.id.radioBtmNonAC -> "NonAC"
                else -> "All"
            }
            p.coachType = coachType


        } else {
            p.departingTime = "All"
            p.coachType = "Any"

        }


        if (radioGroupTripType.checkedRadioButtonId == R.id.radioButtonButtonRoud) {


            val checkedRadioButtonId = radioGroupRetrun.checkedRadioButtonId
            val returnTime = when (checkedRadioButtonId) {
                R.id.radioJourneyTimeAll -> "All"
                R.id.radioJourneyTimeAllMorning -> "Morning"
                R.id.radioJourneyTimeAllEvening -> "Evening"
                R.id.radioJourneyTimeAllNight -> "Night"

                else -> "All"
            }

            p.returnTime = returnTime


            val id = radioGroupRetunTime.checkedRadioButtonId
            val returnCoachType = when (id) {
                R.id.radioBtmAllRetrun -> "All"
                R.id.radioBtmACRetrun -> "AC"
                R.id.radioBtmNonACRetrun -> "NonAC"
                else -> "All"
            }
            p.returnCoachType = returnCoachType


            val returnDate = AppStorageBox.get(applicationContext, AppStorageBox.Key.BUS_RETURN_DATE) as String
            p.returnDate = returnDate
            p.roundTrip = "1"
        }


        val toJson = Gson().toJson(p)

        AppStorageBox.put(AppController.getContext(), AppStorageBox.Key.RequestScheduledata, toJson)
        val intent = Intent(applicationContext, BusHosttActivity::class.java)
        startActivity(intent)


//        viewMode?.search(p)
        return false
    }


    private fun switchFromAndTo() {
        busToCityTS.setText(fromString)
        busFromCityTS.setText(toString)

        fromCitiesListItem.let {
            TO_STRING.let { it1 -> fromCitiesListItem?.let { it2 -> setCityDataToSP(it1, it2) } }
        }

        toCitiesListItem.let {
            FROM_STRING.let { it1 -> toCitiesListItem?.let { it2 -> setCityDataToSP(it1, it2) } }
        }

        val from = toString
        toString = fromString
        fromString = from


        val tempFromCitiesListItem = fromCitiesListItem
        fromCitiesListItem = toCitiesListItem

        toCitiesListItem = tempFromCitiesListItem
    }

    private fun setOldDataForLaunch() {
        val fromDataSP = AppStorageBox.get(this, AppStorageBox.Key.LAUNCH_LEAVING_FROM_CITY) as? String
        fromDataSP.let {
            fromCitiesListItem = Gson().fromJson(it, CitiesListItem::class.java)

            if (fromCitiesListItem != null) {
                busFromCityTS.setText(fromCitiesListItem?.citiesName)
                fromString = fromCitiesListItem?.citiesName

            }
        }

        val toDataSP = AppStorageBox.get(this, AppStorageBox.Key.LAUNCH_GOING_TO_CITY) as? String
        toDataSP.let {
            toCitiesListItem = Gson().fromJson(toDataSP, CitiesListItem::class.java)

            if (toCitiesListItem != null) {
                busToCityTS.setText(toCitiesListItem?.citiesName)
                toString = toCitiesListItem?.citiesName
            }
        }

    }

    private fun setOldDataForBus() {
        val fromDataSP = AppStorageBox.get(this, AppStorageBox.Key.BUS_LEAVING_FROM_CITY) as? String
        fromDataSP.let {
            fromCitiesListItem = Gson().fromJson(it, CitiesListItem::class.java)

            if (fromCitiesListItem != null) {
                busFromCityTS.setText(fromCitiesListItem?.citiesName)
                fromString = fromCitiesListItem?.citiesName

            }
        }

        val toDataSP = AppStorageBox.get(this, AppStorageBox.Key.BUS_GOING_TO_CITY) as? String
        toDataSP.let {
            toCitiesListItem = Gson().fromJson(toDataSP, CitiesListItem::class.java)

            if (toCitiesListItem != null) {
                busToCityTS.setText(toCitiesListItem?.citiesName)
                toString = toCitiesListItem?.citiesName
            }
        }
    }


    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(BusTransportViewModel::class.java)
        viewMode?.setIbusTransportListView(this)


    }

    private fun openTripListActivity(from: String, to: String, boardingPoint: String) {


    }

    override fun setCityData(citiesListItem: CitiesListItem, toOrFrom: String) {
        if (toOrFrom == TO_STRING) {
            busToCityTS.setText(citiesListItem.citiesName)
            toString = citiesListItem.citiesName
            toCitiesListItem  = citiesListItem
        } else if (toOrFrom == FROM_STRING) {
            busFromCityTS.setText(citiesListItem.citiesName)
            fromString = citiesListItem.citiesName
            fromCitiesListItem  = citiesListItem
        } else {
            Toast.makeText(this@BusCitySearchActivity, resources.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
        }
        //getBoardingPoint();
    }


    override fun showNoTripFoundUI() {
        Toast.makeText(applicationContext, applicationContext.resources.getString(R.string.no_boarding_point_found), Toast.LENGTH_LONG).show()
        // boardingViewStatusChange(View.GONE);
    }

    private fun boardingViewStatusChange(gone: Int) { //        boothList.setVisibility(gone);
//        boaringPointLevel.setVisibility(gone);
//        viewBoardingPoint.setVisibility(gone);
    }

    override fun showErrorMessage(message: String) {
        showBusTicketErrorDialog(message)

    }
    override fun showSeatCheckAndBookingRepose(it: ResSeatCheckBookAPI) {}
    override fun showShowConfirmDialog(it: ResposeTicketConfirm) {}
    override fun showProgress() {
        showProgressDialog()
    }

    override fun hiddenProgress() {
        dismissProgressDialog()
    }

    override fun setBoardingPoint(allBoothNameInfo: MutableSet<String>) { //        String[] strings = allBoothNameInfo.toArray(new String[allBoothNameInfo.size()]);

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) { //        AppStorageBox.put(AppController.getContext(), AppStorageBox.Key.BOARDING_POGISTION, position);
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}



    private fun setCityDataToSP(toOrFrom: String, city: CitiesListItem) {

        val isBusTicket = AppStorageBox.get(applicationContext, AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW) as Boolean

        if (isBusTicket) {
            if (toOrFrom == FROM_STRING) {
                AppStorageBox.put(applicationContext, AppStorageBox.Key.BUS_LEAVING_FROM_CITY, Gson().toJson(city))
            } else {
                AppStorageBox.put(applicationContext, AppStorageBox.Key.BUS_GOING_TO_CITY, Gson().toJson(city))
            }
        }else{
            if (toOrFrom == FROM_STRING) {
                AppStorageBox.put(applicationContext, AppStorageBox.Key.LAUNCH_LEAVING_FROM_CITY, Gson().toJson(city))
            } else {
                AppStorageBox.put(applicationContext, AppStorageBox.Key.LAUNCH_GOING_TO_CITY, Gson().toJson(city))
            }
        }


    }

    override fun saveRequestScheduledata(p: RequestScheduledata) {
       // AppStorageBox.put(this@BusCitySearchActivity, AppStorageBox.Key.RequestScheduledata,  Gson().toJson(p))
    }

    override fun saveExtraCharge(double: Double) {
        AppStorageBox.put(this@BusCitySearchActivity, AppStorageBox.Key.ExtraCharge_bus,  double)
    }

    override fun showFilterList(filterTypeDepartingTime: List<ScheduleDataItem>) {

    }

    companion object {
        const val FullSCREEN_DIALOG_HEADER = "header"
        const val TO_STRING = "Going To"
        const val FROM_STRING = "Leaving From"
    }



}
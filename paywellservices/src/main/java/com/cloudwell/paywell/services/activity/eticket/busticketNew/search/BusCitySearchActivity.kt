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
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.BusTransportListActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.*
import com.cloudwell.paywell.services.activity.eticket.busticketNew.search.FullScreenDialogBus.OnCitySet
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.activity_bus_city_search_new.*
import kotlinx.android.synthetic.main.bus_advance_setttings.*
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BusCitySearchActivity : BusTricketBaseActivity(), OnCitySet, IbusTransportListView, OnItemSelectedListener {

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
        AppStorageBox.put(this, AppStorageBox.Key.BUS_JOURNEY_DATE, simpleDateFormat!!.format(myCalender.getTimeInMillis()))
        dateTV.setText(myCalender.get(Calendar.DAY_OF_MONTH).toString())
        monthTV.setText(DateFormatSymbols().months[myCalender.get(Calendar.MONTH)])
        dayTV.setText(DateFormatSymbols().weekdays[myCalender.get(Calendar.DAY_OF_WEEK)])


        myCalenderRetrun.add(Calendar.DATE, 1)

        dateTVRound.setText(myCalenderRetrun.get(Calendar.DAY_OF_MONTH).toString())
        monthTVRound.setText(DateFormatSymbols().months[myCalenderRetrun.get(Calendar.MONTH)])
        dayTVRound.setText(DateFormatSymbols().weekdays[myCalenderRetrun.get(Calendar.DAY_OF_WEEK)])




        busFromCityTS.setFactory(ViewSwitcher.ViewFactory {
            val switcherTextView = TextView(applicationContext)
            switcherTextView.textSize = 18f
            switcherTextView.setTextColor(Color.BLACK)
            switcherTextView
        })
        fromString = FROM_STRING
        toString = TO_STRING
        val fromData = AppStorageBox.get(this, AppStorageBox.Key.BUS_LEAVING_FROM_CITY) as String
        val toData = AppStorageBox.get(this, AppStorageBox.Key.BUS_GOING_TO_CITY) as String
        if (fromData != null && !fromData.isEmpty() && fromData != "null") {
            busFromCityTS.setText(fromData)
            fromString = fromData
        } else {
            busFromCityTS.setText(fromString)
        }
        busToCityTS.setFactory(ViewSwitcher.ViewFactory {
            val switcherTextView = TextView(applicationContext)
            switcherTextView.textSize = 18f
            switcherTextView.setTextColor(Color.BLACK)
            switcherTextView
        })
        if (toData != null && !toData.isEmpty() && toData != "null") {
            busToCityTS.setText(toData)
            toString = toData
        } else {
            busToCityTS.setText(toString)
        }
        val inAnim = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in)
        val outAnim = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out)
        inAnim.duration = 200
        outAnim.duration = 200
        busFromCityTS.setInAnimation(inAnim)
        busFromCityTS.setOutAnimation(outAnim)
        busToCityTS.setInAnimation(inAnim)
        busToCityTS.setOutAnimation(outAnim)

        textSwitchIV.setOnClickListener(View.OnClickListener {
            busToCityTS.setText(fromString)
            busFromCityTS.setText(toString)
            val from = toString
            toString = fromString
            fromString = from
            boardingPoint
        })
        btn_search.setOnClickListener(View.OnClickListener {
            val from = (busFromCityTS.getCurrentView() as TextView).text.toString()
            val to = (busToCityTS.getCurrentView() as TextView).text.toString()
            if (!from.isEmpty() && from != FROM_STRING && !to.isEmpty() && to != TO_STRING) {
                if (from != TO_STRING && to != FROM_STRING) {
                    if (from == to) {
                        Toast.makeText(this@BusCitySearchActivity, applicationContext.resources.getString(R.string.bus_validation_message_location), Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                    //                        if (boothList.getSelectedItem().toString().equals("All")) {
//                            openTripListActivity(from, to, "All");
//                        } else {
//                            openTripListActivity(from, to, boothList.getSelectedItem().toString());
//                        }
                }
            } else {
                Toast.makeText(this@BusCitySearchActivity, applicationContext.resources.getString(R.string.bus_validation_message), Toast.LENGTH_SHORT).show()
            }
        })
        fromLL.setOnClickListener(View.OnClickListener {
            val dialog = FullScreenDialogBus()
            val b = Bundle()
            b.putString(FullSCREEN_DIALOG_HEADER, FROM_STRING)
            dialog.arguments = b
            val ft = fragmentManager.beginTransaction()
            dialog.show(ft, FullScreenDialogBus.TAG)
        })
        toLL.setOnClickListener(View.OnClickListener { view: View? ->
            val dialog = FullScreenDialogBus()
            val b = Bundle()
            b.putString(FullSCREEN_DIALOG_HEADER, TO_STRING)
            dialog.arguments = b
            val ft = fragmentManager.beginTransaction()
            dialog.show(ft, FullScreenDialogBus.TAG)
        })

        initViewModel()

        radioGroupTripType.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonOneWay -> {
                    var i = 0
                    while (i < llRetrunDate.getChildCount()) {
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
                    ivUpDown.setBackgroundResource(R.drawable.icon_down)

                }
                R.id.radioButtonButtonRoud -> {
                    var i = 0
                    while (i < llRetrunDate.getChildCount()) {
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
                    ivUpDown.setBackgroundResource(R.drawable.ic_up)

                }
            }
        })




        linearLayoutForAdvanceSearch.setOnClickListener {

            if (advanceSearch.visibility == View.VISIBLE) {
                advanceSearch.visibility = View.GONE

            } else {
                advanceSearch.visibility = View.VISIBLE
            }


        }


        llJourneyDate.setOnClickListener {

            val datePickerDialog = DatePickerDialog(this@BusCitySearchActivity, R.style.DialogTheme, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                myCalender.set(i, i1, i2)
                dateTV.setText(myCalender.get(Calendar.DAY_OF_MONTH).toString())
                monthTV.setText(DateFormatSymbols().months[myCalender.get(Calendar.MONTH)])
                dayTV.setText(DateFormatSymbols().weekdays[myCalender.get(Calendar.DAY_OF_WEEK)])
                AppStorageBox.put(this@BusCitySearchActivity, AppStorageBox.Key.BUS_JOURNEY_DATE, simpleDateFormat!!.format(myCalender.getTimeInMillis()))
            }, myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 10000
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val validateDate = AppStorageBox.get(applicationContext, AppStorageBox.Key.BUS_VALIDATE_DATE) as String
                var date: Date? = null
                date = sdf.parse(validateDate)
                val millis = date.time
                datePickerDialog.datePicker.maxDate = millis
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            datePickerDialog.show()
        }


        llRetrunDate.setOnClickListener {

            val datePickerDialog = DatePickerDialog(this@BusCitySearchActivity, R.style.DialogTheme, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                myCalender.set(i, i1, i2)
                dateTVRound.setText(myCalender.get(Calendar.DAY_OF_MONTH).toString())
                monthTVRound.setText(DateFormatSymbols().months[myCalender.get(Calendar.MONTH)])
                dayTVRound.setText(DateFormatSymbols().weekdays[myCalender.get(Calendar.DAY_OF_WEEK)])
                AppStorageBox.put(this@BusCitySearchActivity, AppStorageBox.Key.BUS_JOURNEY_DATE, simpleDateFormat!!.format(myCalender.getTimeInMillis()))
            }, myCalender.get(Calendar.YEAR),
                    myCalender.get(Calendar.MONTH),
                    myCalender.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 10000
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val validateDate = AppStorageBox.get(applicationContext, AppStorageBox.Key.BUS_VALIDATE_DATE) as String
                var date: Date? = null
                date = sdf.parse(validateDate)
                val millis = date?.time
                datePickerDialog.datePicker.maxDate = millis!!
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            datePickerDialog.show()
        }

    }

    //    private void tryToOldBoardingPointSeletion() {
//        Integer o = (Integer) AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.BOARDING_POGISTION);
//        if (o != null) {
//            boothList.setSelection(o);
//        }
//    }
    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(BusTransportViewModel::class.java)
        viewMode!!.setIbusTransportListView(this)
    }

    private fun openTripListActivity(from: String, to: String, boardingPoint: String) {
        val date = AppStorageBox.get(applicationContext, AppStorageBox.Key.BUS_JOURNEY_DATE) as String
        val requestBusSearch = RequestBusSearch()
        requestBusSearch.from = from
        requestBusSearch.to = to
        requestBusSearch.date = date
        requestBusSearch.boadingPoint = boardingPoint
        AppStorageBox.put(AppController.getContext(), AppStorageBox.Key.REQUEST_AIR_SERACH, requestBusSearch)
        val intent = Intent(applicationContext, BusTransportListActivity::class.java)
        startActivity(intent)
    }

    override fun setCityData(cityName: String, toOrFrom: String) {
        if (toOrFrom == TO_STRING) {
            busToCityTS!!.setText(cityName)
            toString = cityName
        } else if (toOrFrom == FROM_STRING) {
            busFromCityTS!!.setText(cityName)
            fromString = cityName
        } else {
            Toast.makeText(this@BusCitySearchActivity, resources.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
        }
        //getBoardingPoint();
    }

    private val boardingPoint: Unit
        private get() {
            val from = (busFromCityTS!!.currentView as TextView).text.toString()
            val to = (busToCityTS!!.currentView as TextView).text.toString()
            if (!from.isEmpty() && from != FROM_STRING && !to.isEmpty() && to != TO_STRING) {
                if (from != TO_STRING && to != FROM_STRING) {
                    val date = AppStorageBox.get(applicationContext, AppStorageBox.Key.BUS_JOURNEY_DATE) as String
                    val transport = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.SELETED_BUS_INFO) as Transport
                    val requestBusSearch = RequestBusSearch()
                    requestBusSearch.from = from
                    requestBusSearch.to = to
                    requestBusSearch.date = date
                    viewMode!!.getBoardingPoint(requestBusSearch, transport)
                } else {
                    boardingViewStatusChange(View.GONE)
                }
            } else {
                boardingViewStatusChange(View.GONE)
                Toast.makeText(this@BusCitySearchActivity, applicationContext.resources.getString(R.string.bus_validation_message), Toast.LENGTH_SHORT).show()
            }
        }

    override fun showNoTripFoundUI() {
        Toast.makeText(applicationContext, applicationContext.resources.getString(R.string.no_boarding_point_found), Toast.LENGTH_LONG).show()
        // boardingViewStatusChange(View.GONE);
    }

    private fun boardingViewStatusChange(gone: Int) { //        boothList.setVisibility(gone);
//        boaringPointLevel.setVisibility(gone);
//        viewBoardingPoint.setVisibility(gone);
    }

    override fun setAdapter(it1: List<TripScheduleInfoAndBusSchedule>) {}
    override fun showErrorMessage(message: String) {}
    override fun showSeatCheckAndBookingRepose(it: ResSeatCheckBookAPI) {}
    override fun showShowConfirmDialog(it: ResPaymentBookingAPI) {}
    override fun showProgress() {
        showProgressDialog()
    }

    override fun hiddenProgress() {
        dismissProgressDialog()
    }

    override fun setBoardingPoint(allBoothNameInfo: MutableSet<String>) { //        String[] strings = allBoothNameInfo.toArray(new String[allBoothNameInfo.size()]);
//
//
//        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.bus_spinner_back, strings);
//        boothList.setAdapter(stringArrayAdapter);
//        boardingViewStatusChange(View.VISIBLE);
//        tryToOldBoardingPointSeletion();
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) { //        AppStorageBox.put(AppController.getContext(), AppStorageBox.Key.BOARDING_POGISTION, position);
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    companion object {
        const val FullSCREEN_DIALOG_HEADER = "header"
        const val TO_STRING = "Going To"
        const val FROM_STRING = "Leaving From"
    }
}
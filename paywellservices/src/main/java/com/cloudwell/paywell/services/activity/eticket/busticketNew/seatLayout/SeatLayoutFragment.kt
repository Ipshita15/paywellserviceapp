package com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusSeat
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.utils.BusCalculationHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.bottom_seat_layout.*
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*

class SeatLayoutFragment(requestScheduledata: Any?) : Fragment() , View.OnClickListener {

    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var layout: ViewGroup

    lateinit var rootJsonObject: JSONObject


    internal var seatsPattenStr = ""

    internal var seatViewList: MutableList<TextView> = ArrayList()
    internal var seatSize = 80
    internal var seatGaping = 10

    internal var STATUS_AVAILABLE = 1
    internal var STATUS_BOOKED = 2
    internal var STATUS_RESERVED = 3
    internal var selectedIds = ""
    var allBusSeat = mutableListOf<BusSeat>()
    var lastSeatBusSeat = mutableListOf<BusSeat>()


    internal lateinit var model: TripScheduleInfoAndBusSchedule;
    internal lateinit var requestBusSearch: RequestBusSearch;

    var bus = Transport()

    var seatCounter = 0

    var seatLevel = ""
    var seatIds = ""
    var totalPrices = 0.0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_seat_layout, container, false)

        //        val stringExtra = intent.getStringExtra("jsonData")
//        model = Gson().fromJson(stringExtra, TripScheduleInfoAndBusSchedule::class.java)
//
//        val stringExtra2 = intent.getStringExtra("requestBusSearch")
//        requestBusSearch = Gson().fromJson(stringExtra2, RequestBusSearch::class.java)
//
//        bus = AppStorageBox.get(applicationContext, AppStorageBox.Key.SELETED_BUS_INFO) as Transport
//
//        val title = bus.busname + " " + BusCalculationHelper.getACType(model) + "/" + (model.busSchedule?.scheduleTime
//                ?: "")
//        setToolbar(title, resources.getColor(R.color.bus_ticket_toolbar_title_text_color))
//
//
//        initilizationReviewBottomSheet()
//
//        layout = findViewById<ViewGroup>(R.id.layoutSeat)
//
//
//
//        Logger.v("seat data" + Gson().toJson(model))

        // displaySeatLayout()


        return view

    }


    private fun displaySeatLayout() {
//        val busLocalDB = model.busLocalDB
//        val busSchedule = model.busSchedule
//
//        val indexTotalColumes = Integer.parseInt(busLocalDB?.totalColumns)
//        val totalRowsInt = Integer.parseInt(busLocalDB?.totalRows)
//        val totalMatrix = totalRowsInt * indexTotalColumes
//        val columnsInRightInt = Integer.parseInt(busLocalDB?.columnsInRight)
//
//        allBusSeat = model.resSeatInfo?.allBusSeat!! as MutableList<BusSeat>
//
//
//        var indexCounter = 0
//        try {
//            var i = 0
//            while (i < allBusSeat.size) {
//
//                seatsPattenStr = seatsPattenStr + "/"
//
//
//                for (j in 0 until indexTotalColumes) {
//
//                    if (allBusSeat.size != totalMatrix) {
//                        if (indexCounter >= allBusSeat!!.size - 1) {
//                            break
//                        }
//                    }
//
//                    indexCounter = indexCounter + j
//
//                    if (indexTotalColumes == 1) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                    } else if (indexTotalColumes == 3 && j == 0 && columnsInRightInt == 2) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                        // for right columns 4  ## ##
//                        //                      ## ##
//                    } else if (indexTotalColumes == 4 && j == 1 && columnsInRightInt == 2) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                    } else if (indexTotalColumes == 9 && j == 0 && columnsInRightInt == 4) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                    } else if (indexTotalColumes == 9 && j == 4 && columnsInRightInt == 4) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                    } else if (indexTotalColumes == 9 && j == 8 && columnsInRightInt == 4) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                    } else {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithoutSpace(seatCounter)
//                    }
//
//
//                }
//                i = i + indexTotalColumes
//
//            }
//
//            //        //miss mass matrix than remove last row and add row seat with extra set...
//            if (totalMatrix != allBusSeat.size) {
//                val removeCharaterNumber = indexTotalColumes + 1
//                seatsPattenStr = seatsPattenStr.substring(0, seatsPattenStr.length - removeCharaterNumber)
//
//                lastSeatBusSeat = allBusSeat.subList((allBusSeat.size - removeCharaterNumber), (allBusSeat.size - 1))
//
//
//                for (i in 0 until lastSeatBusSeat.size) {
//                    seatsPattenStr = seatsPattenStr + getSeatSymbolWithoutSpaceForLastLayout(i)
//                }
//            }
//            run {
//                displaySeatPatten()
//            }
//        } catch (e: Exception) {
//            Logger.v("" + e.message)
//        }
    }

    private fun getSeatSymbolWithoutSpaceForLastLayout(i: Int): Any? {
        var data = "";
        if (lastSeatBusSeat.get(i).value == 0) {
            // available seats
            data = "A"
        } else if (lastSeatBusSeat.get(i).value == 1) {
            //Temp booked
            data = "R"
        } else if (lastSeatBusSeat.get(i).value == 2) {
            //Temp booked
            data = "U"
        } else {
            data = "U"
        }
        return data
    }

    private fun getSeatSymbolWithSpace(i: Int): String {
        var data = "";
        if (allBusSeat.get(seatCounter).value == 0) {
            // available seats
            data = "A_"
        } else if (allBusSeat.get(seatCounter).value == 1) {
            //Temp booked
            data = "R_"
        } else if (allBusSeat.get(seatCounter).value == 2) {
            //Temp booked
            data = "U_"
        } else {
            //Temp booked
            data = "U_"
        }
        seatCounter = seatCounter + 1
        Logger.v("ICounter" + seatCounter)

        return data
    }

    private fun getSeatSymbolWithoutSpace(i: Int): String {
        Logger.v("ICounter" + i)
        var data = "";
        if (allBusSeat.get(seatCounter).value == 0) {
            // available seats
            data = "A"
        } else if (allBusSeat.get(seatCounter).value == 1) {
            //Temp booked
            data = "R"
        } else if (allBusSeat.get(seatCounter).value == 2) {
            //Temp booked
            data = "U"
        } else {
            data = "U"
        }

        seatCounter = seatCounter + 1
        Logger.v("ICounter" + seatCounter)

        return data
    }

    private fun initilizationReviewBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(seatLayoutBottonSheet)

        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        btNext.setOnClickListener {


            val toJson = Gson().toJson(model)
            val requestBusSearchJson = Gson().toJson(requestBusSearch)

            var counter = 0;

            allBusSeat.forEach {
                if (it.isUserSeleted) {
                    counter++;
                }
            }

            val transport = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.SELETED_BUS_INFO) as Transport


            val busBaseTicketPrices = BusCalculationHelper.getPricesWithExtraAmount(model.busSchedule?.ticketPrice, requestBusSearch.date, transport = transport, isExtraAmount = false).toDouble()
            val totalAPIValuePrices = busBaseTicketPrices.times(counter)


//            val intent = Intent(this, BusPassengerBoothDepartureActivity::class.java)
//            intent.putExtra("requestBusSearch", requestBusSearchJson)
//            intent.putExtra("jsonData", toJson)
//            intent.putExtra("seatLevel", seatLevel)
//            intent.putExtra("seatId", seatIds)
//            intent.putExtra("totalPrices", "" + totalPrices)
//            intent.putExtra("totalAPIValuePrices", "" + totalAPIValuePrices)
//            startActivity(intent)
        }


        hiddenButtonSheet()


    }

    private fun hiddenButtonSheet() {
        bottomSheetBehavior.setHideable(true);//Important to add
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private fun showButtonSheet() {
        bottomSheetBehavior.setHideable(false);//Important to add
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

//    private fun displaySeatPatten() {
//        seatsPattenStr = "/$seatsPattenStr"
//
//        val layoutSeat = LinearLayout(this)
//        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        layoutSeat.orientation = LinearLayout.VERTICAL
//        layoutSeat.layoutParams = params
//        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping)
//        layout.addView(layoutSeat)
//
//        var layout: LinearLayout? = null
//
//        var count = 0
//
//        for (index in 0 until seatsPattenStr.length) {
//            if (seatsPattenStr.get(index) == '/') {
//                layout = LinearLayout(this)
//                layout.orientation = LinearLayout.HORIZONTAL
//                layoutSeat.addView(layout)
//            } else if (seatsPattenStr.get(index) == 'U') {
//                val model = allBusSeat.get(count)
//
//                val view = TextView(this)
//                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
//                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
//                view.layoutParams = layoutParams
//                view.setPadding(0, 0, 0, 2 * seatGaping)
//                view.id = count
//                view.gravity = Gravity.CENTER
//                view.setBackgroundResource(R.drawable.ic_seat_booked)
//                view.setTextColor(Color.WHITE)
//                view.tag = count
//                view.setText(model.seatLbls)
//                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
//                layout!!.addView(view)
//                seatViewList.add(view)
//                count++
//                view.setOnClickListener(this)
//            } else if (seatsPattenStr.get(index) == 'A') {
//                val model = allBusSeat?.get(count)
//                val view = TextView(this)
//                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
//                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
//                view.layoutParams = layoutParams
//                view.setPadding(0, 0, 0, 2 * seatGaping)
//                view.id = count
//                view.gravity = Gravity.CENTER
//                view.setBackgroundResource(R.drawable.ic_seat_avaliable)
//                view.setText(model.seatLbls)
//                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
//                view.setTextColor(Color.BLACK)
//                view.tag = count
//                layout!!.addView(view)
//                seatViewList.add(view)
//                view.setOnClickListener(this)
//                count++
//            } else if (seatsPattenStr.get(index) == 'R') {
//                val model = allBusSeat?.get(count)
//                val view = TextView(this)
//                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
//                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
//                view.layoutParams = layoutParams
//                view.setPadding(0, 0, 0, 2 * seatGaping)
//                view.id = count
//                view.gravity = Gravity.CENTER
//                view.setBackgroundResource(R.drawable.ic_seat_booked)
//                view.setText(model.seatLbls)
//                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
//                view.setTextColor(Color.WHITE)
//                view.tag = count
//                layout!!.addView(view)
//                seatViewList.add(view)
//                view.setOnClickListener(this)
//                count++
//            } else if (seatsPattenStr.get(index) == '_') {
//                val view = TextView(this)
//                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
//                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
//                view.layoutParams = layoutParams
//                view.setBackgroundColor(Color.TRANSPARENT)
//                view.text = ""
//                layout!!.addView(view)
//            }
//        }
//    }

    override fun onClick(view: View) {
//        val model = allBusSeat.get(view.tag as Int)
//
//        if (model.status.equals("Available")) {
//            if (!model.isUserSeleted) {
//                view.setBackgroundResource(R.drawable.ic_seat_seleted)
//
//                val get = allBusSeat.get(view.tag as Int)
//                get.isUserSeleted = true
//                allBusSeat.set(view.tag as Int, get)
//                updateSeatLayuout()
//                Toast.makeText(this, "Seat " + model.seatLbls + " is selected", Toast.LENGTH_SHORT).show()
//            } else {
//                view.setBackgroundResource(R.drawable.ic_seat_avaliable)
//                val get = allBusSeat.get(view.tag as Int)
//                get.isUserSeleted = false
//                allBusSeat.set(view.tag as Int, get)
//                updateSeatLayuout()
//            }
//        } else {
//            Toast.makeText(this, "Seat " + model.seatLbls + " already booked", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun updateSeatLayuout() {
        displaySeatLayout()
        updatePriceLayuout()

    }

    private fun updatePriceLayuout() {
        seatLevel = ""
        totalPrices = 0.0
        seatIds = ""

        allBusSeat.forEach {
            if (it.isUserSeleted) {
                seatLevel = seatLevel + it.seatLbls + ","
                seatIds = seatIds + it.seatid + ","
                var transport = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.SELETED_BUS_INFO) as Transport


                val toDouble = BusCalculationHelper.getPricesWithExtraAmount(model.busSchedule?.ticketPrice, requestBusSearch.date, transport = transport, isExtraAmount = true).toDouble()
                totalPrices = totalPrices + toDouble

            }
        }
        if (!seatLevel.equals("")) {
            seatLevel = removeLastChar(seatLevel)
            showButtonSheet()
            tvSelectedSeat.text = seatLevel
            tvTotalTotalPrices.text = DecimalFormat("#").format(totalPrices)
        } else {
            hiddenButtonSheet()
        }

        if (!seatIds.equals("")) {
            seatIds = removeLastChar(seatIds)
        }


    }

    fun removeLastChar(s: String): String {
        return if (s.length == 0) {
            s
        } else s.substring(0, s.length - 1)
    }

}

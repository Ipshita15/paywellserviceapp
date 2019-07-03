package com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusSeat
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.bottom_seat_layout.*
import org.json.JSONObject
import java.util.*

class SeatLayoutActivity : BusTricketBaseActivity(), View.OnClickListener {

    internal lateinit var layout: ViewGroup

    internal lateinit var rootJsonObject: JSONObject


    internal var seatsPattenStr = ""

    internal var seatViewList: MutableList<TextView> = ArrayList()
    internal var seatSize = 80
    internal var seatGaping = 10

    internal var STATUS_AVAILABLE = 1
    internal var STATUS_BOOKED = 2
    internal var STATUS_RESERVED = 3
    internal var selectedIds = ""
    //    internal lateinit var allSeatkey: ArrayList<String>
//    internal lateinit var allSeatValue: ArrayList<String>
    var allBusSeat: List<BusSeat> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_layout)


        initilizationReviewBottomSheet()
        layout = findViewById<ViewGroup>(R.id.layoutSeat)

        val stringExtra = intent.getStringExtra("jsonData")
        val model = Gson().fromJson(stringExtra, TripScheduleInfoAndBusSchedule::class.java)
        Logger.v("seat data" + Gson().toJson(model))

        val busLocalDB = model.busLocalDB
        val busSchedule = model.busSchedule

        val indexTotalColumes = Integer.parseInt(busLocalDB?.totalColumns)
        val totalRowsInt = Integer.parseInt(busLocalDB?.totalRows)
        val totalMatrix = totalRowsInt * indexTotalColumes
        val columnsInRightInt = Integer.parseInt(busLocalDB?.columnsInRight)


        var indexCounter = 0

        allBusSeat = model.resSeatInfo?.allBusSeat!!


        try {
            var i = 0
            while (i < allBusSeat.size) {

                seatsPattenStr = seatsPattenStr + "/"

                for (j in 0 until indexTotalColumes) {

                    if (allBusSeat.size != totalMatrix) {
                        if (indexCounter >= allBusSeat!!.size - 1) {
                            break
                        }
                    }

                    indexCounter = indexCounter + j

                    if (indexTotalColumes == 1) {
                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(indexCounter)
                    } else if (indexTotalColumes == 3 && j == 0 && columnsInRightInt == 2) {
                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(indexCounter)

                        // for right columns 4  ## ##
                        //                      ## ##
                    } else if (indexTotalColumes == 4 && j == 1 && columnsInRightInt == 2) {
                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(indexCounter)
                    } else if (indexTotalColumes == 9 && j == 0 && columnsInRightInt == 4) {
                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(indexCounter)
                    } else if (indexTotalColumes == 9 && j == 4 && columnsInRightInt == 4) {
                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(indexCounter)
                    } else if (indexTotalColumes == 9 && j == 8 && columnsInRightInt == 4) {
                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(indexCounter)
                    } else {
                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithoutSpace(indexCounter)
                    }

                }
                i = i + indexTotalColumes


            }

//        //miss mass matrix than remove last row and add row seat with extra set...
            if (totalMatrix != allBusSeat.size) {
                val removeCharaterNumber = indexTotalColumes + 1
                seatsPattenStr = seatsPattenStr.substring(0, seatsPattenStr.length - removeCharaterNumber)
                for (i in 0 until indexTotalColumes) {
                    seatsPattenStr = seatsPattenStr + getSeatSymbolWithoutSpace(i)
                }
            }

            run {
                displaySeatPatten()
            }
        } catch (e: Exception) {

        }


    }

    private fun getSeatSymbolWithSpace(i: Int): String {
        var data = "";
        if (allBusSeat.get(i).value == 0) {
            // available seats
            data = "A_"
        } else if (allBusSeat.get(i).value == 1) {
            //Temp booked
            data = "R_"
        } else if (allBusSeat.get(i).value == 2) {
            //Temp booked
            data = "U_"
        } else {
            //Temp booked
            data = "U_"
        }

        return data
    }

    private fun getSeatSymbolWithoutSpace(i: Int): String {
        var data = "";
        if (allBusSeat.get(i).value == 0) {
            // available seats
            data = "A"
        } else if (allBusSeat.get(i).value == 1) {
            //Temp booked
            data = "R"
        } else if (allBusSeat.get(i).value == 2) {
            //Temp booked
            data = "U"
        } else {
            data = "U"
        }

        return data
    }

    private fun initilizationReviewBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(seatLayoutBottonSheet)

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


    }

    private fun displaySeatPatten() {
        seatsPattenStr = "/$seatsPattenStr"

        val layoutSeat = LinearLayout(this)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutSeat.orientation = LinearLayout.VERTICAL
        layoutSeat.layoutParams = params
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping)
        layout.addView(layoutSeat)

        var layout: LinearLayout? = null

        var count = 0

        for (index in 0 until seatsPattenStr.length) {
            if (seatsPattenStr.get(index) == '/') {
                layout = LinearLayout(this)
                layout.orientation = LinearLayout.HORIZONTAL
                layoutSeat.addView(layout)
            } else if (seatsPattenStr.get(index) == 'U') {
                val value = allBusSeat.get(count)
                count++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seats_booked)
                view.setTextColor(Color.WHITE)
                view.tag = STATUS_BOOKED
                view.setText(value.seatLbls)
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seatsPattenStr.get(index) == 'A') {
                val value = allBusSeat?.get(count)
                count++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seats_book)
                view.setText(value.seatLbls)
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.BLACK)
                view.tag = STATUS_AVAILABLE
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seatsPattenStr.get(index) == 'R') {
                val value = allBusSeat?.get(count)
                count++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seats_reserved)
                view.setText(value.seatLbls)
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.WHITE)
                view.tag = STATUS_RESERVED
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seatsPattenStr.get(index) == '_') {
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setBackgroundColor(Color.TRANSPARENT)
                view.text = ""
                layout!!.addView(view)
            }
        }
    }

    override fun onClick(view: View) {
        if (view.tag as Int == STATUS_AVAILABLE) {
            if (selectedIds.contains(view.id.toString() + ",")) {
                selectedIds = selectedIds.replace((+view.id).toString() + ",", "")
                view.setBackgroundResource(R.drawable.ic_seats_book)
            } else {
                selectedIds = selectedIds + view.id + ","
                view.setBackgroundResource(R.drawable.ic_seats_selected)
            }
        } else if (view.tag as Int == STATUS_BOOKED) {
            Toast.makeText(this, "Seat " + view.id + " is Booked", Toast.LENGTH_SHORT).show()
        } else if (view.tag as Int == STATUS_RESERVED) {
            Toast.makeText(this, "Seat " + view.id + " is Reserved", Toast.LENGTH_SHORT).show()
        }
    }

}

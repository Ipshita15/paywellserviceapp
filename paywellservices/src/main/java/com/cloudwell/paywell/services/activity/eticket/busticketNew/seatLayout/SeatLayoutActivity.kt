package com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository
import com.cloudwell.paywell.services.utils.AssetHelper
import kotlinx.android.synthetic.main.bottom_seat_layout.*
import org.json.JSONException
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
    internal lateinit var allSeatkey: ArrayList<String>
    internal lateinit var allSeatValue: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_layout)

        initilizationReviewBottomSheet()

        layout = findViewById<ViewGroup>(R.id.layoutSeat)


        val transport_id = "37"
        val route = "Dhaka-Kolkata"
        val bus_id = "71"
        val departure_id = "457"
        val departure_date = "2019-06-20"
//        val seat_ids = ""
        val seat_ids = getString(R.string.jsonData)


        BusTicketRepository(this).getSeatCheck(transport_id, route, bus_id, departure_id, departure_date, seat_ids).observeForever {


        }


        try {

            allSeatkey = ArrayList<String>()
            allSeatValue = ArrayList<String>()

            val s = AssetHelper().loadJSONFromAsset(applicationContext, "countriestest.json")

            rootJsonObject = JSONObject(s)

            // total_seat = 40 , total_columns = 10, columns_in_right = 2
            val source = "Teknaf"
            val destination = "Coxs Bazar"
            val schedulesID = "28487"
            val busId = "72"


            // total_seat 34, total_columns = 11, columns_in_right = 2
            //            String source = "Dhaka";
            //            String destination = "Sylhet";
            //            String schedulesID = "24252";
            //            String busId = "86";

            // total_seat 126, total_columns = 9, columns_in_right = 4
            //            String source = "Dhaka";
            //            String destination = "Kolkata";
            //            String schedulesID = "80000";
            //            String busId = "80";


            val allowedSeatNumbers = rootJsonObject.getJSONObject("data").getJSONObject("schedule_info").getJSONObject(source).getJSONObject(destination).getJSONObject("schedules").getJSONObject(schedulesID).getJSONObject("allowed_seat_numbers")

            val busObject = rootJsonObject.getJSONObject("data").getJSONObject("bus_info").getJSONObject(busId)
            val totalSeats = busObject.getString("total_seats")
            val totalRows = busObject.getString("total_rows")
            val totalColumns = busObject.getString("total_columns")
            val structureType = busObject.getString("structure_type")
            val seatStructure = busObject.getString("seat_structure")
            val emptyRowsInRight = busObject.getString("empty_rows_in_right")
            val emptyRowsInLeft = busObject.getString("empty_rows_in_left")
            val columnsInRight = busObject.getString("columns_in_right")
            val busIsAc = busObject.getString("bus_is_ac")
            val busColInMiddle = busObject.getString("bus_col_in_middle")


            val indexTotalColumes = Integer.parseInt(totalColumns)
            val totalRowsInt = Integer.parseInt(totalRows)
            val totalMatrix = totalRowsInt * indexTotalColumes

            val columnsInRightInt = Integer.parseInt(columnsInRight)
            val totalSeatsInt = Integer.parseInt(totalSeats)

            val seatkeys = allowedSeatNumbers.keys()
            while (seatkeys.hasNext()) {
                val seatkey = seatkeys.next()
                allSeatkey.add(seatkey)

                val seatValue = allowedSeatNumbers.getString(seatkey)
                allSeatValue.add(seatValue)


                Log.e("", "")
            }


            var indexCounter = 0

            val message = ""

            run {
                var i = 0
                while (i < allSeatkey.size) {

                    seatsPattenStr = seatsPattenStr + "/"


                    for (j in 0 until indexTotalColumes) {

                        try {

                            if (allSeatkey.size != totalMatrix) {
                                if (indexCounter >= allSeatkey.size - 1) {
                                    break
                                }
                            }

                            indexCounter = indexCounter + j

                            if (indexTotalColumes == 1) {
                                seatsPattenStr = seatsPattenStr + "A_"
                            } else if (indexTotalColumes == 3 && j == 0 && columnsInRightInt == 2) {
                                seatsPattenStr = seatsPattenStr + "A_"

                                // for right columns 4  ## ##
                                //                      ## ##
                            } else if (indexTotalColumes == 4 && j == 1 && columnsInRightInt == 2) {
                                seatsPattenStr = seatsPattenStr + "A_"
                            } else if (indexTotalColumes == 9 && j == 0 && columnsInRightInt == 4) {
                                seatsPattenStr = seatsPattenStr + "A_"
                            } else if (indexTotalColumes == 9 && j == 4 && columnsInRightInt == 4) {
                                seatsPattenStr = seatsPattenStr + "A_"
                            } else if (indexTotalColumes == 9 && j == 8 && columnsInRightInt == 4) {
                                seatsPattenStr = seatsPattenStr + "A_"
                            } else {
                                seatsPattenStr = seatsPattenStr + "A"
                            }// for right columns 4  # #### ####
                            //                      # #### ####
                            // for right columns 3  # ##
                            //                      # ##

                        } catch (ignored: Exception) {
                            break
                        }

                    }
                    i = i + indexTotalColumes
                }
            }

            // miss mass matrix than remove last row and add row seat with extra set...
            if (totalMatrix != allSeatkey.size) {
                val removeCharaterNumber = indexTotalColumes + 1
                seatsPattenStr = seatsPattenStr.substring(0, seatsPattenStr.length - removeCharaterNumber)
                for (i in 0 until indexTotalColumes) {
                    seatsPattenStr = seatsPattenStr + "A"
                }
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        displaySeatPatten()
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
                val value = allSeatValue.get(count)
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
                view.setText(value)
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seatsPattenStr.get(index) == 'A') {
                val value = allSeatValue.get(count)
                count++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seats_book)
                view.setText(value)
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.BLACK)
                view.tag = STATUS_AVAILABLE
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seatsPattenStr.get(index) == 'R') {
                val value = allSeatValue.get(count)
                count++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seats_reserved)
                view.setText(value)
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

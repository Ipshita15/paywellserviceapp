package com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.newBase.BaseFragment
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.BusHosttActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatCheckBookAPI
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.OptionInfoItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.RequestScheduledata
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.SeatBlockRequestPojo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata.ScheduleDataItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.BordingPoint
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.SeatstructureDetailsItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm.ResposeTicketConfirm
import com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout.adapter.CustomSpnerForBoardingPoint
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.bottom_seat_layout.*
import kotlinx.android.synthetic.main.bottom_seat_layout.view.*
import kotlinx.android.synthetic.main.layout_seat_layout.view.*
import org.json.JSONObject
import java.text.DecimalFormat

class SeatLayoutFragment(val scheduleDataItem: ScheduleDataItem, val isRetrunTriple: Boolean) : BaseFragment(), View.OnClickListener, IbusTransportListView {

    private var listener: SeatLayoutFragment.OnFragmentInteractionListener? = null
    private lateinit var viewMode: BusTransportViewModel
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    lateinit var rootSeatLayout: ViewGroup

    lateinit var rootJsonObject: JSONObject


    internal var seatsPattenStr = ""

    internal var seatViewList: MutableList<TextView> = ArrayList()
    internal var seatSize = 80
    internal var seatGaping = 10

    internal var STATUS_AVAILABLE = 1
    internal var STATUS_BOOKED = 2
    internal var STATUS_RESERVED = 3
    internal var selectedIds = ""
    var allBusSeat = ArrayList<SeatstructureDetailsItem>()
    var allLocalMatch = ArrayList<SeatstructureDetailsItem>()

    var lastSeatBusSeat = mutableListOf<SeatstructureDetailsItem>()


    internal lateinit var model: TripScheduleInfoAndBusSchedule
    internal lateinit var requestBusSearch: RequestBusSearch

    var bus = Transport()

    var seatCounter = 0

    var seatLevel = ""
    var seatIds = ""
    var totalPrices = 0.0
    private lateinit var seatLayoutBottonSheet: ConstraintLayout
    private lateinit var busListAdapter: Any
    private lateinit var boothList: Spinner


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_seat_layout, container, false)

        seatLayoutBottonSheet = view.seatLayoutBottonSheet
        rootSeatLayout = view.findViewById<ViewGroup>(R.id.layoutSeat)
        boothList = view.findViewById(R.id.boothList)

        if (scheduleDataItem.isTicketCancelable == 1) {
            view.llLayoutTicketCanceableInfo.visibility = View.GONE
        } else {
            view.llLayoutTicketCanceableInfo.visibility = View.VISIBLE
        }

        initilizationReviewBottomSheet(seatLayoutBottonSheet)


        //displaySeatLayoutv1()
        //displaySeatLayoutv2()
//        displaySeatLayoutv3()
        // displaySeatPattenv2()
//        displaySeatPattenv3()

        //displaySeatLayoutv6()


        setupBoardingpont()

        seatsPattenStr = scheduleDataItem.resSeatInfo?.seatsPattenStr.toString()
        allBusSeat = scheduleDataItem.resSeatInfo?.allBusSeat!!
        displaySeatPattenv2()

        val isBusTicket = AppStorageBox.get(context, AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW) as Boolean
        if (isBusTicket) {
            view.tvUserHitmessage.setText(getString(R.string.any_kind_of_ticket_cancellation_bus))
        } else {
            view.tvUserHitmessage.setText(getString(R.string.any_kind_of_ticket_cancellation_lunch))
        }

        return view

    }

//    private fun displaySeatLayoutv6() {
//
//        val seatViewData = scheduleDataItem.resSeatInfo?.seatviewResponse?.seatViewData
//
//        var indexTotalColumes = seatViewData?.columnNo?.toInt() as Int
//        val totalRowsInt = seatViewData.rowNo?.toInt() as Int
//        val totalMatrix = totalRowsInt * indexTotalColumes
//        allBusSeat = seatViewData.seatstructureDetails as ArrayList<SeatstructureDetailsItem>
//
//        // check miss match Colum
//        if (indexTotalColumes == 5) {
//            desingFor5Columes(indexTotalColumes, totalRowsInt, seatViewData)
//        } else {
//            //  match Colum like green line
//            var columnsInRightInt = 0;
//            if (indexTotalColumes == 3) {
//                columnsInRightInt = 2
//            } else if (indexTotalColumes == 4) {
//                columnsInRightInt = 2
//            } else if (indexTotalColumes == 5) {
//                columnsInRightInt = 2
//            }
//            var indexCounter = 0
//            try {
//                var i = 0
//                while (i < allBusSeat.size) {
//
//                    seatsPattenStr = seatsPattenStr + "/"
//
//
//                    for (j in 0 until indexTotalColumes) {
//
//                        if (allBusSeat.size != totalMatrix) {
//                            if (indexCounter >= allBusSeat!!.size - 1) {
//                                break
//                            }
//                        }
//
//                        indexCounter = indexCounter + j
//
//                        if (indexTotalColumes == 1) {
//                            seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                        } else if (indexTotalColumes == 3 && j == 0 && columnsInRightInt == 2) {
//                            seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                            // for right columns 4  ## ##
//                            //                      ## ##
//                        } else if (indexTotalColumes == 4 && j == 1 && columnsInRightInt == 2) {
//                            seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                        } else if (indexTotalColumes == 9 && j == 0 && columnsInRightInt == 4) {
//                            seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                        } else if (indexTotalColumes == 9 && j == 4 && columnsInRightInt == 4) {
//                            seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                        } else if (indexTotalColumes == 9 && j == 8 && columnsInRightInt == 4) {
//                            seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                        } else {
//                            seatsPattenStr = seatsPattenStr + getSeatSymbolWithoutSpace(seatCounter)
//                        }
//
//
//                    }
//                    i = i + indexTotalColumes
//
//                }
//
//                //        //miss mass matrix than remove last row and add row seat with extra set...
//                if (totalMatrix != allBusSeat.size) {
//                    val removeCharaterNumber = indexTotalColumes + 1
//                    seatsPattenStr = seatsPattenStr.substring(0, seatsPattenStr.length - removeCharaterNumber)
//
//                    lastSeatBusSeat = allBusSeat.subList((allBusSeat.size - removeCharaterNumber), (allBusSeat.size - 1))
//
//
//                    for (i in 0 until lastSeatBusSeat.size) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithoutSpaceForLastLayout(i)
//                    }
//                }
//                run {
//                    displaySeatPattenv2()
//                }
//            } catch (e: Exception) {
//                Logger.v("" + e.message)
//
//            }
//
//        }
//
//
//    }

//    private fun desingFor5Columes(indexTotalColumes: Int, totalRowsInt: Int, allBusSeat: SeatViewData) {
//        for (i in 1..totalRowsInt) {
//
//            seatsPattenStr = seatsPattenStr + "/"
//
//            if (indexTotalColumes == 5) {
//                allBusSeat.seatstructureDetails?.forEach {
//                    if (it?.xAxis == i && it.yAxis == 1) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithoutSpace(seatCounter)
//                    }
//                    if (it?.xAxis == i && it?.yAxis == 2) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSpace(seatCounter)
//                    }
//                    if (it?.xAxis == i && it?.yAxis == 4) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithoutSpace(seatCounter)
//                    }
//                    if (it?.xAxis == i && it?.yAxis == 5) {
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithoutSpace(seatCounter)
//                    }
//                }
//
//            }
//        }
//
//
//        Log.e("", "");
//        run {
//            displaySeatPattenv2()
//        }
//    }


    private fun setupBoardingpont() {


        val values = scheduleDataItem.resSeatInfo?.bordingPoints
        val valueList = values?.let { ArrayList(it) }


        busListAdapter = CustomSpnerForBoardingPoint(requireContext().applicationContext, valueList)
        boothList.adapter = busListAdapter as CustomSpnerForBoardingPoint
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SeatLayoutFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(activity!!).get(BusTransportViewModel::class.java)
        viewMode.setIbusTransportListView(this)


    }

//    private fun displaySeatLayoutv2() {
//
//        val seatViewData = scheduleDataItem.resSeatInfo?.seatviewResponse?.seatViewData
//
//        val indexTotalColumes = seatViewData?.columnNo?.toInt() as Int
//        val totalRowsInt = seatViewData.rowNo?.toInt() as Int
//        //val totalMatrix = totalRowsInt * indexTotalColumes
//        allBusSeat = seatViewData.seatstructureDetails as ArrayList<SeatstructureDetailsItem>
//
//
//        var indexCounter = 0
//        try {
//
//            var row: Int
//            var coloum: Int
//
//            row = 0
//            while (row < totalRowsInt) {
//                coloum = 0
//
//                seatsPattenStr = seatsPattenStr + "/"
//
//
//                while (coloum < indexTotalColumes) {
//                    val indexI = row + 1
//                    val indexJ = coloum + 1
//
//                    val xy = "$indexI$indexJ"
//
//                    val item = allBusSeat.get(indexCounter)
//
//                    if (xy.equals("${item.xAxis}${item.yAxis}")) {
//                        Log.e("L:", "" + item.seatNo)
//
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSymbol(item)
//
//                        indexCounter++
//                        coloum++
//                    } else {
//                        seatsPattenStr = seatsPattenStr + "_"
//                    }
//
//
//                }
//                seatsPattenStr = seatsPattenStr + "/"
//                row++
//            }
//
//        } catch (e: Exception) {
//            Log.e("", "" + e)
//        }
//
//
//    }

//    private fun displaySeatLayoutv3() {
//
//        val seatViewData = scheduleDataItem.resSeatInfo?.seatviewResponse?.seatViewData
//
//        val indexTotalColumes = seatViewData?.columnNo?.toInt() as Int
//        val totalRowsInt = seatViewData.rowNo?.toInt() as Int
//        //val totalMatrix = totalRowsInt * indexTotalColumes
//        allBusSeat = seatViewData.seatstructureDetails as ArrayList<SeatstructureDetailsItem>
//
//
//        val hashMap = HashMap<String, SeatstructureDetailsItem>()
//        allBusSeat.forEach {
//            val xAxis = it.xAxis
//            val yAxis = it.yAxis
//            hashMap.put("$xAxis+$yAxis", it)
//        }
//
//
//        ////
//
//        var indexCounter = 0
//        try {
//
//            var row: Int
//            var coloum: Int
//
//            row = 0
//            while (row < totalRowsInt) {
//                coloum = 0
//
//                seatsPattenStr = seatsPattenStr + "/"
//                while (coloum < indexTotalColumes) {
//                    val indexI = row + 1
//                    val indexJ = coloum + 1
//
//                    val xy = "$indexI$indexJ"
//
//                    val item = hashMap.get("${indexI}${indexJ}")
//
//                    if (item == null) {
//                        seatsPattenStr = seatsPattenStr + "U"
//                        allLocalMatch.add(SeatstructureDetailsItem())
//                    } else {
//                        allLocalMatch.add(item)
//                        seatsPattenStr = seatsPattenStr + getSeatSymbolWithSymbol(item)
//                    }
//
//                    coloum++
//                }
//                seatsPattenStr = seatsPattenStr + "/"
//                row++
//            }
//
//        } catch (e: Exception) {
//            Log.e("", "" + e)
//        }
//
//
//    }

    private fun getSeatSymbolWithSymbol(item: SeatstructureDetailsItem): String {
        var data = ""
        if (item.status.equals("Available")) {
            data = "A"
        } else {
            data = "U"
        }
        return data
    }


    private fun initilizationReviewBottomSheet(seatLayoutBottonSheet: ConstraintLayout) {
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

        seatLayoutBottonSheet.ivSelect.setOnClickListener {

            val appHandler = AppHandler.getmInstance(requireContext())

            val selectedItem = boothList.selectedItem as BordingPoint
            if (isRetrunTriple) {
                val value = viewMode.requestScheduledata.value


                val seatBlockRequestPojo = viewMode.seatBlockRequestPojo.value
                seatBlockRequestPojo?.roundTrip = "2"
                val opif = OptionInfoItem()
                opif.boardingPointId = "" + selectedItem.reportingBranchId
                opif.departureDate = value?.returnDate.toString()
                opif.optionId = scheduleDataItem.busServiceType + "_" + scheduleDataItem.departureId.toString()
                opif.seat = seatIds
                opif.seatLevel = seatLevel


                val oldSingleOptionInfo = seatBlockRequestPojo?.optionInfo?.get(0)
                seatBlockRequestPojo?.optionInfo?.clear()
                oldSingleOptionInfo?.let { it1 ->
                    seatBlockRequestPojo.optionInfo.add(it1)
                    seatBlockRequestPojo.optionInfo.add(opif)
                }

                viewMode.seatBlockRequestPojo.value = seatBlockRequestPojo

                viewMode.retrunBordingPoint.value = selectedItem
                viewMode.retrunScheduleDataItem.value = scheduleDataItem
                viewMode.retrunTotalAmount.value = totalPrices

            } else {
                val value = viewMode.requestScheduledata.value

                val m = SeatBlockRequestPojo()
                m.deviceId = appHandler.androidID
                m.fromCity = value?.departure.toString()
                m.password = ""
                m.roundTrip = "1"
                m.toCity = value?.destination.toString()
                m.username = appHandler.userName
                val opif = OptionInfoItem()
                opif.boardingPointId = "" + selectedItem.reportingBranchId
                opif.departureDate = value?.departingDate.toString()
                opif.optionId = scheduleDataItem.busServiceType + "_" + scheduleDataItem.departureId.toString()
                opif.seat = seatIds
                opif.seatLevel = seatLevel
                m.optionInfo.add(opif)
                viewMode.seatBlockRequestPojo.value = m

                viewMode.singleBordingPoint.value = selectedItem
                viewMode.singleScheduleDataItem.value = scheduleDataItem
                viewMode.singleTotalAmount.value = totalPrices


            }

            listener?.onItemNextButtonClick(isRetrunTriple)

        }

        hiddenButtonSheet()


    }

    private fun hiddenButtonSheet() {
        bottomSheetBehavior.isHideable = true//Important to add
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showButtonSheet() {
        bottomSheetBehavior.isHideable = false//Important to add
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun displaySeatPattenv2() {

        val layoutSeat = LinearLayout(activity?.applicationContext)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutSeat.orientation = LinearLayout.VERTICAL
        layoutSeat.layoutParams = params
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping)
        layoutSeat.gravity = Gravity.CENTER
        rootSeatLayout.addView(layoutSeat)

        var layout: LinearLayout? = null

        var count = 0

        for (index in 0 until seatsPattenStr.length) {
            if (seatsPattenStr.get(index) == '/') {
                layout = LinearLayout(requireActivity())
                layout.orientation = LinearLayout.HORIZONTAL
                layoutSeat.addView(layout)
            } else if (seatsPattenStr.get(index) == 'U') {
                val model = allBusSeat.get(count)

                val view = TextView(requireActivity())
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seat_booked)
                view.setTextColor(Color.WHITE)
                view.tag = count
                view.text = model.seatNo
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                layout!!.addView(view)
                seatViewList.add(view)
                count++
                view.setOnClickListener(this)
            } else if (seatsPattenStr.get(index) == 'A') {
                val model = allBusSeat.get(count)
                val view = TextView(activity?.applicationContext)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seat_avaliable)
                view.text = model.seatNo
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.BLACK)
                view.tag = count
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
                count++
            } else if (seatsPattenStr.get(index) == 'R') {
                val model = allBusSeat.get(count)
                val view = TextView(requireActivity())
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seat_booked)
                view.text = model.seatNo
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.WHITE)
                view.tag = count
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
                count++
            } else if (seatsPattenStr.get(index) == '_') {
                val view = TextView(activity?.applicationContext)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setBackgroundColor(Color.TRANSPARENT)
                view.text = ""
                layout!!.addView(view)
            }
        }
    }

    private fun displaySeatPattenv3() {
        seatsPattenStr = "/$seatsPattenStr"

        val layoutSeat = LinearLayout(activity?.applicationContext)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutSeat.orientation = LinearLayout.VERTICAL
        layoutSeat.layoutParams = params
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping)
        layoutSeat.gravity = Gravity.CENTER
        rootSeatLayout.addView(layoutSeat)

        var layout: LinearLayout? = null

        var count = 0

        for (index in 0 until seatsPattenStr.length) {
            if (seatsPattenStr.get(index) == '/') {
                layout = LinearLayout(requireActivity())
                layout.orientation = LinearLayout.HORIZONTAL
                layoutSeat.addView(layout)
            } else if (seatsPattenStr.get(index) == 'U') {
                val model = allLocalMatch.get(count)

                val view = TextView(requireActivity())
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seat_booked)
                view.setTextColor(Color.WHITE)
                view.tag = count
                view.text = model.seatNo
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                layout!!.addView(view)
                seatViewList.add(view)
                count++
                view.setOnClickListener(this)
            } else if (seatsPattenStr.get(index) == 'A') {
                val model = allLocalMatch.get(count)
                val view = TextView(activity?.applicationContext)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seat_avaliable)
                view.text = model.seatNo
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.BLACK)
                view.tag = count
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
                count++
            } else if (seatsPattenStr.get(index) == 'R') {
                val model = allLocalMatch.get(count)
                val view = TextView(requireActivity())
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seat_booked)
                view.text = model.seatNo
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.WHITE)
                view.tag = count
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
                count++
            } else if (seatsPattenStr.get(index) == '_') {
                val view = TextView(activity?.applicationContext)
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
        val model = allBusSeat.get(view.tag as Int)

        if (model.status.equals("Available")) {
            if (!model.isUserSeleted) {
                view.setBackgroundResource(R.drawable.ic_seat_seleted)
                val get = allBusSeat.get(view.tag as Int)
                get.isUserSeleted = true
                allBusSeat.set(view.tag as Int, get)
                updateSeatLayuout()
                Toast.makeText(requireContext(), "Seat " + model.seatNo + " is selected", Toast.LENGTH_SHORT).show()
            } else {
                view.setBackgroundResource(R.drawable.ic_seat_avaliable)
                val get = allBusSeat.get(view.tag as Int)
                get.isUserSeleted = false
                allBusSeat.set(view.tag as Int, get)
                updateSeatLayuout()
            }
        } else {
            Toast.makeText(requireContext(), "Seat " + model.seatNo + " already booked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateSeatLayuout() {
        updatePriceLayuout()

    }

    private fun updatePriceLayuout() {
        seatLevel = ""
        totalPrices = 0.0
        seatIds = ""

        allBusSeat.forEach {
            if (it.isUserSeleted) {
                seatLevel = seatLevel + it.seatNo + ","
                seatIds = seatIds + it.seatid + ","

                totalPrices = totalPrices + it.fare + viewMode.extraCharge.value!!

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

    override fun showNoTripFoundUI() {


    }

    override fun showErrorMessage(message: String) {

    }

    override fun showSeatCheckAndBookingRepose(it: ResSeatCheckBookAPI) {

    }

    override fun showShowConfirmDialog(it: ResposeTicketConfirm) {

    }

    override fun setBoardingPoint(allBoothNameInfo: MutableSet<String>) {

    }

    override fun saveRequestScheduledata(p: RequestScheduledata) {

    }

    override fun saveExtraCharge(double: Double) {

    }

    override fun showFilterList(filterTypeDepartingTime: List<ScheduleDataItem>) {


    }

    override fun updateListData(position: Int, itemCount: Int) {


    }

    override fun showProgress() {

    }

    override fun hiddenProgress() {

    }

    override fun showNoInternetConnectionFound() {

    }

    interface OnFragmentInteractionListener {
        fun onItemNextButtonClick(retrunTriple: Boolean)
    }


    override fun onBackPressed() {
        super.onBackPressed()

        val busHosttActivity = activity as BusHosttActivity
        if (!isRetrunTriple) {
            busHosttActivity.setToolbar("Departure Ticket", resources.getColor(R.color.bus_ticket_toolbar_title_text_color))
        } else {
            busHosttActivity.setToolbar("Return Ticket", resources.getColor(R.color.bus_ticket_toolbar_title_text_color))
        }

    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        Logger.v("onAttachFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.v("onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.v("onDestroyView")

    }

    override fun onStop() {
        super.onStop()
        Logger.v("onStop")
    }

    override fun onResume() {
        super.onResume()
        Logger.v("onResume")
    }


//    private fun displaySeatLayoutv1() {
//
//        val seatViewData = scheduleDataItem.resSeatInfo?.seatviewResponse?.seatViewData
//
//        val indexTotalColumes = seatViewData?.columnNo?.toInt() as Int
//        val totalRowsInt = seatViewData.rowNo?.toInt() as Int
//        val totalMatrix = totalRowsInt * indexTotalColumes
//        allBusSeat = seatViewData.seatstructureDetails as ArrayList<SeatstructureDetailsItem>
//
//        var columnsInRightInt = 0;
//        if (indexTotalColumes == 3) {
//            columnsInRightInt = 2
//        } else if (indexTotalColumes == 4) {
//            columnsInRightInt = 2
//        }
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
//                displaySeatPattenv2()
//            }
//        } catch (e: Exception) {
//            Logger.v("" + e.message)
//        }
//    }


    private fun getSeatSymbolWithSpace(i: Int): String {
        var data = "";
        if (allBusSeat.get(seatCounter).status.equals("Available")) {
            // available seats
            data = "A_"
        } else if (allBusSeat.get(seatCounter).status.equals("Booked")) {
            //Temp booked
            data = "R_"
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
        if (allBusSeat.get(seatCounter).status.equals("Available")) {
            // available seats
            data = "A"
        } else if (allBusSeat.get(seatCounter).status.equals("Booked")) {
            //Temp booked
            data = "R"
        } else {
            data = "U"
        }

        seatCounter = seatCounter + 1
        Logger.v("ICounter" + seatCounter)

        return data
    }

    private fun getSeatSymbolWithoutSpaceForLastLayout(i: Int): Any? {
        var data = "";
        if (lastSeatBusSeat.get(i).status.equals("Available")) {
            // available seats
            data = "A"
        } else if (lastSeatBusSeat.get(i).status.equals("Booked")) {
            //Temp booked
            data = "R"
        } else {
            data = "U"
        }
        return data
    }


}

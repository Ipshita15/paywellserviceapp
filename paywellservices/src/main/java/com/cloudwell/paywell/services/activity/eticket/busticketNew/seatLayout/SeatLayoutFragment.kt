package com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.*
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.RequestScheduledata
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata.ScheduleDataItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.seatview.SeatstructureDetailsItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout.adapter.CustomSpnerForBoardingPoint
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_seat_layout.*
import kotlinx.android.synthetic.main.bottom_seat_layout.view.*
import org.json.JSONObject
import java.text.DecimalFormat
import kotlin.collections.ArrayList

class SeatLayoutFragment(val scheduleDataItem: ScheduleDataItem, retrunTriple: Boolean) : Fragment() , View.OnClickListener, IbusTransportListView {

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
    var lastSeatBusSeat = mutableListOf<SeatstructureDetailsItem>()


    internal lateinit var model: TripScheduleInfoAndBusSchedule;
    internal lateinit var requestBusSearch: RequestBusSearch;

    var bus = Transport()

    var seatCounter = 0

    var seatLevel = ""
    var seatIds = ""
    var totalPrices = 0.0
    private lateinit var seatLayoutBottonSheet: ConstraintLayout
    private lateinit var busListAdapter: Any
    private lateinit var boothList:Spinner


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
        seatLayoutBottonSheet = view.seatLayoutBottonSheet
        initilizationReviewBottomSheet(seatLayoutBottonSheet)
//
        rootSeatLayout = view.findViewById<ViewGroup>(R.id.layoutSeat)
        boothList = view.findViewById(R.id.boothList)

        displaySeatLayoutv2()
        displaySeatPatten()
        setupBoardingpont()

        return view

    }




    private fun setupBoardingpont() {

        val values = scheduleDataItem.resSeatInfo?.seatviewResponse?.boothInfoHashMap?.values
        val valueList = ArrayList(values)


        busListAdapter = CustomSpnerForBoardingPoint(requireContext().applicationContext,valueList)
        boothList.setAdapter(busListAdapter as ArrayAdapter<String>)
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

    private fun displaySeatLayoutv2() {

        val seatViewData = scheduleDataItem.resSeatInfo?.seatviewResponse?.seatViewData

        val indexTotalColumes = seatViewData?.columnNo?.toInt() as Int
        val totalRowsInt = seatViewData.rowNo?.toInt() as Int
        //val totalMatrix = totalRowsInt * indexTotalColumes
        allBusSeat = seatViewData.seatstructureDetails as ArrayList<SeatstructureDetailsItem>


        var indexCounter = 0
        try {

            var row: Int
            var coloum: Int

                row = 0
                while (row < totalRowsInt) {
                    coloum = 0

                    seatsPattenStr = seatsPattenStr + "/"


                    while (coloum < indexTotalColumes) {
                        val indexI = row + 1
                        val indexJ = coloum + 1

                        val xy = "$indexI$indexJ"

                        val item = allBusSeat.get(indexCounter)

                        if (xy.equals("${item.xAxis}${item.yAxis}")){
                            Log.e("L:",""+item.seatNo)

                            seatsPattenStr = seatsPattenStr +getSeatSymbolWithSymbol(item)

                            indexCounter++
                        }else{
                            seatsPattenStr = seatsPattenStr + "_"
                        }
                        coloum++
                    }
                    seatsPattenStr = seatsPattenStr + "/"
                    row++
                }

        }catch (e: Exception){
            Log.e("", ""+e)
        }



    }

    private fun getSeatSymbolWithSymbol(item: SeatstructureDetailsItem): String {
        var data =""
        if (item.status.equals("Available")){
            data = "A"
        }else {
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

        seatLayoutBottonSheet.btNext.setOnClickListener{


            Log.e("", "")

            listener?.onItemNextButtonClick()


//            val toJson = Gson().toJson(model)
//            val requestBusSearchJson = Gson().toJson(requestBusSearch)
//
//            var counter = 0;
//
//            allBusSeat.forEach {
//                if (it.isUserSeleted) {
//                    counter++;
//                }
//            }
//
//            val transport = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.SELETED_BUS_INFO) as Transport
//
//
//            val busBaseTicketPrices = BusCalculationHelper.getPricesWithExtraAmount(model.busSchedule?.ticketPrice, requestBusSearch.date, transport = transport, isExtraAmount = false).toDouble()
//            val totalAPIValuePrices = busBaseTicketPrices.times(counter)
//
//
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

    private fun displaySeatPatten() {
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
                view.setText(model.seatNo)
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                layout!!.addView(view)
                seatViewList.add(view)
                count++
                view.setOnClickListener(this)
            } else if (seatsPattenStr.get(index) == 'A') {
                val model = allBusSeat?.get(count)
                val view = TextView(activity?.applicationContext)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seat_avaliable)
                view.setText(model.seatNo)
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.BLACK)
                view.tag = count
                layout!!.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
                count++
            } else if (seatsPattenStr.get(index) == 'R') {
                val model = allBusSeat?.get(count)
                val view = TextView(requireActivity())
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGaping)
                view.id = count
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.ic_seat_booked)
                view.setText(model.seatNo)
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
        //displaySeatLayout()
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

                totalPrices = totalPrices + it.fare+ viewMode.extraCharge.value!!

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

    override fun showShowConfirmDialog(it: ResPaymentBookingAPI) {

    }

    override fun setBoardingPoint(allBoothNameInfo: MutableSet<String>) {

    }

    override fun saveRequestScheduledata(p: RequestScheduledata) {

    }

    override fun saveExtraCharge(double: Double) {

    }

    override fun showProgress() {

    }

    override fun hiddenProgress() {

    }

    override fun showNoInternetConnectionFound() {

    }


    interface OnFragmentInteractionListener {
        fun onItemNextButtonClick()
    }

}

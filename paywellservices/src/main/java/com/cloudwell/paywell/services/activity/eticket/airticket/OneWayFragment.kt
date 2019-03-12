package com.cloudwell.paywell.services.activity.eticket.airticket


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
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
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.franmontiel.fullscreendialog.FullScreenDialogFragment
import kotlinx.android.synthetic.main.fragment_one_way.*
import mehdi.sakout.fancybuttons.FancyButton
import java.text.SimpleDateFormat
import java.util.*


class OneWayFragment : Fragment(), View.OnClickListener, FullScreenDialogFragment.OnConfirmListener, FullScreenDialogFragment.OnDiscardListener {

    private val KEY_TAG = OneWayFragment::class.java!!.getName()
    private val REQ_CODE_FROM = 1
    private val REQ_CODE_TO = 3

    private var mAppHandler: AppHandler? = null
    private lateinit var frameLayout: FrameLayout
    private lateinit var dialogFragment: FullScreenDialogFragment
    val dialogTag = "dialog"

    val myCalendar = Calendar.getInstance()

    companion object {
        val KEY_REQUEST_KEY = "KEY_REQUEST_KEY"
        val KEY_REQUEST_FOR_FROM = 1
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_one_way, container, false)

        frameLayout = view.findViewById(R.id.frameLayout)
        val tvDepart = view.findViewById<TextView>(com.cloudwell.paywell.services.R.id.tvDepart)
        val tvDepartDate = view.findViewById<TextView>(com.cloudwell.paywell.services.R.id.tvDepartDate)
        val airTicketClass = view.findViewById<TextView>(com.cloudwell.paywell.services.R.id.airTicketClass)
        val llPassenger = view.findViewById<LinearLayout>(com.cloudwell.paywell.services.R.id.llPsngr)
        val btnSearch = view.findViewById<FancyButton>(com.cloudwell.paywell.services.R.id.btn_search)
        val tvFrom = view.findViewById<LinearLayout>(com.cloudwell.paywell.services.R.id.tvFrom)
        val layoutTo = view.findViewById<LinearLayout>(com.cloudwell.paywell.services.R.id.layoutTo)

        mAppHandler = AppHandler.getmInstance(context)

        tvDepart.setOnClickListener(this)
        tvDepartDate.setOnClickListener(this)
        airTicketClass.setOnClickListener(this)
        llPassenger.setOnClickListener(this)
        btnSearch.setOnClickListener(this)
        tvFrom.setOnClickListener(this)
        layoutTo.setOnClickListener(this)

        val tsFrom = view.findViewById<TextSwitcher>(R.id.tsOneWayTripFrom)
        val tsFromPort = view.findViewById<TextSwitcher>(R.id.tsOneWayTripFromPort)
        val tsTo = view.findViewById<TextSwitcher>(R.id.tsOneWayTripTo)
        val tsToPort = view.findViewById<TextSwitcher>(R.id.tsOneWayTripToPort)
        val ivSwitchTrip = view.findViewById<ImageView>(R.id.ivOneWayTripTextSwitcher)

        tsFrom.setFactory {
            TextView(ContextThemeWrapper(context,
                    R.style.TicketFrom), null, 0)
        }
        tsFromPort.setFactory {
            TextView(ContextThemeWrapper(context,
                    R.style.TicketFromPort), null, 0)
        }
        tsTo.setFactory {
            TextView(ContextThemeWrapper(context,
                    R.style.TicketTo), null, 0)
        }
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

        tsFrom.setCurrentText("From")
        tsFromPort.setCurrentText("Shahjalal International")
        tsTo.setCurrentText("COX'S BAZAR")
        tsToPort.setCurrentText("Cox's Bazar airport")

        val textFrom = tsFrom.currentView as TextView
        val textFromPort = tsFromPort.currentView as TextView
        val textTo = tsTo.currentView as TextView
        val textToPort = tsToPort.currentView as TextView

        val searchRoundTripModel = SearchRoundTripModel(textFrom.text.toString(), textTo.text.toString(),
                textFromPort.text.toString(), textToPort.text.toString())

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

        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            com.cloudwell.paywell.services.R.id.tvDepart -> {
                showDepartDatePicker()
            }

            com.cloudwell.paywell.services.R.id.tvDepartDate -> {
                showDepartDatePicker()
            }


            com.cloudwell.paywell.services.R.id.airTicketClass -> {

                handleClass()

            }

            com.cloudwell.paywell.services.R.id.llPsngr -> {

                handlePassengerClick()
            }

            com.cloudwell.paywell.services.R.id.btn_search -> {

                val intent = Intent(activity?.applicationContext, FlightSearchViewActivity::class.java)
                startActivity(intent)
            }

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

            com.cloudwell.paywell.services.R.id.llDatePicker -> {
                DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    var date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {

        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                               dayOfMonth: Int) {
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel()
        }

    }

    private fun updateDateLabel() {
        val date = "yyyy-MM-dd"
        val myFormat = SimpleDateFormat(date, Locale.ENGLISH).parse(date) as Date


        val humanReadAbleDate = SimpleDateFormat("YYYY-mm-dd", Locale.ENGLISH).format(myFormat)

        val myFormatOne = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormatOne, Locale.ENGLISH)

//        tvDepartDate.setText(sdf.format(myCalendar.time))
        tvDepartDate.setText(humanReadAbleDate.format(myCalendar.time))
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
                onAdultPsngrTextChange(text)
            }

            override fun onKidButtonClickListener(text: String) {
                onKidPsngrTextChange(text)

            }

            override fun onAdultButtonClickListener(text: String) {
                onInfantPsngrTextChange(text)

            }

        })
        passengerBottomSheet.arguments = b
        passengerBottomSheet.show(fragmentManager, "psngrBottomSheet")
    }


    fun onAdultPsngrTextChange(text: String) {
        airTicketAdult.setText(text)
    }

    fun onKidPsngrTextChange(text: String) {
        airTicketKid.setText(text)
    }

    fun onInfantPsngrTextChange(text: String) {
        airTicketInfant.setText(text)
    }

    private fun handleClass() {
        val b = Bundle()
        b.putString("myClassName", airTicketClass.text.toString())

        val bottomSheet = ClassBottomSheetDialog()
        bottomSheet.setOnClassListener(object : ClassBottomSheetDialog.ClassBottomSheetListener {
            override fun onButtonClickListener(text: String) {

                airTicketClass.setText(text)
            }

        })

        bottomSheet.arguments = b
        bottomSheet.show(fragmentManager, "classBottomSheet")
    }

    private fun showDepartDatePicker() {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val thismonth = calendar.get(Calendar.MONTH) + 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->

                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    val date = calendar.getTime()

                    val nameOfDayOfWeek = SimpleDateFormat("EEE").format(date)
                    val nameOfMonth = SimpleDateFormat("MMM").format(calendar.getTime())

                    tvDepartDate.text = "$nameOfDayOfWeek, $day $nameOfMonth"
                    tvDepart.setTextColor(Color.BLACK);


                }, year, thismonth, dayOfMonth)

        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        calendar.add(Calendar.MONTH, 6)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis


        datePickerDialog.show()

    }

    fun onClassTextChange(text: String) {
        airTicketClass.setText(text)

    }


    private fun handleFromSearchClick() {

        val args = Bundle()
        args.putString("Name", "Naima")

        dialogFragment = FullScreenDialogFragment.Builder(context as AirTicketMainActivity)
                .setTitle("From")
                .setOnConfirmListener(this@OneWayFragment)
                .setOnDiscardListener(this@OneWayFragment)
                .setContent(SourceFragment::class.java, args)
                .build()

        dialogFragment.show(fragmentManager, dialogTag)
    }

    override fun onConfirm(result: Bundle?) {
    }

    override fun onDiscard() {
        dialogFragment.dismiss()
    }

//    private fun getSourceList() {
////        showProgressDialog()
//
////        val requestDistrict = RequestDistrict()
////        requestDistrict.setmUsername("" + mAppHandler.getImeiNo())
////        requestDistrict.setmBankId("" + bankId)
//
//        val responseBodyCall = ApiUtils.getAPIService().getAirports(mAppHandler?.getImeiNo(), requestDistrict.getmBankId())
//
//        responseBodyCall.enqueue(object : Callback<AirPortsData> {
//            override fun onResponse(call: Call<AirPortsData>, response: Response<AirPortsData>) {
////                dismissProgressDialog()
//                val bundle = Bundle()
//                bundle.putString("bankId", requestDistrict.getmBankId())
//
//                BankDetailsActivity.responseDistrictData = response.body()
//                startBankDetailsActivity(bundle)
//            }
//
//            override fun onFailure(call: Call<AirPortsData>, t: Throwable) {
////                dismissProgressDialog()
//                Log.d(KEY_TAG, "onFailure:")
//                val snackbar = Snackbar.make(frameLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG)
//                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
//                val snackBarView = snackbar.view
//                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
//                snackbar.show()
//            }
//        })
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_FROM -> when (resultCode) {
                    RESULT_OK -> {

                        val get = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.AIRPORT) as Airport

                        tsOneWayTripFrom.setText(get.city)
                        tsOneWayTripFromPort.setText(get.airportName)
                    }
                }
            }
        }
    }
}



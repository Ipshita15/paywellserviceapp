package com.cloudwell.paywell.services.activity.eticket.busticketNew.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransactionLog.BusTransactionLogActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.cencel.BusCancelActiivty
import com.cloudwell.paywell.services.activity.eticket.busticketNew.search.BusCitySearchActivity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.constant.IconConstant
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.cloudwell.paywell.services.utils.LanuageConstant.KEY_BANGLA
import com.cloudwell.paywell.services.utils.LanuageConstant.KEY_ENGLISH
import com.cloudwell.paywell.services.utils.StringConstant
import kotlinx.android.synthetic.main.activity_air_ticket_main_contain.btTransationLog
import kotlinx.android.synthetic.main.activity_air_ticket_main_contain.btViewTricket
import kotlinx.android.synthetic.main.activity_bus_tricket_menu.*
import java.util.*


class BusTicketMenuActivity : BusTricketBaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    val KEY_TAG = BusTicketMenuActivity::class.java.name
    val BOOKING_TAG = "BOOKING"
    val TRX_TAG = "TRX_LOG"


    lateinit var mConstraintLayout: ConstraintLayout


    internal var radioButton_five: RadioButton? = null
    internal var radioButton_ten: RadioButton? = null
    internal var radioButton_twenty: RadioButton? = null
    internal var radioButton_fifty: RadioButton? = null
    internal var radioButton_hundred: RadioButton? = null
    internal var radioButton_twoHundred: RadioButton? = null
    var selectedLimit = 5
    lateinit var cd: ConnectionDetector


    companion object {
        val KEY_LIMIT = "LIMIT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_tricket_menu)


        val isBusTicket = AppStorageBox.get(applicationContext, AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW) as Boolean
        if (isBusTicket) {
            setToolbar(getString(R.string.home_eticket_bus), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))
        }else{
            setToolbar(getString(R.string.launch), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))
        }


        btViewTricket.setOnClickListener(this)
        btTransationLog.setOnClickListener(this)
        btCancel.setOnClickListener(this)



        //callnewApiTest()


        cd = ConnectionDetector(AppController.getContext())
        mConstraintLayout = findViewById(R.id.constraintLayoutBookingList)
        mAppHandler = AppHandler.getmInstance(applicationContext)


        addRecentUsedList()


    }

    private fun addRecentUsedList() {
        val recentUsedMenu = RecentUsedMenu(StringConstant.KEY_home_bus, StringConstant.KEY_home_ticket, IconConstant.KEY_ic_ticket, 0, 34)
        addItemToRecentListInDB(recentUsedMenu)
    }

    private fun callnewApiTest() {
//1
//        var pojo = BusLunCityRequest()
//        pojo.deviceId = mAppHandler.androidID
//        pojo.username = mAppHandler.userName
//        pojo.transportType = "1"
//        BusTicketRepository().getbusAndLaunchCities(pojo)

//2
//        val p = GetScheduledata()
//        p.username = mAppHandler.userName
//        p.coachType = "all"
//        p.departingDate = "2020-04-26"
//        p.departingTime = "Morning"
//        p.departure = "Dhaka"
//        p.destination = "Chittagong"
//        p.deviceId = mAppHandler.androidID
//        p.returnCoachType = "any"
//        p.returnDate = "2020-04-27"
//        p.returnTime = "Night"
//        p.roundTrip = "1"
//        p.transportType ="1"
//        BusTicketRepository().getScheduleData(p)

////3
//        val po = GetSeatViewRquestPojo()
//        po.departureDate = "2020-04-26"
//        po.deviceId = mAppHandler.androidID
//        po.fromCity = "Dhaka"
//        po.toCity = "Chittagong"
//        po.optionId = "BBS_3897"
//        po.username = mAppHandler.userName
//            BusTicketRepository().getSeatView(po)
//
////4
//       val p4 =  GetSeatStatusRequest()
//        p4.departureDate = "2020-04-26"
//        p4.deviceId = mAppHandler.androidID
//        p4.fromCity = "Dhaka"
//        p4.optionId = "BBS_3897"
//        p4.seatData = "2,3"
//        p4.toCity = "Chittagong"
//        p4.username = mAppHandler.userName
//        BusTicketRepository().getSeatStatus(p4)
//
//
//       val p5 =  SeatBlockRequestPojo()
//        p5.deviceId = mAppHandler.androidID
//        p5.fromCity = "Dhaka"
//        p5.password = "12345"
//        p5.roundTrip = "2"
//        p5.toCity = "Chittagong"
//        p5.username = mAppHandler.userName
//        val opif = OptionInfoItem()
//        opif.boardingPointId = "110"
//        opif.departureDate = "2020-04-26"
//        opif.optionId = "BBS_3897"
//        opif.seat = "3"
//        p5.optionInfo?.add(opif)
//
//        val passenger = Passenger()
//        passenger.passengerAddress = "cloudWell"
//        passenger.passengerAge = "25"
//        passenger.passengerEmail = "sepon@gmail.com"
//        passenger.passengerGender = "male"
//        passenger.passengerMobile = "01912250477"
//        passenger.passengerName = "sepon"
//        p5.passenger = passenger
//        //BusTicketRepository().getseatBlock(p5)
//
//       val p6 =  CancelBookedTicketReques()
//        p6.deviceId = mAppHandler.androidID
//        p6.password = "12345"
//        p6.trxId = "PRB202004211217214235"
//        p6.username =  mAppHandler.userName
//        //BusTicketRepository().getcancelBookedTicket(p6)
//
//       val p7 =  ConfirmTicketRquestPojo()
//        p7.deviceId = mAppHandler.androidID
//        p7.password = "12345"
//        p7.trxId = "PRB202004211353153450"
//        p7.username =  mAppHandler.userName
//        BusTicketRepository().getconfirmTicket(p7)
//
//
//       val p8 =  TicketInformationForCancelRequest()
//        p8.deviceId = mAppHandler.androidID
//        p8.password = "12345"
//        p8.trxId = "PRB202004211353153450"
//        p8.username =  mAppHandler.userName
//        BusTicketRepository().getticketInformationForCancel(p8)
//
//
//
//        val p9 =  CancelTicketRequest()
//        p9.deviceId = mAppHandler.androidID
//        p9.password = "12345"
//        p9.trxId = "PRB202004211217214235"
//        p9.username =  mAppHandler.userName
//        p9.ticketNo = ""

//        BusTicketRepository().getcancelTicket(p9)




    }


    override fun onResume() {
        super.onResume()
        selectedLimit = 5
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btViewTricket -> {
                startActivity(Intent(applicationContext, BusCitySearchActivity::class.java))
            }


            R.id.btTransationLog -> {
                showLimitPrompt(TRX_TAG)

            }

            R.id.btBooking -> {

                showLimitPrompt(BOOKING_TAG)

            }
            R.id.btCancel -> {
                startActivity(Intent(applicationContext, BusCancelActiivty::class.java))
            }
        }
    }


    private fun showReposeUI(response: BookingList) {
        val builder = AlertDialog.Builder(this@BusTicketMenuActivity)
        builder.setTitle("Result")
        builder.setMessage(response.message)
        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            dialogInterface.dismiss()
        }
        val alert = builder.create()
        alert.show()

    }

    private fun showLimitPrompt(tag: String) {
        val dialog = AppCompatDialog(this)
        if (tag.equals(BOOKING_TAG)) {
            dialog.setTitle(R.string.book)
        } else if (tag.equals(TRX_TAG)) {
            dialog.setTitle(R.string.booking_log_limit_title_msg)
        }

        dialog.setContentView(R.layout.dialog_trx_limit)

        val btn_okay = dialog.findViewById<Button>(R.id.buttonOk)
        val btn_cancel = dialog.findViewById<Button>(R.id.cancelBtn)

        radioButton_five = dialog.findViewById(R.id.radio_five)
        radioButton_ten = dialog.findViewById(R.id.radio_ten)
        radioButton_twenty = dialog.findViewById(R.id.radio_twenty)
        radioButton_fifty = dialog.findViewById(R.id.radio_fifty)
        radioButton_hundred = dialog.findViewById(R.id.radio_hundred)
        radioButton_twoHundred = dialog.findViewById(R.id.radio_twoHundred)

        radioButton_five?.setOnCheckedChangeListener(this)
        radioButton_ten?.setOnCheckedChangeListener(this)
        radioButton_twenty?.setOnCheckedChangeListener(this)
        radioButton_fifty?.setOnCheckedChangeListener(this)
        radioButton_hundred?.setOnCheckedChangeListener(this)
        radioButton_twoHundred?.setOnCheckedChangeListener(this)

        assert(btn_okay != null)
        btn_okay!!.setOnClickListener {
            dialog.dismiss()
            if (isInternetConnection) {

                val intent = Intent(application, BusTransactionLogActivity::class.java)
                intent.putExtra(KEY_LIMIT, selectedLimit)
                startActivity(intent)

            } else {
                showNoInternetConnectionFound()
            }


        }
        assert(btn_cancel != null)
        btn_cancel!!.setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(true)
        dialog.show()
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            if (buttonView.id == R.id.radio_five) {
                selectedLimit = 5
                radioButton_ten?.isChecked = false
                radioButton_twenty?.isChecked = false
                radioButton_fifty?.isChecked = false
                radioButton_hundred?.isChecked = false
                radioButton_twoHundred?.isChecked = false
            }
            if (buttonView.id == R.id.radio_ten) {
                selectedLimit = 10
                radioButton_five?.isChecked = false
                radioButton_twenty?.isChecked = false
                radioButton_fifty?.isChecked = false
                radioButton_hundred?.isChecked = false
                radioButton_twoHundred?.isChecked = false
            }
            if (buttonView.id == R.id.radio_twenty) {
                selectedLimit = 20
                radioButton_five?.isChecked = false
                radioButton_ten?.isChecked = false
                radioButton_fifty?.isChecked = false
                radioButton_hundred?.isChecked = false
                radioButton_twoHundred?.isChecked = false
            }
            if (buttonView.id == R.id.radio_fifty) {
                selectedLimit = 50
                radioButton_five?.isChecked = false
                radioButton_ten?.isChecked = false
                radioButton_twenty?.isChecked = false
                radioButton_hundred?.isChecked = false
                radioButton_twoHundred?.isChecked = false
            }
            if (buttonView.id == R.id.radio_hundred) {
                selectedLimit = 100
                radioButton_five?.isChecked = false
                radioButton_ten?.isChecked = false
                radioButton_twenty?.isChecked = false
                radioButton_fifty?.isChecked = false
                radioButton_twoHundred?.isChecked = false
            }
            if (buttonView.id == R.id.radio_twoHundred) {
                selectedLimit = 200
                radioButton_five?.isChecked = false
                radioButton_ten?.isChecked = false
                radioButton_twenty?.isChecked = false
                radioButton_fifty?.isChecked = false
                radioButton_hundred?.isChecked = false
            }
        }
    }

    override fun onBackPressed() {

        val isEnglish = mAppHandler.appLanguage.equals("en", ignoreCase = true)
        if (isEnglish) {
            switchToCzLocale(Locale(KEY_ENGLISH, ""))
        } else {
            switchToCzLocale(Locale(KEY_BANGLA, ""))
        }


        super.onBackPressed()
    }
}

package com.cloudwell.paywell.services.activity.eticket.airticket

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextSwitcher
import android.widget.TextView
import com.cloudwell.paywell.services.R
import kotlinx.android.synthetic.main.fragment_one_way.*

class RoundTripFragment : Fragment() {

    lateinit var tvClass: TextView
    lateinit var tvAdult: TextView
    lateinit var tvKid: TextView
    lateinit var tvInfant: TextView
    lateinit var llPassenger: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_round_trip, container, false)

        val tsFrom = view.findViewById<TextSwitcher>(R.id.tsRoundTripFrom)
        val tsFromPort = view.findViewById<TextSwitcher>(R.id.tsRoundTripFromPort)
        val tsTo = view.findViewById<TextSwitcher>(R.id.tsRoundTripTo)
        val tsToPort = view.findViewById<TextSwitcher>(R.id.tsRoundTripToPort)
        val ivSwitchTrip = view.findViewById<ImageView>(R.id.ivRoundTripTextSwitcher)
        tvClass = view.findViewById(R.id.airTicketClass)
        llPassenger = view.findViewById(R.id.llPsngr)
        tvAdult = view.findViewById(R.id.airTicketAdult)
        tvKid = view.findViewById(R.id.airTicketKid)
        tvInfant = view.findViewById(R.id.airTicketInfant)

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

        tsFrom.setCurrentText("Dhaka")
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

        tvClass.setOnClickListener {


            handleClass()
        }

        llPassenger.setOnClickListener {

            handlePassengerClick()
        }
        return view
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

    fun onClassTextChange(text: String) {
        tvClass.setText(text)
    }

    fun onAdultPsngrTextChange(text: String) {
        tvAdult.setText(text)
    }

    fun onKidPsngrTextChange(text: String) {
        tvKid.setText(text)
    }

    fun onInfantPsngrTextChange(text: String) {
        tvInfant.setText(text)
    }
}

package com.cloudwell.paywell.services.activity.eticket.airticket


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextSwitcher
import android.widget.TextView
import com.cloudwell.paywell.services.R

class OneWayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_one_way, container, false)

        val tsFrom = view.findViewById<TextSwitcher>(R.id.tsRoundTripFrom)
        val tsFromPort = view.findViewById<TextSwitcher>(R.id.tsRoundTripFromPort)
        val tsTo = view.findViewById<TextSwitcher>(R.id.tsRoundTripTo)
        val tsToPort = view.findViewById<TextSwitcher>(R.id.tsRoundTripToPort)

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

        return view
    }


}

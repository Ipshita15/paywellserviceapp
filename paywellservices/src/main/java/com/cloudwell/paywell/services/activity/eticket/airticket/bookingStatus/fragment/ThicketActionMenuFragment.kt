package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.fragment_tricket_action_menu.view.*


class ThicketActionMenuFragment : DialogFragment() {


    lateinit var onClickHandler: OnClickHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_tricket_action_menu, container, false)


        val model = AppStorageBox.get(activity, AppStorageBox.Key.BOOKING_STATUS_ITEM) as Datum

        if (model.message.equals("Booked")) {
            v.btActionIssueTicket.visibility = View.VISIBLE

        } else {
            v.btActionIssueTicket.visibility = View.GONE
            setMargins(v.btCencel, 0, 50, 0, 50)

        }


        v.btActionIssueTicket.setOnClickListener {
            dismiss()
            onClickHandler.onClickIssisTricketButton()
        }

        v.btCencel.setOnClickListener {
            dismiss()
            onClickHandler.onClickCencelButton()
        }



        return v
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }


    interface OnClickHandler {
        fun onClickCencelButton()
        fun onClickIssisTricketButton()

    }

}

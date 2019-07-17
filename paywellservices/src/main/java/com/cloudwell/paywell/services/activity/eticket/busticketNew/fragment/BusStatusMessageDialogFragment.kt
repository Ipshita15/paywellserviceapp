package com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity.Companion.KEY_ReIssue
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity.Companion.KEY_ReSchedule
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity.Companion.KEY_ticket_cancel
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model.ResCancellationMapping
import kotlinx.android.synthetic.main.fragment_prices_change.view.*


class BusStatusMessageDialogFragment : DialogFragment() {


    companion object {
        lateinit var resCencelMaping: ResCancellationMapping
        lateinit var type: String
    }

    lateinit var onClickHandler: OnClickHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_cancel_fee, container, false)
        v.btActionIssueTicket.text = type

        val void = resCencelMaping.getCancelData().void.toDouble()
        val count = resCencelMaping.bookingData.get(0).passengers.count()
        val reIssue = resCencelMaping.cancelData.reIssue.toDouble()
        val reSchedule = resCencelMaping.cancelData.reSchedule.toDouble()
        val refund = resCencelMaping.cancelData.refund.toDouble()

        var totalFee = 0.0
        if (type.equals(KEY_ReIssue)) {
            totalFee = reIssue
            v.tvYourSeats.text = "Admin charges tk. ${totalFee} and other charges will be applicable for the reissue process."

        } else if (type.equals(KEY_ReSchedule)) {
            totalFee = reSchedule
            v.tvYourSeats.text = "Admin charges tk. ${totalFee} and other charges will be applicable for the reschedule process."

        } else if (type.equals(KEY_ticket_cancel)) {
            totalFee = refund
            v.tvYourSeats.text = "Admin charges tk. ${totalFee} and other charges will be applicable for the ticket cancellation process."

        }

        v.btActionIssueTicket.setOnClickListener {
            dismiss()
            onClickHandler.onClickActionIssueTicket(totalFee, type)
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
        fun onClickActionIssueTicket(cancellationFee: Double, type: String)

    }

}

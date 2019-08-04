package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity.Companion.KEY_ReIssue
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity.Companion.KEY_ReSchedule
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity.Companion.KEY_ticket_cancel
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model.ResCancellationMapping
import kotlinx.android.synthetic.main.fragment_cancel_fee.view.*
import kotlinx.android.synthetic.main.fragment_prices_change.view.btActionIssueTicket
import kotlinx.android.synthetic.main.fragment_prices_change.view.tvFree


class UserAcceptDialogFragment : DialogFragment() {


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
        v.btActionIssueTicket.text = getString(R.string.text_accept)
        v.btDecline.text = getString(R.string.text_decline)

        val void = resCencelMaping.getCancelData().void.toDouble()
        val count = resCencelMaping.bookingData.get(0).passengers.count()
        val reIssue = resCencelMaping.cancelData.reIssue.toDouble()
        val reSchedule = resCencelMaping.cancelData.reSchedule.toDouble()
        val refund = resCencelMaping.cancelData.refund.toDouble()

        var totalFee = 0.0
        if (type.equals(KEY_ReIssue)) {
            totalFee = reIssue
            v.tvFree.text = "Admin charge tk. ${totalFee} will be applicable for the re-issue process."

        } else if (type.equals(KEY_ReSchedule)) {
            totalFee = reSchedule
            v.tvFree.text = "Admin charge tk. ${totalFee} will be applicable for the reschedule process."

        } else if (type.equals(KEY_ticket_cancel)) {
            totalFee = refund
            v.tvFree.text = "Admin charge tk. ${totalFee}  will be applicable for the ticket cancellation process."

        }

        v.btActionIssueTicket.setOnClickListener {
            dismiss()
            onClickHandler.onClickActionIssueTicket(totalFee, type)
        }

        v.btDecline.setOnClickListener {
            dismiss()
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

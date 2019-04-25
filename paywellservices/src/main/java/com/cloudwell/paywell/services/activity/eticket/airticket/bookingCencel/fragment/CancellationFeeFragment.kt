package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model.ResCancellationMapping
import kotlinx.android.synthetic.main.fragment_prices_change.view.*


class CancellationFeeFragment : DialogFragment() {


    companion object {
        lateinit var resCencelMaping: ResCancellationMapping
    }

    lateinit var onClickHandler: OnClickHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_cancel_fee, container, false)

        val refund = resCencelMaping.getCancelData().getRefund()
        val bookingDatum = resCencelMaping.getBookingData().get(0)
        val cancellationFee = java.lang.Double.parseDouble(refund) * java.lang.Double.parseDouble(bookingDatum.getAdultQty()) + java.lang.Double.parseDouble(bookingDatum.getChildQty()) + java.lang.Double.parseDouble(bookingDatum.getInfantQty())

        v.tvFree.text = getString(R.string.cancellation_fee) + " Tk. " + cancellationFee


        v.btActionIssueTicket.setOnClickListener {
            dismiss()
            onClickHandler.onClickActionIssueTicket(cancellationFee)
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
        fun onClickActionIssueTicket(cancellationFee: Double)

    }

}

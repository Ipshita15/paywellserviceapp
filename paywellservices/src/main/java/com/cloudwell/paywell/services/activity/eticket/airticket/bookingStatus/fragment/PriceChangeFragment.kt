package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.service.CalculationHelper
import com.cloudwell.paywell.services.R
import kotlinx.android.synthetic.main.fragment_prices_change.view.*


class PriceChangeFragment : DialogFragment() {


    companion object {

    }

    lateinit var onClickHandler: OnClickHandler
    lateinit var modelPriceChange: List<com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.Datum>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_prices_change, container, false)

        v.tvNewPrices.text = getString(R.string.new_ticket_prices) + " " + CalculationHelper.getTotalFareDetati(modelPriceChange.get(0).fares)


        v.btActionIssueTicket.setOnClickListener {
            dismiss()
            onClickHandler.onClickActionIssueTicket()
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
        fun onClickActionIssueTicket()

    }

}

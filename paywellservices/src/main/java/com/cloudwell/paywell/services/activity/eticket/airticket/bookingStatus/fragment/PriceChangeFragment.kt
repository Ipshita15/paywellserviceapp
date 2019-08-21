package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.ResIssueTicket
import kotlinx.android.synthetic.main.fragment_prices_change.view.*


class PriceChangeFragment : DialogFragment() {


    companion object {

    }

    lateinit var onClickHandler: OnClickHandler
    lateinit var modelPriceChange: ResIssueTicket


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_prices_change, container, false)

        v.tvPrices.text = getString(R.string.old_ticket_prices) + " " + modelPriceChange.total_fare_calculated
        v.tvFree.text = getString(R.string.new_ticket_prices) + " " + modelPriceChange.total_fare_calculated_new


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

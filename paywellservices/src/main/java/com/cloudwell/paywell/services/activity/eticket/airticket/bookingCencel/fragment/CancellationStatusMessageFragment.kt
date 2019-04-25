package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_prices_change.view.*


class CancellationStatusMessageFragment : DialogFragment() {


    companion object {
        lateinit var message: String
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_status_cencelation, container, false)

        v.tvFree.text = message


        v.btActionIssueTicket.setOnClickListener {
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


}

package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_prices_change.view.*


class ShowMessageFragment() : DialogFragment() {


    companion object {
        lateinit var message: String
    }

    var mListener: MyInterface? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_status_cencelation, container, false)
        isCancelable = false

        v.tvFree.text = message
        v.btActionIssueTicket.setOnClickListener {
            dismiss()
            mListener?.onOkButtonClick()

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

    interface MyInterface {
        fun onOkButtonClick()
    }


}

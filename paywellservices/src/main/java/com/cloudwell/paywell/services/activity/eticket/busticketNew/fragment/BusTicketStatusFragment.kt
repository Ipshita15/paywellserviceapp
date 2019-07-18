package com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import kotlinx.android.synthetic.main.fragment_prices_change.view.*


class BusTicketStatusFragment : DialogFragment() {

    companion object {
        lateinit var message: String
    }

    private var onClicklistener: OnClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickListener(onClicklistener: OnClickListener) {
        this.onClicklistener = onClicklistener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_bus_status_fragment, container, false)
        v.tvYourSeats.text = message

        v.btAction.setOnClickListener {
            dismiss()
            onClicklistener?.onClick()
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

public interface OnClickListener {
    fun onClick()
}

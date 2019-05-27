package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_tricket_chooser.view.*


class TricketChooserFragment : DialogFragment() {

    lateinit var onClickHandler: OnClickHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_tricket_chooser, container, false)
        v.btCencel.setOnClickListener {
            dismiss()
            onClickHandler.onClick("view")
        }

        v.btSendEmail.setOnClickListener {
            dismiss()
            onClickHandler.onClick("email")
        }
        return v
    }


    interface OnClickHandler {
        fun onClick(s: String);

    }

}

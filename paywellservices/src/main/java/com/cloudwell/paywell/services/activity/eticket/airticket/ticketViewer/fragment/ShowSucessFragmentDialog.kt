package com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_tricket_chooser.view.*


class ShowSucessFragmentDialog : DialogFragment() {

    lateinit var onClickHandler: OnClickHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_tricket_respose, container, false)
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

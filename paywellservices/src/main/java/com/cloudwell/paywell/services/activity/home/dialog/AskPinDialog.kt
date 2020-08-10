package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.ask_mobile_number_dialog.view.cancel_button
import kotlinx.android.synthetic.main.ask_mobile_number_dialog.view.ok_button
import kotlinx.android.synthetic.main.ask_pin_dialog.view.*


class AskPinDialog(val onclick: getPinInterface, val getFinisedInterface: GetFinisedInterface) : BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.ask_pin_dialog, null)


        view.ok_button.setTextColor(resources.getColor(R.color.bongo_pay_btn))
        view.ok_button.setOnClickListener(View.OnClickListener {

            if (view.pin_et.text.length != 0) {
                if (onclick != null) {
                    onclick.onclick(view.pin_et.text.toString())
                }
                dialog.dismiss()
            }


        })

        view.cancel_button.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            getFinisedInterface.onclick()
        })


        return view

    }

    override fun onStart() {
        super.onStart()

        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.getWindow().setLayout(width, height)
        }
    }

    interface getPinInterface {
        fun onclick(pinNumber: String)
    }


    interface GetFinisedInterface {
        fun onclick()
    }


}
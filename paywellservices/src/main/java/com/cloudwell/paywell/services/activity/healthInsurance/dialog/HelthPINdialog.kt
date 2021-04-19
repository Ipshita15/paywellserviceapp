package com.cloudwell.paywell.services.activity.healthInsurance.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import com.cloudwell.paywell.services.activity.education.PaywellPinDialog
import kotlinx.android.synthetic.main.helth_pin_dialog.*
import kotlinx.android.synthetic.main.helth_pin_dialog.view.*

class HelthPINdialog(val onclick : PaywellPinDialog.IonClickInterface) : BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.helth_pin_dialog, null)

        view.next_btn.setOnClickListener {

            if(onclick != null){
                var pin : String = pinfor_helth.text.toString()
                onclick.onclick(pin)
                dismiss()
            }


        }
        return view

    }

    override fun onStart() {
        super.onStart()

        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.getWindow()?.setLayout(width, height)
        }
    }

    interface IonClickInterface {
        fun onclick(pin : String)
    }

}
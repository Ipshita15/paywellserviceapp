package com.cloudwell.paywell.services.activity.education

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.otp_error_msg_dialog.view.*
import kotlinx.android.synthetic.main.paywell_pin_dialog.*
import kotlinx.android.synthetic.main.paywell_pin_dialog.view.*
import kotlinx.android.synthetic.main.paywell_pin_dialog.view.paywell_pin

/**
 * Created by Sepon on 3/24/2020.
 */
class PaywellPinDialog (val message: String, val onclick : IonClickInterface): BaseDialogFragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.paywell_pin_dialog, null)

        view.msg.setText(message)
        view.reg_btn.setOnClickListener {

            if(onclick != null){
                var pin : String = paywell_pin.text.toString()
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
            dialog.getWindow().setLayout(width, height)
        }
    }

    interface IonClickInterface {
        fun onclick(pin : String)
    }
}
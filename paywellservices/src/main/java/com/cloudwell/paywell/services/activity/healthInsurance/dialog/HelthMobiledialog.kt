package com.cloudwell.paywell.services.activity.healthInsurance.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.helth_pin_dialog.*
import kotlinx.android.synthetic.main.helth_pin_dialog.view.*

class HelthMobiledialog(val onclick : HelthMobiledialog.MobileInterface) : BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.helth_pin_dialog, null)

        view.next_btn.setOnClickListener {

            val pin : String = mobile_helth.text.toString()
            if (pin.isNullOrEmpty()){
                mobile_helth.setError("Required")
            }
//            else if (pin.length >= 11 || pin.length <= 11){
//                mobile_helth.setError("Invlaid")
//            }
            else{

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

    interface MobileInterface {
        fun onclick(pin : String)
    }

}
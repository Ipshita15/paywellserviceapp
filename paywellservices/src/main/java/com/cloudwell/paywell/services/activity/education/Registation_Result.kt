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
import kotlinx.android.synthetic.main.paywell_pin_dialog.view.msg
import kotlinx.android.synthetic.main.paywell_pin_dialog.view.paywell_pin
import kotlinx.android.synthetic.main.reg_result.view.*

/**
 * Created by Sepon on 3/24/2020.
 */
class Registation_Result (val onclick : IonClickInterface): BaseDialogFragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.reg_result, null)

        view.reg_dialog_btn.setOnClickListener {
            dismiss()
            if(onclick != null){
                onclick.onclick()
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
        fun onclick()
    }


}
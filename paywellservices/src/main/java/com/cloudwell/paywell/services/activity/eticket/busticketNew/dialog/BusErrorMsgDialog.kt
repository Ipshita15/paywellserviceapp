package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.otp_error_msg_dialog.view.*


class BusErrorMsgDialog(val message: String, val needFinishedActivity: Boolean = false) : BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.bus_ticket_error_msg_dialog, null)

        view.otpErrorMsgTV.text = message
        view.btnOtpErrorCall.setOnClickListener {
            dismiss()
            if (needFinishedActivity) {
                activity!!.finish()
            }
        }

        isCancelable = false
        return view

    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }


}
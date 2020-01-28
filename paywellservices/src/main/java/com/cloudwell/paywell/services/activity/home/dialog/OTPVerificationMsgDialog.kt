package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.R
import kotlinx.android.synthetic.main.otp_verification_msg_dialog.view.*

class OTPVerificationMsgDialog(val onClickHandler: OnClickHandler, val  message: String): DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.otp_verification_msg_dialog, null)
        isCancelable = false
        view.otpOkMsg.setOnClickListener {
            dismiss()
            onClickHandler.onSubmit()

        }

        view.otpVerificationMsgTV.text = ""+message
        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    public interface OnClickHandler{

        public fun onSubmit();
    }

}
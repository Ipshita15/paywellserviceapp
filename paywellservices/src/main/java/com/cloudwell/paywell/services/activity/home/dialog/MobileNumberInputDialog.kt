package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import com.cloudwell.paywell.services.app.AppHandler
import kotlinx.android.synthetic.main.mobile_number_dialog.*
import kotlinx.android.synthetic.main.mobile_number_dialog.view.*


class MobileNumberInputDialog(val onClickHandler: OnClickHandler) : BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.mobile_number_dialog, null)

        view.btbtSubmitMobileNumber.setOnClickListener {

            val mobileNumber = view.etMobileOrRID.text
            onClickHandler.onSubmit(mobileNumber.toString().trim(), etPin.text.toString().trim())

        }

        view.btForgetPinNumber.setOnClickListener {
            dismiss()
            onClickHandler.onForgetPinNumber()
        }

//        view.etMobileOrRID.setText("01675349882")
//        view.etPin.setText("1234")


        val userName = AppHandler.getmInstance(activity?.applicationContext).userName
        userName.let {
            if (it.equals("unknown")) {

            } else {
                view.etMobileOrRID.setText(it)
            }
        }


        return view

    }


    public interface OnClickHandler {

        public fun onSubmit(mobileNumber: String, pin: String)
        public fun onForgetPinNumber();
    }

}
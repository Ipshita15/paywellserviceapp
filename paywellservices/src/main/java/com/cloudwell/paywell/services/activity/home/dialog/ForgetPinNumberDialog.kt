package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.mobile_number_dialog.view.*


class ForgetPinNumberDialog(val onClickHandler: OnClickHandler): BaseDialogFragment() {

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.forget_pin_number, null)

        view.btForgetPinNumber.setOnClickListener {

            val last4DigitOfMobiile = view.etMobileOrRID.text.toString().trim()

            if (last4DigitOfMobiile.equals("")){
                Toast.makeText(activity, getString(R.string.invalid_input), Toast.LENGTH_LONG).show()
            }else{
                onClickHandler.onForgetPinNumber(view.etMobileOrRID.text.toString().trim())
            }


        }

        return view

    }





    public interface OnClickHandler{
        public fun onForgetPinNumber( moibleNumber: String)
    }

}
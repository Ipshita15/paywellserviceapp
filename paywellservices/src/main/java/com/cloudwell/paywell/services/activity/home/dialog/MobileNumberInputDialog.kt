package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.mobile_number_dialog.*
import kotlinx.android.synthetic.main.mobile_number_dialog.view.*


class MobileNumberInputDialog(val onClickHandler: OnClickHandler): BaseDialogFragment() {

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.mobile_number_dialog, null)

        view.btbtSubmitMobileNumber.setOnClickListener {

            val mobileNumber = view.etMobileOrRID.text
            onClickHandler?.onSubmit(mobileNumber.toString().trim(),etPin.text.toString().trim() )

        }

        view.etMobileOrRID.setText("01675349882")
        view.etPin.setText("1234")
        return view

    }





    public interface OnClickHandler{

        public fun onSubmit(mobileNumber:String, pin:String);
    }

}
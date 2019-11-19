package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.R
import kotlinx.android.synthetic.main.mobile_number_dialog.view.*

class OTPInputDialog(val onClickHandler: OnClickHandler): DialogFragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.otp_dialog, null)
        view.btbtSubmitMobileNumber.setOnClickListener {

            val mobileNumber = view.etMobileOrRID.text
            onClickHandler?.onSubmit(mobileNumber.toString().trim())



        }
        view.etMobileOrRID.setText("8970")

        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    public interface OnClickHandler{

        public fun onSubmit(mobileNumber:String);
    }

}
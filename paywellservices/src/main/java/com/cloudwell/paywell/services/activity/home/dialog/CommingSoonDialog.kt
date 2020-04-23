package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.banner_comming_soon.view.*


class CommingSoonDialog() : BaseDialogFragment() {


    private val roundEadius = 16


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.banner_comming_soon, null)

        view.btCacelDialog.setOnClickListener {
            dismiss()
        }

        return view

    }


    public interface OnClickHandler {

        public fun onSubmit(mobileNumber: String, pin: String)
        public fun onForgetPinNumber();
    }

}
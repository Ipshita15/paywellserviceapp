package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.ask_mobile_number_dialog.view.*


class AskMobileNumberDialog(val onclick : getNumberClickInterface): BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.ask_mobile_number_dialog, null)

        view.mobile_et.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (view.mobile_et.text.length == 11){
                    view.ok_button.setTextColor(resources.getColor(R.color.bongo_pay_btn))
                    view.ok_button.setOnClickListener(View.OnClickListener {
                        if(onclick != null){
                            onclick.onclick(view.mobile_et.text.toString())
                            }
                        dialog.dismiss()
                    })
                }else{
                    view.ok_button.setTextColor(resources.getColor(R.color.new_text_clr))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
        view.cancel_button.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
        })


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

    interface getNumberClickInterface {
        fun onclick(mobileNumber: String)
    }




}
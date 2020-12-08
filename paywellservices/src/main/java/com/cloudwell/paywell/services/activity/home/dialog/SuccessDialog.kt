package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.success_dialog.view.*


class SuccessDialog(val title: String,val message: String, val onclick : successInterface): BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.success_dialog, null)

        view.title_txt.text = title
        view.message.text = message
        view.submit_button.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
            onclick.onclick()
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



}

interface successInterface {
    fun onclick()
}

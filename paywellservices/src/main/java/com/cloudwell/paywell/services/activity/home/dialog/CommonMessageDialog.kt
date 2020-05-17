package com.cloudwell.paywell.services.activity.home.dialog

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.successInterface
import kotlinx.android.synthetic.main.common_dialog.view.*
import kotlinx.android.synthetic.main.success_dialog.view.*
import kotlinx.android.synthetic.main.success_dialog.view.message
import kotlinx.android.synthetic.main.success_dialog.view.submit_button
import kotlinx.android.synthetic.main.success_dialog.view.title_txt

/**
 * Created by Sepon on 5/4/2020.
 */
class CommonMessageDialog(val title: String,val message: String,val msg_clr : Int, val onclick : CommonDialogBtnInterface): BaseDialogFragment()  {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.common_dialog, null)

        view.title_txt.text = title
        view.message.setTextColor(msg_clr)
        view.message.text = message
        view.submit_button.setOnClickListener(View.OnClickListener {

            if(onclick != null){
                onclick.onclick()
                dismiss()
            }
        })
        view.dismiss_btn.setOnClickListener(View.OnClickListener {
            if(onclick != null){
                onclick.onDismiss()
                dismiss()
            }
        })

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



}

interface CommonDialogBtnInterface {
    fun onclick()
    fun onDismiss()
}
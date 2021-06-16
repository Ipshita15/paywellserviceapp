package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.no_internet_connection_msg_dialog.view.*


class NoInternetConnectionMsgDialog(val onClickHandler: OnClickHandler): BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.no_internet_connection_msg_dialog, null)
        view.btRetry.setOnClickListener {
            dismiss()
            onClickHandler.onRetry();

        }
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

    public interface OnClickHandler{
        public fun onRetry()
    }




}
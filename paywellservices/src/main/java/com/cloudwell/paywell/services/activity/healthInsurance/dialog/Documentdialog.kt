package com.cloudwell.paywell.services.activity.healthInsurance.dialog

import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.document_dialog.view.*
import kotlinx.android.synthetic.main.helth_pin_dialog.*
import kotlinx.android.synthetic.main.helth_pin_dialog.view.*


class Documentdialog(val onclick: Documentdialog.DocClickInterface) : BaseDialogFragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.document_dialog, null)


        view.nid_btn.setOnClickListener(this)
        view.passport_btn.setOnClickListener(this)
        view.birth_certificate.setOnClickListener(this)




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

    interface DocClickInterface {
        fun onclick(document: String)
    }

    override fun onClick(v: View?) {


        when (v!!.id) {
            R.id.nid_btn -> onclick.onclick(getString(R.string.nid_paper))
            R.id.passport_btn -> onclick.onclick(getString(R.string.passport))
            R.id.birth_certificate -> onclick.onclick(getString(R.string.helth_birth_certificate))

        }

        dismiss()

    }

}
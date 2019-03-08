package com.cloudwell.paywell.services.activity.eticket.airticket


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.franmontiel.fullscreendialog.FullScreenDialogContent
import com.franmontiel.fullscreendialog.FullScreenDialogController

class SourceFragment : Fragment(), FullScreenDialogContent {

    private var dialogController: FullScreenDialogController? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_source, container, false)
    }

    override fun onConfirmClick(dialogController: FullScreenDialogController?): Boolean {
        return true
    }

    override fun onDialogCreated(dialogController: FullScreenDialogController?) {
        this.dialogController = dialogController
    }

    override fun onDiscardClick(dialogController: FullScreenDialogController?): Boolean {
        dialogController!!.discard()
        return true
    }
}

package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.PalliBidyutBillPayResponse






/**
 * Created by Yasin Hosain on 10/16/2019.
 * yasinenubd5@gmail.com
 */
class BillPayResponseDialog(palliBidyutBillPayResponse: PalliBidyutBillPayResponse,context: Context): DialogFragment() {

    val  data=palliBidyutBillPayResponse
    val  mContext=context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  layoutInflater.inflate(R.layout.mobile_number_dialog,null)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
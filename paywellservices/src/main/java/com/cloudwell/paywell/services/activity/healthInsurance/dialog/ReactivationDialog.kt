package com.cloudwell.paywell.services.activity.healthInsurance.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import com.cloudwell.paywell.services.activity.healthInsurance.model.TransactionDataItem
import kotlinx.android.synthetic.main.reactication_dialog.view.*


class ReactivationDialog(val item: TransactionDataItem, val onclick : reactiveInterface) : BaseDialogFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.reactication_dialog, null)

        val subscriptionID = view.trx_id
        val trxdate = view.health_trx_date
        val trxname = view.owner_name
        val packageType = view.package_type
        val trxmobileNumber = view.mobile_number
        val trx_time = view.trx_time
        val trxstatus = view.status
        val comment = view.comment
        val trxamount = view.package_amount


        subscriptionID.setText(getString(R.string.health_trx_id) + " " + item.trxId)
        packageType.setText(item.nameEn)
        trxmobileNumber.setText(item.customerPhoneNumber)
        trx_time.setText(item.activeDatetime)
        trxdate.setText(item.addDatetime)
        trxamount.setText(item.amount)
        trxname.setText(item.customerName)



        comment.setText(item.statusName)

        if(item.status.equals("200")){
            trxstatus.setTextColor(Color.parseColor("#33A544"))
            trxstatus.setText(getString(R.string.success_msg))
        }else{
            trxstatus.setTextColor(Color.parseColor("#FD9738"))
            trxstatus.setText(getString(R.string.failed_msg))
        }



        view.active_package.setOnClickListener(View.OnClickListener {
            onclick.onclick(item)
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

    interface reactiveInterface {
        fun onclick(packageItem : TransactionDataItem)
    }


}
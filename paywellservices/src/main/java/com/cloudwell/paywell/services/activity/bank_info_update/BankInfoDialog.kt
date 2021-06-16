package com.cloudwell.paywell.services.activity.bank_info_update

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.bank_info_update.model.BankDialogPojo
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment

/**
 * Created by Sepon on 5/6/2020.
 */
class BankInfoDialog (val mContext:Context, val dialogList : ArrayList<BankDialogPojo>, val onClick : OKInterface): BaseDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.bank_info_dialog, null)


        val recycler : RecyclerView = view.findViewById(R.id.bank_dialog_recyclerview)
        val dismissBtn : Button = view.findViewById(R.id.dismiss_btn)
        val OKBtn : Button = view.findViewById(R.id.submit_button)
        val adapter : BankDialogAdapter = BankDialogAdapter(mContext, dialogList)
        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false)
        recycler.layoutManager = linearLayoutManager
        recycler.adapter = adapter

        dismissBtn.setOnClickListener(View.OnClickListener { dismiss() })
        OKBtn.setOnClickListener(View.OnClickListener {
            if(onClick != null){
                onClick.onclick()
            }
            dismiss()
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

interface OKInterface {
    fun onclick()
}

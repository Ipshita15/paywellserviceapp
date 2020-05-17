package com.cloudwell.paywell.services.activity.bank_info_update

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.bank_info_update.model.BankDialogPojo
import kotlinx.android.synthetic.main.bank_dialog_item.view.*
import kotlinx.android.synthetic.main.bank_link_list_item.view.*


class BankLink_ListAdapter(val mContext: Context, var bList: List<BankDetailsListItem?>?, val onDelate : DelateInterface) : RecyclerView.Adapter<BankLink_ListAdapter.CourseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.bank_link_list_item,parent,false)

        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return bList!!.size
    }

    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {

            holder.acNumber.setText(mContext.getString(R.string.ac_no)+bList?.get(position)?.userAccount)
            holder.bankname.setText(bList?.get(position)?.bankName)
            holder.bankAddress.setText(bList?.get(position)?.branchname+", "+bList?.get(position)?.districtName)
            //holder.acNumber.setText(bList?.get(position)?.userAccount)
            holder.delateIcon.setOnClickListener(View.OnClickListener {
                if (onDelate != null){
                    onDelate.OnDelate(bList?.get(position))
                }
            })
    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val bankname = itemView.bankName
        val bankAddress = itemView.bank_adress
        val acNumber = itemView.acNumber
        val bankIcon = itemView.bankicon
        val delateIcon = itemView.delate_icon

    }

    interface DelateInterface{
        fun OnDelate(bank : BankDetailsListItem?)
    }

}



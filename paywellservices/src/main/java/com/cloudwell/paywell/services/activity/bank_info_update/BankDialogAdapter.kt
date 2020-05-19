package com.cloudwell.paywell.services.activity.bank_info_update

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.bank_info_update.model.BankDialogPojo
import kotlinx.android.synthetic.main.bank_dialog_item.view.*


class BankDialogAdapter(val mContext: Context, var bList: List<BankDialogPojo?>?) : RecyclerView.Adapter<BankDialogAdapter.CourseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.bank_dialog_item,parent,false)

        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return bList!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {
        holder.bank.text = bList?.get(position)?.bank
        holder.b_district.text = mContext.getString(R.string.district)+": "+bList?.get(position)?.district
        holder.b_branch.text =mContext.getString(R.string.branch)+": "+bList?.get(position)?.branch
        holder.ac.text =mContext.getString(R.string.ac_no)+": "+ bList?.get(position)?.account
        holder.b_counter.text = (position+1).toString()


    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val bank = itemView.bank
        val b_district = itemView.district
        val b_branch = itemView.branch
        val ac = itemView.ac
        val b_counter = itemView.counter


    }







}



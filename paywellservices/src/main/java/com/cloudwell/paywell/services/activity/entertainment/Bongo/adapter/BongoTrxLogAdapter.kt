package com.cloudwell.paywell.services.activity.entertainment.Bongo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.education.BBC.model.CoursesItem
import com.cloudwell.paywell.services.activity.education.BBC.model.ResponseDetailsItem
import com.cloudwell.paywell.services.activity.entertainment.Bongo.model.DataItem
import kotlinx.android.synthetic.main.bbc_trx_item.view.*
import kotlinx.android.synthetic.main.bbc_trx_item.view.txr_name
import kotlinx.android.synthetic.main.bongo_trx_item.view.*


class BongoTrxLogAdapter(val mContext: Context, var trList: List<DataItem?>?) : RecyclerView.Adapter<BongoTrxLogAdapter.CourseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.bongo_trx_item,parent,false)

        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return trList!!.size
    }

    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {

        holder.bongo_date.setText(trList?.get(position)?.addtime)
        holder.bongo_status.setText(trList?.get(position)?.statusName)
        holder.bongo_customerNumber.setText(trList?.get(position)?.customerMobileNo)
        holder.amount.setText(trList?.get(position)?.totalAmount)
        holder.courseSubscriberName.setText(trList?.get(position)?.title)

    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val courseSubscriberName = itemView.txr_name
        val bongo_customerNumber = itemView.bongo_number
        val bongo_status = itemView.bongo_status
        val bongo_date = itemView.bongo_trx_date
        val amount = itemView.amount_bongo

    }

    interface CourseOnClick{
        fun onclick(course : CoursesItem)
    }





}



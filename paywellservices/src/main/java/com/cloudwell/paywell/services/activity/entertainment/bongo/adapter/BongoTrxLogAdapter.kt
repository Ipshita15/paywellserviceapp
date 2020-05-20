package com.cloudwell.paywell.services.activity.entertainment.bongo.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.education.bbc.model.CoursesItem
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.ResponseDetailsItem
import kotlinx.android.synthetic.main.bbc_trx_item.view.txr_name
import kotlinx.android.synthetic.main.bongo_trx_item.view.*


class BongoTrxLogAdapter(val mContext: Context, var trList: ArrayList<ResponseDetailsItem?>?) : RecyclerView.Adapter<BongoTrxLogAdapter.CourseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.bongo_trx_item,parent,false)

        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return trList!!.size
    }

    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {

        holder.bongo_date.setText(trList?.get(position)?.addDatetime)
        holder.bongo_customerNumber.setText(trList?.get(position)?.customerMobileNo)
        holder.courseSubscriberName.setText(trList?.get(position)?.title)
        val status = trList?.get(position)?.statusName
        if (status.equals("Successful")){
            holder.bongo_status.setText(status)
            holder.bongo_status.setTextColor(mContext.resources.getColor(R.color.tab_background))
        }else{
            holder.bongo_status.setText(status)
            holder.bongo_status.setTextColor(Color.RED)
        }


    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val courseSubscriberName = itemView.txr_name
        val bongo_customerNumber = itemView.bongo_number
        val bongo_status = itemView.bongo_status
        val bongo_date = itemView.bongo_trx_date

    }

    interface CourseOnClick{
        fun onclick(course : CoursesItem)
    }





}



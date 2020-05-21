package com.cloudwell.paywell.services.activity.education.bbc.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.education.bbc.model.CoursesItem
import com.cloudwell.paywell.services.activity.education.bbc.model.ResponseDetailsItem
import kotlinx.android.synthetic.main.bbc_trx_item.view.*


class TransactionLogAdapter(val mContext: Context, var trList: List<ResponseDetailsItem?>?) : RecyclerView.Adapter<TransactionLogAdapter.CourseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.bbc_trx_item,parent,false)

        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return trList!!.size
    }

    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {

        holder.bbc_date.setText(trList?.get(position)?.addDatetime)
        holder.subscriptionID.setText(mContext.getString(R.string.bbc_subscription)+trList?.get(position)?.subscriptionId)
        holder.bbc_number.setText(trList?.get(position)?.mobileNo)
        holder.courseDes.setText(trList?.get(position)?.courseName)
        holder.courseSubscriberName.setText(mContext.getString(R.string.name)+trList?.get(position)?.fullName)
        val status = trList?.get(position)?.statusName
        if (status.equals("Successful")){
            holder.bbc_status.setText(status)
            holder.bbc_status.setTextColor(mContext.resources.getColor(R.color.tab_background))
        }else{
            holder.bbc_status.setText(status)
            holder.bbc_status.setTextColor(Color.RED)
        }

    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val courseSubscriberName = itemView.txr_name
        val courseDes = itemView.bbc_course
        val bbc_number = itemView.bbc_sub_number
        val bbc_status = itemView.bbc_status
        val bbc_date = itemView.bbc_trx_date
        val subscriptionID = itemView.bbc_subscription_id

    }

    interface CourseOnClick{
        fun onclick(course : CoursesItem)
    }





}



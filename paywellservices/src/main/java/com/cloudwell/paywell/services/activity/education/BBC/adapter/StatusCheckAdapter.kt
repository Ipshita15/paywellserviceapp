package com.cloudwell.paywell.services.activity.education.BBC.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.education.BBC.model.CoursesItem
import com.cloudwell.paywell.services.activity.education.BBC.model.StatusCheckCourseItem
import kotlinx.android.synthetic.main.status_check_item.view.*


class StatusCheckAdapter(val mContext: Context, var courseList: List<StatusCheckCourseItem?>?) : RecyclerView.Adapter<StatusCheckAdapter.CourseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.status_check_item,parent,false)
        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return courseList!!.size
    }

    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {

            holder.courseNo.setText("কোর্স - "+courseList?.get(position)?.courseNo)
            holder.courseStartDate.setText("মেয়াদ শুরুর তারিখ - "+courseList?.get(position)?.addDatetime)
            holder.courseEndDate.setText("মেয়াদ শেষের তারিখ - "+courseList?.get(position)?.expiryDatetime)

    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val courseNo = itemView.courseNo
        val courseStartDate = itemView.courseStartDate
        val courseEndDate = itemView.courseEndDate


    }

    interface CourseOnClick{
        fun onclick(course : CoursesItem)
    }





}



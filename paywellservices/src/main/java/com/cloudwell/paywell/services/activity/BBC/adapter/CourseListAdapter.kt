package com.cloudwell.paywell.services.activity.BBC.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.BBC.model.CoursesItem
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.ErrorCallBackMsgDialog
import kotlinx.android.synthetic.main.activity_course_list.view.*
import kotlinx.android.synthetic.main.bbc_course_item.view.*

class CourseListAdapter(val mContext: Context, var courseList : List<CoursesItem>, var onclick : CourseListAdapter.CourseOnClick) : RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder>() {
    var courselist: List<CoursesItem?>? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.bbc_course_item,parent,false)

        // Return the view holder
        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return courseList.size
    }

    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {

        holder.courseName.setText(courseList.get(position).courseName)
        holder.courseDes.setText(courseList.get(position).description)
        holder.courseAmount.setText(courseList.get(position).amount)

        holder.itemView.setOnClickListener(View.OnClickListener {
            if(onclick != null){
                onclick.onclick(courseList.get(position))
            }
        })
    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val courseName = itemView.courseName
        val courseDes = itemView.coursDescription
        val courseAmount = itemView.courseAmount

    }

    interface CourseOnClick{
        fun onclick(course : CoursesItem)
    }
}
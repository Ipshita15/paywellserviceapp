package com.cloudwell.paywell.services.activity.education.BBC.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.education.BBC.model.CoursesItem
import com.cloudwell.paywell.services.activity.education.BBC.model.ResponseDetailsItem
import kotlinx.android.synthetic.main.bbc_course_item.view.*
import net.cachapa.expandablelayout.ExpandableLayout


class TransactionLogAdapter(val mContext: Context, var trList: List<ResponseDetailsItem?>?) : RecyclerView.Adapter<TransactionLogAdapter.CourseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.bbc_course_item,parent,false)

         //   extendedLayout = v.extended
        // Return the view holder
        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return trList!!.size
    }

    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {

//        holder.courseName.setText(trList.get(position).courseName)
//        holder.courseDes.setText(trList.get(position).description)
//        holder.courseAmount.setText(trList.get(position).amount)
//
//        holder.expandableLayout.setInterpolator(OvershootInterpolator())

    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val courseName = itemView.courseName
        val courseDes = itemView.coursDescription
        val courseAmount = itemView.courseAmount
        val expended = itemView.expandable_layout

        val expandableLayout : ExpandableLayout = itemView.findViewById(R.id.expandable_layout)


    }

    interface CourseOnClick{
        fun onclick(course : CoursesItem)
    }





}



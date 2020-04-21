package com.cloudwell.paywell.services.activity.education.BBC.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.education.BBC.model.CoursesItem
import kotlinx.android.synthetic.main.bbc_course_item.view.*
import net.cachapa.expandablelayout.ExpandableLayout


class CourseListAdapter(val mContext: Context, var courseList : List<CoursesItem>, var onclick : CourseListAdapter.CourseOnClick) : RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder>() {
    var courselist: List<CoursesItem?>? = null

    var originalHeight : Int = 0
    var isViewExpanded : Boolean = false
    var extendedLayout : LinearLayout? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.bbc_course_item,parent,false)

         //   extendedLayout = v.extended
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

        holder.expandableLayout.setInterpolator(OvershootInterpolator())
        //holderexpandableLayout.setOnExpansionUpdateListener(this)


    //    holder.extentedLayout.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(View.OnClickListener {

       //         holder.extentedLayout.setVisibility(View.VISIBLE);

            if(onclick != null){
                onclick.onclick(courseList.get(position))
            }
        })
    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val courseName = itemView.courseName
        val courseDes = itemView.coursDescription
        val courseAmount = itemView.courseAmount
        val expended = itemView.expandable_layout

        val expandableLayout : ExpandableLayout = itemView.findViewById(R.id.expandable_layout)


      //  val extentedLayout = itemView.extended

    }

    interface CourseOnClick{
        fun onclick(course : CoursesItem)
    }





}



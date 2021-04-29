package com.cloudwell.paywell.services.activity.healthInsurance.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.education.bbc.model.CoursesItem
import com.cloudwell.paywell.services.activity.healthInsurance.model.TransactionDataItem
import kotlinx.android.synthetic.main.health_trx_item.view.*


class TrxLogAdapter(val mContext: Context, var trList: List<TransactionDataItem?>?) : RecyclerView.Adapter<TrxLogAdapter.CourseListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.health_trx_item,parent,false)

        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return trList!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {


        holder.subscriptionID.setText(mContext.getString(R.string.health_trx_id)+" "+trList?.get(position)?.trxId)
        holder.packageType.setText(trList?.get(position)?.nameEn)
        holder.trxmobileNumber.setText(trList?.get(position)?.customerPhoneNumber)
        holder.trx_time.setText(trList?.get(position)?.activeDatetime)
        holder.trxdate.setText(trList?.get(position)?.addDatetime)
        holder.trxstatus.setText(trList?.get(position)?.statusName)
        holder.trxamount.setText(trList?.get(position)?.amount)
        holder.trxname.setText(trList?.get(position)?.customerName)





//        holder.bbc_date.setText(trList?.get(position)?.addDatetime)
//        holder.subscriptionID.setText(mContext.getString(R.string.bbc_subscription)+trList?.get(position)?.subscriptionId)
//        holder.bbc_number.setText(trList?.get(position)?.mobileNo)
//        holder.courseDes.setText(trList?.get(position)?.courseName)
//        holder.courseSubscriberName.setText(mContext.getString(R.string.name)+trList?.get(position)?.fullName)
//        val status = trList?.get(position)?.statusName
//        if (status.equals("Successful")){
//            holder.bbc_status.setText(status)
//            holder.bbc_status.setTextColor(mContext.resources.getColor(R.color.tab_background))
//        }else{
//            holder.bbc_status.setText(status)
//            holder.bbc_status.setTextColor(Color.RED)
//        }

    }


    class CourseListViewHolder(v: View) : RecyclerView.ViewHolder(v) {

               val subscriptionID = itemView.trx_id
               val trxdate = itemView.health_trx_date
               val trxname = itemView.owner_name
               val packageType = itemView.package_type
               val trxmobileNumber = itemView.mobile_number
               val trx_time = itemView.trx_time
               val trxstatus = itemView.status
               val trxamount = itemView.package_amount


    }

    interface CourseOnClick{
        fun onclick(course : CoursesItem)
    }





}



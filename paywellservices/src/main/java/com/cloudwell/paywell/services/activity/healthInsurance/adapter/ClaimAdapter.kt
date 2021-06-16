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
import com.cloudwell.paywell.services.activity.healthInsurance.model.ClaimDataItem
import com.cloudwell.paywell.services.activity.healthInsurance.model.TransactionDataItem
import kotlinx.android.synthetic.main.health_trx_item.view.*


class ClaimAdapter(val mContext: Context, var trList: List<ClaimDataItem?>?) : RecyclerView.Adapter<ClaimAdapter.CourseListViewHolder>() {

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
        holder.trxamount.setText(trList?.get(position)?.amount)
        holder.trxname.setText(trList?.get(position)?.customerName)


        holder.trxstatus.setText(trList?.get(position)?.statusName)

        if(trList?.get(position)?.status.equals("200")){
                holder.trxstatus.setTextColor(Color.parseColor("#33A544"))

        }else{
            holder.trxstatus.setTextColor(Color.parseColor("#FD9738"))
        }




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



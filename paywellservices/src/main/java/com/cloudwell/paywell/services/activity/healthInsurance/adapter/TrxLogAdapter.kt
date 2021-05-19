package com.cloudwell.paywell.services.activity.healthInsurance.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.healthInsurance.model.TransactionDataItem
import kotlinx.android.synthetic.main.health_trx_item.view.*


class TrxLogAdapter(val mContext: Context, var trList: List<TransactionDataItem?>?) : RecyclerView.Adapter<TrxLogAdapter.CourseListViewHolder>() {


    var ontrxclick : trxOnClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseListViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.health_trx_item, parent, false)

        return CourseListViewHolder(v)
    }

    override fun getItemCount(): Int {
       return trList!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CourseListViewHolder, position: Int) {


        holder.subscriptionID.setText(mContext.getString(R.string.health_trx_id) + " " + trList?.get(position)?.trxId)
        holder.packageType.setText(trList?.get(position)?.nameEn)
        holder.trxmobileNumber.setText(trList?.get(position)?.customerPhoneNumber)
        holder.trx_time.setText(trList?.get(position)?.activeDatetime)
        holder.trxdate.setText(trList?.get(position)?.addDatetime)
        holder.trxamount.setText(trList?.get(position)?.amount)
        holder.trxname.setText(trList?.get(position)?.customerName)



//        holder.trxstatus.setText(trList?.get(position)?.statusName)

        if(trList?.get(position)?.status.equals("200")){
                holder.trxstatus.setTextColor(Color.parseColor("#33A544"))
            holder.trxstatus.setText(mContext.getString(R.string.success_msg))
        }else{
            holder.trxstatus.setTextColor(Color.parseColor("#FD9738"))
            holder.trxstatus.setText(mContext.getString(R.string.failed_msg))
        }




//        holder.itemView.setOnClickListener(View.OnClickListener {
//            clickListener?.onPaymentClick(courselist.get(position), it, position)


        holder.itemView.setOnClickListener(View.OnClickListener {

            trList?.get(position)?.let { it1 -> ontrxclick?.onclick(it1) }
        })

    }

    fun setClickListener(itemClickListener: trxOnClick) {
        ontrxclick = itemClickListener
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

    interface trxOnClick{
        fun onclick(item: TransactionDataItem)
    }





}



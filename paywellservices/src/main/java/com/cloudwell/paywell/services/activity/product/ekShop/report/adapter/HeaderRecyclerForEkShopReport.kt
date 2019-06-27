package com.cloudwell.paywell.services.activity.product.ekShop.report.adapter


import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.product.ekShop.model.Data
import com.cloudwell.paywell.services.activity.product.ekShop.model.OrderDetail
import com.cloudwell.paywell.services.app.AppController
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
class HeaderRecyclerForEkShopReport(private val title: String, private val list: List<Data>, private val isEnglish: Boolean, val onCLick: OnClickHandler) : StatelessSection(R.layout.header_ek_shop_report, R.layout.item_ek_shope_report) {
    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolderReport(view)
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolderHeaderReport(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val get = list.get(position)
        val hHolder = holder as ItemViewHolderReport

        holder.tvStatus.text = "Status : " + get.status
        holder.tvOrderCode.text = "Order Code : " + get.orderCode
        holder.tvAmount.text = "Total Amount : " + get.totalAmount

        holder.rootLayout.setOnClickListener {
            onCLick.onDetailsClick(get.orderDetails)

        }

        holder.btShowOrderDetails.setOnClickListener {
            onCLick.onDetailsClick(get.orderDetails)

        }


    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {

        val hHolder = holder as ItemViewHolderHeaderReport
        hHolder.headerTitle.text = title
        if (isEnglish) {
            hHolder.headerTitle.typeface = AppController.getInstance().oxygenLightFont
        } else {
            hHolder.headerTitle.typeface = AppController.getInstance().aponaLohitFont
        }
    }


}

class ItemViewHolderHeaderReport(view: View) : RecyclerView.ViewHolder(view) {
    val headerTitle = itemView.findViewById<View>(R.id.headerEkShopeTitle) as TextView


}

internal class ItemViewHolderReport(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvStatus = itemView.findViewById<View>(R.id.tvStatus) as TextView
    val tvOrderCode = itemView.findViewById<View>(R.id.tvOrderCode) as TextView
    val tvAmount = itemView.findViewById<View>(R.id.tvOrderTotalAmont) as TextView
    val rootLayout = itemView.findViewById<View>(R.id.rootLayout) as ConstraintLayout
    val btShowOrderDetails = itemView.findViewById<View>(R.id.btShowOrderDetails) as Button


}

interface OnClickHandler {
    fun onDetailsClick(orderDetails: List<OrderDetail>?)
}
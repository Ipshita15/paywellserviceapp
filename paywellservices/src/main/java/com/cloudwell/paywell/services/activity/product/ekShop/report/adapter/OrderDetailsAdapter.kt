package com.cloudwell.paywell.services.activity.product.ekShop.report.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.activity.product.ekShop.model.OrderDetail
import com.squareup.picasso.Picasso


class OrderDetailsAdapter(val data: Array<OrderDetail>, val english: Boolean) : RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.cloudwell.paywell.services.R.layout.order_details, parent, false)
        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val get = data.get(position)
        holder.tvOrderCode.text = "Order code: " + get.orderCode
        holder.tvOrderName.text = "Product name: " + get.name
        holder.tvOrderPrices.text = "Product price: " + get.price

        Picasso.get()
                .load(get.image)
                .into(holder.iv_order);

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvOrderName = itemView.findViewById<View>(com.cloudwell.paywell.services.R.id.tvOrderName) as TextView
        val tvOrderCode = itemView.findViewById<View>(com.cloudwell.paywell.services.R.id.tvOrderCode) as TextView
        val tvOrderPrices = itemView.findViewById<View>(com.cloudwell.paywell.services.R.id.tvOrderPrices) as TextView
        val iv_order = itemView.findViewById<View>(com.cloudwell.paywell.services.R.id.iv_order) as ImageView


    }

}

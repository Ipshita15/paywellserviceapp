package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.app.AppController
import com.orhanobut.logger.Logger

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 17/1/19.
 */
class TransitionRVSectionAdapter(private val title: String, private val list: List<Datum>, private val mIsEnglish: Boolean) : StatelessSection(R.layout.item_header_air_tricket_transtion_log, R.layout.item_child_airtricket_transtion_log) {
    private var onActionButtonClick: ItemClickListener? = null


    fun setOnActionButtonClick(onActionButtonClick: ItemClickListener) {
        this.onActionButtonClick = onActionButtonClick
    }

    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val iHolder = holder as ItemViewHolder
        val model = list[position]

        if (mIsEnglish) {
            iHolder.tvBookingId.typeface = AppController.getInstance().oxygenLightFont
            iHolder.tvTricketPrices.typeface = AppController.getInstance().oxygenLightFont
            iHolder.tvStatus.typeface = AppController.getInstance().oxygenLightFont

        } else {
            iHolder.tvBookingId.typeface = AppController.getInstance().aponaLohitFont
            iHolder.tvTricketPrices.typeface = AppController.getInstance().aponaLohitFont
            iHolder.tvStatus.typeface = AppController.getInstance().aponaLohitFont
        }

        iHolder.tvBookingId.text = "Booking ID: " + model.bookingId!!
        iHolder.tvTricketPrices.text = "Price: " + model.currency + " " + `model`.totalFare
        iHolder.tvStatus.text = "Status: " + model.message!!


        iHolder.mLinearLayout.setOnClickListener { }


        if (`model`.message != null) {
            if (`model`.invoiceUrl != null) {
                iHolder.ivSymbolTicketed.visibility = View.VISIBLE
            } else {
                iHolder.ivSymbolTicketed.visibility = View.INVISIBLE
            }
        } else {
            iHolder.ivSymbolTicketed.visibility = View.INVISIBLE
        }


        iHolder.ivSymbolTicketed.setOnClickListener { v ->
            if (`model`.invoiceUrl != null) {
                Logger.v("InvoiceURl: " + model.invoiceUrl)
                onActionButtonClick!!.onItemClick(model)
            }
        }

        iHolder.tvAction.setOnClickListener { v -> onActionButtonClick!!.onActionButtonClick(position, model) }


    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val hHolder = holder as HeaderViewHolder?
        hHolder!!.tvHeader.text = title
        if (mIsEnglish) {
            hHolder.tvHeader.typeface = AppController.getInstance().oxygenLightFont
        } else {
            hHolder.tvHeader.typeface = AppController.getInstance().aponaLohitFont
        }
    }

    private inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvBookingId: TextView
        internal var tvTricketPrices: TextView
        internal var tvStatus: TextView
        internal var mLinearLayout: LinearLayout
        internal var ivSymbolTicketed: ImageView
        internal var tvAction: ImageView


        init {
            tvBookingId = view.findViewById(R.id.tvBookingId)
            tvTricketPrices = view.findViewById(R.id.tvTricketPrices)
            tvStatus = view.findViewById(R.id.tvStatus)
            mLinearLayout = view.findViewById(R.id.ivRootLayout)
            ivSymbolTicketed = view.findViewById(R.id.ivSymbolTicketed)
            tvAction = view.findViewById(R.id.ivActionTrans)

        }
    }

    private inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvHeader: TextView

        init {
            tvHeader = view.findViewById(R.id.header)
        }
    }

    interface ItemClickListener {

        fun onItemClick(datum: Datum)

        fun onActionButtonClick(position: Int, model: Datum)

    }

    companion object {
        private val TAG = TransitionRVSectionAdapter::class.java.simpleName
    }
}


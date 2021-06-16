package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.adapter

import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.constant.AllConstant
import com.orhanobut.logger.Logger
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection
import java.text.NumberFormat


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 17/1/19.
 */
class TransitionRVSectionAdapter(val mContext: Context, private val title: String, private val list: List<Datum>, private val mIsEnglish: Boolean) : StatelessSection(R.layout.item_header_air_tricket_transtion_log, R.layout.item_child_airtricket_transtion_log) {
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

//        use for testing purpose
//        model.message = "Ticketed";
//        model.invoiceUrl = "http://www.africau.edu/images/default/sample.pdf"
//        model.invoiceUrlWithFare = "http://www.pdf995.com/samples/pdf.pdf"


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
        iHolder.tvTricketPrices.text = model.currency + " " + NumberFormat.getInstance().format(model.total_fare_calculated.toDouble())
        iHolder.tvStatus.text = "Status: " + model.message!!


        if (`model`.message != null) {
            if (`model`.invoiceUrl != null) {
                iHolder.ivSymbolTicketed.visibility = View.VISIBLE
                iHolder.viewPdfView.visibility = View.VISIBLE
            } else {
                iHolder.ivSymbolTicketed.visibility = View.INVISIBLE
                iHolder.viewPdfView.visibility = View.INVISIBLE
            }
        } else {
            iHolder.ivSymbolTicketed.visibility = View.INVISIBLE
            iHolder.viewPdfView.visibility = View.INVISIBLE
        }


        iHolder.ivSymbolTicketed.setOnClickListener { v ->
            if (`model`.invoiceUrl != null) {
                Logger.v("InvoiceURl: " + model.invoiceUrl)
                onActionButtonClick!!.onItemClick(model)
            }
        }

        iHolder.viewPdfView.setOnClickListener {
            if (`model`.invoiceUrl != null) {
                Logger.v("InvoiceURl: " + model.invoiceUrl)
                onActionButtonClick!!.onItemClick(model)
            }
        }


        val m = model.message
        if (m.equals(AllConstant.Booked) || m.equals(AllConstant.Ticketed)) {
            iHolder.tvAction.visibility = View.VISIBLE
        } else {
            iHolder.tvAction.visibility = View.GONE
        }

        iHolder.tvAction.setOnClickListener { v -> onActionButtonClick!!.onActionButtonClick(position, model) }
        iHolder.viewBackgroudAction.setOnClickListener {
            onActionButtonClick!!.onActionButtonClick(position, model)
        }


        iHolder.ivRootLayout.setOnClickListener {

            onActionButtonClick?.onRootViewClick(datum = model)
        }

        iHolder.tvBookingId.setOnClickListener {

            val clipboard = mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setText(model.bookingId)
            Toast.makeText(mContext, "Booking ID to clip", Toast.LENGTH_LONG).show()

        }

        iHolder.ivCopy.setOnClickListener {

            val clipboard = mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setText(model.bookingId)
            Toast.makeText(mContext, "Booking ID to clip", Toast.LENGTH_LONG).show()

        }


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
        internal var ivRootLayout: LinearLayout
        internal var tvBookingId: TextView
        internal var tvTricketPrices: TextView
        internal var tvStatus: TextView
        internal var ivSymbolTicketed: ImageView
        internal var ivCopy: ImageView
        internal var tvAction: ImageView
        internal var viewBackgroudAction: View
        internal var viewPdfView: View


        init {
            ivRootLayout = view.findViewById(R.id.ivRootLayout)
            tvBookingId = view.findViewById(R.id.tvBookingId)
            ivCopy = view.findViewById(R.id.ivCopy)
            tvTricketPrices = view.findViewById(R.id.tvTricketPrices)
            tvStatus = view.findViewById(R.id.tvStatus)
            ivSymbolTicketed = view.findViewById(R.id.ivSymbolTicketed)
            tvAction = view.findViewById(R.id.ivActionTrans)
            viewBackgroudAction = view.findViewById(R.id.viewBackgroudAction1)
            viewPdfView = view.findViewById(R.id.viewPdfView)

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
        fun onRootViewClick(datum: Datum)

    }

    companion object {
        private val TAG = TransitionRVSectionAdapter::class.java.simpleName
    }
}


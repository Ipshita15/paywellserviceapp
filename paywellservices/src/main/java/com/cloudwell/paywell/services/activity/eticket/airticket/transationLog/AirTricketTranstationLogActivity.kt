package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.BookingStatuViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.viewModel.BookingStatuViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.menu.AirTicketMenuActivity
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu
import kotlinx.android.synthetic.main.activity_booking_main.*
import kotlinx.android.synthetic.main.item_booking_status_transtionlog.*
import kotlinx.android.synthetic.main.test.view.*
import java.text.SimpleDateFormat
import java.util.*

class AirTricketTranstationLogActivity : AirTricketBaseActivity() {

    lateinit var tag: String

    private lateinit var viewMode: BookingStatuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_transtionlog)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.transaction_log))


        val bundle = intent.extras
        val limit = bundle.getInt(AirTicketMenuActivity.KEY_LIMIT)

        initViewModel(limit)
    }

    private fun initViewModel(limit: Int) {

        viewMode = ViewModelProviders.of(this).get(BookingStatuViewModel::class.java)

        viewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })


        viewMode.mViewStatus.observe(this, Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })


        viewMode.responseList.observe(this, Observer {
            it?.let { it1 -> setupList(it1) }
        })



        viewMode.getBookingStatus(isInternetConnection, limit)


    }

    private fun setupList(it1: BookingList) {

        val customAdapter = MyAdapterFortranstationLog(this, it1.data)
        listBookingList.adapter = customAdapter


        val mLayoutManager = LinearLayoutManager(applicationContext)
        listBookingList.setLayoutManager(mLayoutManager)
        listBookingList.setItemAnimator(DefaultItemAnimator())

        tvSerialNumber.typeface = Typeface.DEFAULT_BOLD
        tvBookingId.typeface = Typeface.DEFAULT_BOLD
        tvDate.typeface = Typeface.DEFAULT_BOLD
        tvBookingStatus.typeface = Typeface.DEFAULT_BOLD
        tvAction.typeface = Typeface.DEFAULT_BOLD
        tvAction.text = "Ticket Price"

    }

    private fun handleViewStatus(it: BookingStatuViewStatus) {
        if (it.isShowProcessIndicatior) {
            showProgressDialog()
        } else {
            dismissProgressDialog()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }

    inner class MyAdapterFortranstationLog : RecyclerView.Adapter<ViewHolder> {


        var context: Context? = null

        private var mInflater: LayoutInflater?
        private var data: List<Datum>


        constructor(context: Context, data: List<Datum>) {
            this.context = context
            this.mInflater = LayoutInflater.from(context)
            this.data = data
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = mInflater!!.inflate(com.cloudwell.paywell.services.R.layout.item_booking_status_transtionlog, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val count = position + 1

            val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
            val inputDateStr = data.get(position).firstRequestDateTime
            val date = inputFormat.parse(inputDateStr)
            val outputDateStr = outputFormat.format(date)

            holder.tvSerialNumber.setText("" + count)
            holder.tvDate.setText(data.get(position).bookingId)
            holder.tvDate.setText(outputDateStr)
            holder.tvBookingStatus.setText(data.get(position).message)
            holder.tvBookingId.setText(data.get(position).bookingId)
            holder.tvAction.setText(data.get(position).totalFare)

            if (position % 2 == 0)
                holder.rootLayout_booking_status.setBackgroundColor(Color.parseColor("#d8ead2"))
            else
                holder.rootLayout_booking_status.setBackgroundColor(Color.parseColor("#b0d5a4"))


        }


    }


    interface ItemClickListener {

        fun onItemClick(position: Int, favoriteMenu1: FavoriteMenu)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        // Holds the TextView that will add each animal to
        val rootLayout_booking_status = view.rootLayout_booking_status
        val tvSerialNumber = view.tvSerialNumber
        val tvBookingId = view.tvBookingId
        val tvDate = view.tvDate
        val tvBookingStatus = view.tvBookingStatus
        val tvAction = view.tvAction

    }

}

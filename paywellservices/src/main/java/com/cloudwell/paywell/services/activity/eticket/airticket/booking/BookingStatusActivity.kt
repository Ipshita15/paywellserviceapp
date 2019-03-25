package com.cloudwell.paywell.services.activity.eticket.airticket.booking

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.activity_booking_main.*
import kotlinx.android.synthetic.main.item_booking_status.*
import kotlinx.android.synthetic.main.test.view.*
import java.text.SimpleDateFormat
import java.util.*


class BookingStatusActivity : AirTricketBaseActivity() {

    lateinit var responseList: BookingList
    lateinit var tag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_booking_main)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = getString(com.cloudwell.paywell.services.R.string.booking_status_menu)
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#189d49")));
        }
        responseList = AppStorageBox.get(applicationContext, AppStorageBox.Key.AIRTICKET_BOOKING_RESPONSE) as BookingList

        val bundle = intent.extras
        if (!bundle.isEmpty) {
            tag = bundle.getString("tag")
        }
        initViewInitialization()
    }

    private fun initViewInitialization() {
        val customAdapter = MyAdapter(this)
        listBookingList.adapter = customAdapter


        val mLayoutManager = LinearLayoutManager(applicationContext)
        listBookingList.setLayoutManager(mLayoutManager)
        listBookingList.setItemAnimator(DefaultItemAnimator())

        tvSerialNumber.typeface = Typeface.DEFAULT_BOLD
        tvBookingId.typeface = Typeface.DEFAULT_BOLD
        tvDate.typeface = Typeface.DEFAULT_BOLD
        tvBookingStatus.typeface = Typeface.DEFAULT_BOLD
        tvAction.typeface = Typeface.DEFAULT_BOLD


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

    inner class MyAdapter : RecyclerView.Adapter<ViewHolder> {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = mInflater!!.inflate(com.cloudwell.paywell.services.R.layout.item_booking_status, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return responseList.data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val count = position + 1

            val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
            val inputDateStr = responseList.data.get(position).firstRequestDateTime
            val date = inputFormat.parse(inputDateStr)
            val outputDateStr = outputFormat.format(date)

            holder.tvSerialNumber.setText("" + count)
            holder.tvDate.setText(responseList.data.get(position).bookingId)
            holder.tvDate.setText(outputDateStr)
            holder.tvBookingStatus.setText(responseList.data.get(position).message)
            holder.tvBookingId.setText(responseList.data.get(position).bookingId)


            if (position % 2 == 0)
                holder.rootLayout_booking_status.setBackgroundColor(Color.parseColor("#d8ead2"))
            else
                holder.rootLayout_booking_status.setBackgroundColor(Color.parseColor("#b0d5a4"))


        }

        var context: Context? = null

        private var mInflater: LayoutInflater?

        constructor(context: Context) {
            this.context = context
            this.mInflater = LayoutInflater.from(context)
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

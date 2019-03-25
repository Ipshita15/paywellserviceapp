package com.cloudwell.paywell.services.activity.eticket.airticket.booking

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.TextView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.activity_booking_main.*
import kotlinx.android.synthetic.main.list_item_booking_ticket.*
import java.text.SimpleDateFormat
import java.util.*

class BookingMainActivity : AirTricketBaseActivity() {

    lateinit var responseList: BookingList
    lateinit var tag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_main)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = getString(R.string.booking_status_menu)
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
        val customAdapter = MyAdapter(this@BookingMainActivity)
        listBookingList.adapter = customAdapter

        firstCol.findViewById<TextView>(R.id.tvContainer).text = "SL"
        firstCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        secondCol.findViewById<TextView>(R.id.tvContainer).text = "Booking ID"
        secondCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        thirdCol.findViewById<TextView>(R.id.tvContainer).text = "Date"
        thirdCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        fourthCol.findViewById<TextView>(R.id.tvContainer).text = "Booking Status"
        fourthCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        if (tag.equals("BOOKING")) {
            fifthCol.findViewById<TextView>(R.id.tvContainer).text = "Action"
            fifthCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        } else {
            fifthCol.findViewById<TextView>(R.id.tvContainer).text = "Ticket Price"
            fifthCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.airticket_menu, menu)
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

    inner class MyAdapter : BaseAdapter {

        var context: Context? = null

        constructor(context: Context?) {
            this.context = context
        }

        override fun getItem(position: Int): Any {
            return responseList.data.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return responseList.data.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = LayoutInflater.from(context).inflate(R.layout.list_item_booking_ticket, parent, false)

            val colOne = view.findViewById<View>(R.id.firstCol).findViewById<TextView>(R.id.tvContainer)
            val colTwo = view.findViewById<View>(R.id.secondCol).findViewById<TextView>(R.id.tvContainer)
            val colThree = view.findViewById<View>(R.id.thirdCol).findViewById<TextView>(R.id.tvContainer)
            val colFour = view.findViewById<View>(R.id.fourthCol).findViewById<TextView>(R.id.tvContainer)
            val colFive = view.findViewById<View>(R.id.fifthCol).findViewById<TextView>(R.id.tvContainer)

            val count = position + 1

            val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)
            val inputDateStr = responseList.data.get(position).firstRequestDateTime
            val date = inputFormat.parse(inputDateStr)
            val outputDateStr = outputFormat.format(date)

            colOne.setText("" + count)
            colTwo.setText(responseList.data.get(position).bookingId)
            colThree.setText(outputDateStr)
            colFour.setText(responseList.data.get(position).message)
            if (tag.equals("BOOKING")) {
                colFive.setBackgroundResource(R.drawable.action_drawable)
            } else {
                colFive.setText(responseList.data.get(position).totalFare)
            }

//            val model = userList.get(position)
//
//            holder.tvUserType.setText(model.getSelectedUserType())
//            holder.tvUserCount.setText("" + model.getSelectedPsngrCount())
//
//            holder.ivMinus.setOnClickListener(View.OnClickListener {
//                val count = model.getSelectedPsngrCount()
//                if (count.compareTo(0) == 1) {
//                    val data = model.getSelectedPsngrCount().minus(1)
//                    model.setSelectedPsngrCount(data)
//
//                    updateRecords(userList)
//                }
//            })
//
//            holder.ivPlus.setOnClickListener(View.OnClickListener {
//                val data = model.getSelectedPsngrCount().plus(1)
//                model.setSelectedPsngrCount(data)
//
//                updateRecords(userList)
//            })

            if (position % 2 == 0)
                view.setBackgroundColor(Color.parseColor("#d8ead2"))
            else
                view.setBackgroundColor(Color.parseColor("#b0d5a4"))
            return view
        }

    }
}

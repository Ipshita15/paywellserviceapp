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
        if (bundle != null) {
//            responseList = bundle.getString("list") as BookingList
            responseList = bundle.getSerializable("list") as BookingList
        }

        initViewInitialization()
    }

    private fun initViewInitialization() {
        val customAdapter = MyAdapter(this)
        listBookingList.adapter = customAdapter

        firstCol.findViewById<TextView>(R.id.tvContainer).text = "SL"
        firstCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        secondCol.findViewById<TextView>(R.id.tvContainer).text = "Booking ID"
        secondCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        thirdCol.findViewById<TextView>(R.id.tvContainer).text = "Date"
        thirdCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        fourthCol.findViewById<TextView>(R.id.tvContainer).text = "Booking Status"
        fourthCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
//        fifthCol.findViewById<TextView>(R.id.tvContainer).text = "Ticket Status"
//        fifthCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
        fifthCol.findViewById<TextView>(R.id.tvContainer).text = "Action"
        fifthCol.findViewById<TextView>(R.id.tvContainer).typeface = Typeface.DEFAULT_BOLD
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

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
//            val v = super.getView(position, convertView, parent) as LinearLayout
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.list_item_booking_ticket, parent, false)

            val colOne = view.findViewById<View>(R.id.firstCol).findViewById<TextView>(R.id.tvContainer)
            val colTwo = view.findViewById<View>(R.id.secondCol).findViewById<TextView>(R.id.tvContainer)
            val colThree = view.findViewById<View>(R.id.thirdCol).findViewById<TextView>(R.id.tvContainer)
            val colFour = view.findViewById<View>(R.id.fourthCol).findViewById<TextView>(R.id.tvContainer)
            val colFive = view.findViewById<View>(R.id.fifthCol).findViewById<TextView>(R.id.tvContainer)

            val count = position + 1
            val sdf = SimpleDateFormat("dd/MM/yy", Locale.ENGLISH)

            colOne.setText("" + count)
            colTwo.setText(responseList.data.get(position).bookingId)
            colThree.setText(sdf.format(responseList.data.get(position).firstRequestDateTime))
            colFour.setText(responseList.data.get(position).message)
            colFive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group, 0, 0, 0)

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
                convertView.setBackgroundColor(Color.parseColor("#b0d5a4"))
            else
                convertView.setBackgroundColor(Color.parseColor("#d8ead2"))
            return convertView
        }

        inner class ViewHolder {
            var viewOne: View? = null
            var viewTwo: View? = null
            var viewThree: View? = null
            var viewFour: View? = null
            var viewFive: View? = null
        }
    }
}

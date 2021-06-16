package com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.model.Gender
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.class_item_list_dialog.view.*


class PassengerTypeSheetDialog : BottomSheetDialogFragment() {

    lateinit var mListenerClass: ClassBottomSheetListener
    lateinit var data: ArrayList<Gender>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val className: String? = arguments!!.getString("passengerType")
        val view = inflater.inflate(R.layout.passenger_type_item_list_dialog, container, false)
        view.tvCatagory.text = getString(R.string.passenger_type)

        val listView = view.findViewById<ListView>(R.id.listViewAirTicketClass)

        data = ArrayList()
        data.add(Gender(getString(R.string.adult), false))
        data.add(Gender(getString(R.string.child), false))
        data.add(Gender(getString(R.string.infant), false))

        data.get(getClassNamePosition(className.toString())).setClassSelectedStatus(true)

        val customAdapter = CustomAdapter(context, data)
        listView.adapter = customAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val model = data.get(position)
            if (model.isSelected)
                model.setClassSelectedStatus(false)
            else
                model.setClassSelectedStatus(true)

            data.set(position, model)

            customAdapter.updateRecords(data)
            mListenerClass.onButtonClickListener("" + model.name)
            dismiss()
        }

        return view
    }

    public fun setOnClassListener(classBottomSheetListener: ClassBottomSheetListener) {
        mListenerClass = classBottomSheetListener

    }

    private fun getClassNamePosition(value: String): Int {
        var position = 0
        for (i in data.indices) {
            val classModel = data.get(i)
            if (classModel.name.equals(value))
                position = i
        }
        return position
    }

    interface ClassBottomSheetListener {
        fun onButtonClickListener(text: String)
    }


    class CustomAdapter : BaseAdapter {

        var context: Context? = null
        var classList: List<Gender>

        constructor(context: Context?, classList: ArrayList<Gender>) {
            this.context = context
            this.classList = classList
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.class_list_view_item, parent, false)
            val holder = ViewHolder()
            holder.tvUserClass = view.findViewById(R.id.tv_user_class)
            holder.ivCheckBox = view.findViewById(R.id.iv_check_box)

            val model = classList.get(position)

            holder.tvUserClass.setText(model.name)
            if (model.isSelected) {
                holder.tvUserClass.setTextColor(Color.parseColor("#36b24a"))
                holder.ivCheckBox.setBackgroundResource(R.drawable.check)
            } else
                holder.ivCheckBox.visibility = View.INVISIBLE

            return view
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return classList.size
        }

        fun updateRecords(classes: List<Gender>) {
            this.classList = classes

            notifyDataSetChanged()
        }

        class ViewHolder {
            lateinit var tvUserClass: TextView
            lateinit var ivCheckBox: ImageView
        }
    }
}
package com.cloudwell.paywell.services.activity.eticket.airticket

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.cloudwell.paywell.services.R


class ClassBottomSheetDialog : BottomSheetDialogFragment() {

    lateinit var mListenerClass: ClassBottomSheetListener
    lateinit var classes: ArrayList<ClassModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val className: String = arguments!!.getString("myClassName")
        val view = inflater.inflate(R.layout.class_item_list_dialog, container, false)

        val listView = view.findViewById<ListView>(R.id.listViewAirTicketClass)

        classes = ArrayList()
        classes.add(ClassModel("Economy/Primium Economy", false))
        classes.add(ClassModel("Primium Economy", false))
        classes.add(ClassModel("Business/First", false))
        classes.add(ClassModel("Business", false))
        classes.add(ClassModel("First", false))

        classes.get(getClassNamePosition(className)).setClassSelectedStatus(true)

        val customAdapter = CustomAdapter(context, classes)
        listView.adapter = customAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val model = classes.get(position)
            if (model.isSelected)
                model.setClassSelectedStatus(false)
            else
                model.setClassSelectedStatus(true)

            classes.set(position, model)

            customAdapter.updateRecords(classes)
            mListenerClass.onButtonClickListener("" + model.getClassSelectedName())
            dismiss()
        }

        return view
    }

    public fun setOnClassListener(classBottomSheetListener: ClassBottomSheetListener) {
        mListenerClass = classBottomSheetListener

    }

    private fun getClassNamePosition(value: String): Int {
        var position = 0
        for (i in classes.indices) {
            val classModel = classes.get(i)
            if (classModel.getClassSelectedName().equals(value))
                position = i
        }
        return position
    }

    interface ClassBottomSheetListener {
        fun onButtonClickListener(text: String)
    }


    class CustomAdapter : BaseAdapter {

        var context: Context? = null
        var classList: List<ClassModel>

        constructor(context: Context?, classList: List<ClassModel>) {
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

            holder.tvUserClass.setText(model.getClassSelectedName())
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

        fun updateRecords(classes: List<ClassModel>) {
            this.classList = classes

            notifyDataSetChanged()
        }

        class ViewHolder {
            lateinit var tvUserClass: TextView
            lateinit var ivCheckBox: ImageView
        }
    }
}
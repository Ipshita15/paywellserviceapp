package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView

import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.utils.FormatHelper

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 6/3/19.
 */
class ExpandableListAdapter(private val _context: Context, private val _listDataHeader: List<String> // header titles
                            , // child data in format of header title, child title
                            private val _listDataChild: MutableMap<String, ArrayList<String>>) : BaseExpandableListAdapter() {

    override fun getChild(groupPosition: Int, childPosititon: Int): Any {
        return this._listDataChild[this._listDataHeader[groupPosition]]?.get(childPosititon)!!
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val childText = getChild(groupPosition, childPosition) as String

        if (convertView == null) {
            val infalInflater = this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.layout_roles_child, null)
        }

        val txtListChild = convertView!!.findViewById<View>(R.id.lblListItem) as TextView

        txtListChild.text = FormatHelper.formatText(childText)
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this._listDataChild[this._listDataHeader[groupPosition]]?.size!!
    }

    override fun getGroup(groupPosition: Int): Any {
        return this._listDataHeader[groupPosition]
    }

    override fun getGroupCount(): Int {
        return this._listDataHeader.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as String
        if (convertView == null) {
            val infalInflater = this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.layout_roles_parent, null)
        }

        val lblListHeader = convertView!!.findViewById<View>(R.id.tvQuestion) as TextView
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = FormatHelper.formatText(headerTitle)


        val img = convertView.findViewById<ImageView>(R.id.imgDropDown)

        if (isExpanded) {
            img.setImageResource(R.drawable.ic_expandable_up)
        } else {
            img.setImageResource(R.drawable.expandable_down)

        }


        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}

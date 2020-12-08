package com.cloudwell.paywell.services.activity.bank_info_update.spineer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.refill.banktransfer.model.DistrictData
import kotlinx.android.synthetic.main.spinner_item_layout.view.*

/**
 * Created by Sepon on 3/10/2020.
 */
class CustomDistrictAdapter(ctx: Context, moods: List<DistrictData>?) : ArrayAdapter<DistrictData>(ctx, 0, moods!!) {

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val mood = getItem(position)
        val view = recycledView
                ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_layout, parent, false)
        view.spinner_item.text = mood?.district_name ?: ""
        return view
    }

}